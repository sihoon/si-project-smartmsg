package component
{
	/* For guidance on writing an ActionScript Skinnable Component please refer to the Flex documentation: 
	www.adobe.com/go/actionscriptskinnablecomponents */
	
	import component.util.CustomToolTip;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import flashx.textLayout.elements.LinkElement;
	import flashx.textLayout.elements.SpanElement;
	import flashx.textLayout.events.FlowElementMouseEvent;
	
	import lib.CustomEvent;
	import lib.Gv;
	import lib.RemoteSingleManager;
	import lib.SLibrary;
	
	import mx.events.FlexEvent;
	import mx.events.IndexChangedEvent;
	import mx.managers.PopUpManager;
	
	import spark.components.Button;
	import spark.components.DropDownList;
	import spark.components.Label;
	import spark.components.SkinnableContainer;
	import spark.components.TextInput;
	import spark.components.supportClasses.SkinnableComponent;
	import spark.events.IndexChangeEvent;
	
	import valueObjects.BooleanAndDescriptionVO;
	import valueObjects.UserInformationVO;
	
	[Event(name="login", type="flash.events.Event")]
	[Event(name="logout", type="flash.events.Event")]
	[Event(name="join", type="flash.events.Event")]
	/* A component must identify the view states that its skin supports. 
	Use the [SkinState] metadata tag to define the view states in the component class. 
	[SkinState("normal")] */
	[SkinState("logout")]
	[SkinState("login")]
	
	public class Login extends SkinnableComponent
	{
		/* To declare a skin part on a component, you use the [SkinPart] metadata. 
		[SkinPart(required="true")] */
		[SkinPart(required="false")]public var user_id:TextInput;
		[SkinPart(required="false")]public var user_pw:TextInput;
		[SkinPart(required="false")]public var login:Button;
		[SkinPart(required="false")]public var join:Button;
		[SkinPart(required="false")]public var login_id:SpanElement;
		[SkinPart(required="false")]public var point:SpanElement;
		[SkinPart(required="false")]public var logoutBtn:Button;
		[SkinPart(required="false")]public var mType:DropDownList;
		
		
		
		private var _cstat:String = "logout";
		public function get cstat():String { return _cstat; }
		public function set cstat(value:String):void { _cstat = value; }
		
		private var customToolTip:CustomToolTip;
		
		public var bOnceAutoMoveSend:Boolean = false;
		
		
		public function Login()
		{
			//TODO: implement function
			super();
			login_check();
		}
		/**
		 * login check
		 * */
		public function login_check():void {
			
			RemoteSingleManager.getInstance.addEventListener("getUserInformation", login_check_resultHandler, false, 0, true);
			RemoteSingleManager.getInstance.callresponderToken 
				= RemoteSingleManager.getInstance.service.getUserInformation();
		}
		
		/* Implement the getCurrentSkinState() method to set the view state of the skin class. */
		override protected function getCurrentSkinState():String
		{
			return cstat;
		} 
		
		/* Implement the partAdded() method to attach event handlers to a skin part, 
		configure a skin part, or perform other actions when a skin part is added. */	
		override protected function partAdded(partName:String, instance:Object) : void
		{
			super.partAdded(partName, instance);
			if (instance == join) join.addEventListener(MouseEvent.CLICK, join_clickHandler);
			else if (instance == login) login.addEventListener(MouseEvent.CLICK, login_clickHandler);
			//else if (instance == user_id) user_id.addEventListener("tab", user_id_tabHandler);
			else if (instance == login_id) {
				
				login_id.text = Gv.user_id;
			}
			else if (instance == point) {
				
				setPoint();
			}
			else if (instance == user_pw) user_pw.addEventListener(FlexEvent.ENTER, login_clickHandler);
			else if (instance == logoutBtn) logoutBtn.addEventListener(MouseEvent.CLICK, logout_clickHandler);
			else if (instance == mType) mType.addEventListener(IndexChangeEvent.CHANGE, mType_changeHandler);
			
		}
		
		/* Implement the partRemoved() method to remove the even handlers added in partAdded() */
		override protected function partRemoved(partName:String, instance:Object) : void
		{
			super.partRemoved(partName, instance);
			if (instance == join) join.removeEventListener(MouseEvent.CLICK, join_clickHandler);
			else if (instance == login) login.removeEventListener(MouseEvent.CLICK, login_clickHandler);
			else if (instance == user_pw) user_pw.removeEventListener(FlexEvent.ENTER, login_clickHandler); 
			else if (instance == logoutBtn) logoutBtn.removeEventListener(MouseEvent.CLICK, logout_clickHandler);
			else if (instance == mType) mType.removeEventListener(IndexChangeEvent.CHANGE, mType_changeHandler);
		}
		
		private function user_id_tabHandler(event:Event):void {
			
			user_pw.setFocus();
		}
		
		/**
		 * join Handler
		 * */
		private function join_clickHandler(event:MouseEvent):void {
			
			this.dispatchEvent(new Event("join"));
		}
		
		/**
		 * login Handler
		 * */
		private function login_clickHandler(event:Event):void {
			
			if (user_id.text == "") SLibrary.alert("아이디를 입력 하세요");
			else if (user_pw.text == "") SLibrary.alert("비밀번호를 입력 하세요");
			else {
				RemoteSingleManager.getInstance.addEventListener("login", login_resultHandler, false, 0, true);
				RemoteSingleManager.getInstance.callresponderToken 
					= RemoteSingleManager.getInstance.service.login(user_id.text, user_pw.text);
			}
		}
		/**
		 * login resultHandler
		 * */
		private function login_resultHandler(event:CustomEvent):void {
			
			var bVO:BooleanAndDescriptionVO = event.result as BooleanAndDescriptionVO;
			if (bVO.bResult) {
				user_pw.text = "";
				login_check();
				
			} else {
				SLibrary.alert("로그인 실패");
			}
		}
		
		/**
		 * logout Handler
		 * */
		private function logout_clickHandler(event:Event):void {
			
			RemoteSingleManager.getInstance.addEventListener("logout_session", logout_resultHandler, false, 0, true);
			RemoteSingleManager.getInstance.callresponderToken 
				= RemoteSingleManager.getInstance.service.logout_session();
			
		}
		/**
		 * login resultHandler
		 * */
		private function logout_resultHandler(event:CustomEvent):void {
			
			var bVO:BooleanAndDescriptionVO = event.result as BooleanAndDescriptionVO;
			if (bVO.bResult) {
				Gv.bLogin = false;
				cstat = "logout";
				invalidateSkinState();
				dispatchEvent(new Event("logout"));
				
			} else {
				SLibrary.alert("로그아웃 실패");
			}
		}
		
		
		/**
		 * login check resultHandler
		 * */
		private function login_check_resultHandler(event:CustomEvent):void {
			
			var uvo:UserInformationVO = event.result as UserInformationVO;
			if (uvo != null) {
				cstat = "login";
				Gv.bLogin = true;
				Gv.user_id = uvo.user_id;
				Gv.point = uint( uvo.point );
				
				if (bOnceAutoMoveSend) {
					bOnceAutoMoveSend = false;
					callLater(moveSend);
				}
				dispatchEvent(new Event("login"));
			} else {
				Gv.bLogin = false;
				cstat = "logout";
				dispatchEvent(new Event("logout"));
			}
			invalidateSkinState();
			
			
				
		}
		
		
		private function tooltip_overHandler(event:FlowElementMouseEvent):void {

			if(!customToolTip){
				customToolTip = new CustomToolTip();
				customToolTip.x = parentApplication.mouseX - customToolTip.width/2;
				customToolTip.y = parentApplication.mouseY - 40;
				PopUpManager.addPopUp(customToolTip, this, false);
			}
			customToolTip.text =  LinkElement(event.flowElement).href;
		}
		private function tooltip_outHandler(event:FlowElementMouseEvent):void {

			PopUpManager.removePopUp(customToolTip);
			customToolTip = null;
		}
		
		
		private function login_id_clickHandler(event:FlowElementMouseEvent):void {
			event.stopImmediatePropagation();
			event.preventDefault();
			trace("click!!!!");
			
		}
		
		private function moveSend():void {
			
			if (SkinnableContainer(parentApplication).currentState != "send") {
				parentApplication.menus.clickStat ="send";
				//SkinnableContainer(parentApplication).currentState ="send";
			}
		}
		
		private function moveHome():void {
			
			if (SkinnableContainer(parentApplication).currentState != "home") {
				parentApplication.menus.clickStat ="home";
				//SkinnableContainer(parentApplication).currentState ="send";
			}
		}
		
		private function mType_changeHandler(event:IndexChangeEvent):void {
			setPoint();
		}
		
		private function setPoint():void {
			var p:SpanElement = new SpanElement();
			var unit:int = 1;
			
			var t:String = "SMS";
			if (mType.selectedIndex >= 0)
				t = mType.selectedItem.label;
			
			if (t == "LMS") unit = 3;
			else if (t == "MMS") unit = 15;
			else unit = 1;
			
			
			point.text =  SLibrary.addComma( String(Math.floor( Gv.point/unit ) ) );
		}
		
	}
}