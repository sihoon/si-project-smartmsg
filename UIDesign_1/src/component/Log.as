package component
{
	/* For guidance on writing an ActionScript Skinnable Component please refer to the Flex documentation: 
	www.adobe.com/go/actionscriptskinnablecomponents */
	
	import mx.controls.DateField;
	
	import spark.components.Button;
	import spark.components.List;
	import spark.components.supportClasses.SkinnableComponent;
	
	
	/* A component must identify the view states that its skin supports. 
	Use the [SkinState] metadata tag to define the view states in the component class. 
	[SkinState("normal")] */
	
	public class Log extends SkinnableComponent
	{
		/* To declare a skin part on a component, you use the [SkinPart] metadata. 
		[SkinPart(required="true")] */
		
		[SkinPart(required="false")]public var dateField1:DateField;
		[SkinPart(required="false")]public var dateField2:DateField;
		[SkinPart(required="false")]public var groupList:List;
		[SkinPart(required="false")]public var detailList:List;
		
		[SkinPart(required="false")]public var reservationBtn:Button;
		
		
		public function Log()
		{
			//TODO: implement function
			super();
		}
		
		/* Implement the getCurrentSkinState() method to set the view state of the skin class. */
		override protected function getCurrentSkinState():String
		{
			return super.getCurrentSkinState();
		} 
		
		/* Implement the partAdded() method to attach event handlers to a skin part, 
		configure a skin part, or perform other actions when a skin part is added. */	
		override protected function partAdded(partName:String, instance:Object) : void
		{
			super.partAdded(partName, instance);
		}
		
		/* Implement the partRemoved() method to remove the even handlers added in partAdded() */
		override protected function partRemoved(partName:String, instance:Object) : void
		{
			super.partRemoved(partName, instance);
		}
		
	}
}