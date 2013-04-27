﻿package component.home
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.net.URLLoader;
	
	import mx.containers.Canvas;
	import mx.core.UIComponent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import spark.components.Group;
	
	[Event(name="nodeClick", type="component.home.NodeClickEvent")]
	public class TagCloud extends Group	{
		
		private var radius:Number;
		private var mcList:Array;
		private var dtr:Number;
		private var d:Number;
		private var sa:Number;
		private var ca:Number;
		private var sb:Number;
		private var cb:Number;
		private var sc:Number;
		private var cc:Number;
		private var originx:Number;
		private var originy:Number;
		private var tcolor:Number;
		private var hicolor:Number;
		private var tcolor2:Number;
		private var tspeed:Number;
		private var distr:Boolean;
		private var lasta:Number;
		private var lastb:Number;
		private var holder:UIComponent;
		private var active:Boolean;
		private var myXML:XML;
		private var myLoader:URLLoader;
		private var widthTag:Number;
		private var heightTag:Number;
		
		private var _url:String ;
		
		public function set url(str:String):void { this._url = str; goTagGo(); }
		public function get url():String { return this._url }
		
		//this number changes if it's elliptical or spherical
		[Bindable]
		public var howElliptical:Number=1;
		
		public function TagCloud(){		
			// goTagGo(); url 설정시로 옮김
		}
		public function goTagGo():void {
			tspeed = 0.5;
			distr = true; //distribute the words or not
			// load XML file
			var service : HTTPService = new HTTPService();
			//the url for the tag xml...
	      	service.url = this.url; 
	       	service.addEventListener(ResultEvent.RESULT, xmlLoaded);
	      	service.send();
		}
		private function xmlLoaded(e:Event):void {
			trace("xml loaded");
			
			//define sizes
			widthTag = this.width - this.width/10;
			heightTag = this.height - this.height/10;

			//load the xml info using E4X
			var result:* = e.target.lastResult;
			if(result.tags){				
				// set some vars
				radius = 150;
				dtr = Math.PI/180;
				d=300;
				sineCosine( 0,0,0 );
				mcList = [];
				active = false;
				lasta = 1;
				lastb = 1;
				// create holder mc, center it		
				holder = new UIComponent();
				addElement(holder);
				resizeHolder();
				// loop though them to find the smallest and largest 'tags'
				
					for each (var tag:Object in result.tags.tag )  {
				
						var col:Number = 0x000000;
						var hicol:Number = 0x800000;
						
						var mc:Tag = new Tag( tag, col, hicol );
						mc.addEventListener("nodeClick", nodeClickHandler);

						holder.addChild(mc);

						holder.x = (this.widthTag/2) - (holder.width/2);
						holder.y = (this.heightTag/2) - (holder.height/2);
						// store reference
						mcList.push( mc );
					}
		
					// distribute the tags on the sphere
					positionAll();
					// add event listeners
					this.addEventListener(Event.ENTER_FRAME, updateTags);
					parent.addEventListener(MouseEvent.ROLL_OUT, mouseExitHandler);
					parent.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
					this.addEventListener(Event.RESIZE, resizeHandler);
			}
		}
		
		private function nodeClickHandler( e:NodeClickEvent ):void {
			
			dispatchEvent(new NodeClickEvent(NodeClickEvent.NODE_CLICK,e.val));
			//Set the actions you want for click here
			//Alert.show("How dare you? You clicked me!"+e.currentTarget._node.name);
		}

		private function updateTags( e:Event ):void {
			var a:Number;
			var b:Number;
			if( active ){
				a = (-Math.min( Math.max( holder.mouseY, -250 ), 250 ) / 150 ) * tspeed;
				b = (Math.min( Math.max( holder.mouseX, -250 ), 250 ) /150 ) * tspeed;
			} else {
				a = lasta * 0.98;
				b = lastb * 0.98;
			}
			lasta = a;
			lastb = b;
			// if a and b under threshold, skip motion calculations to free up the processor
			if( Math.abs(a) > 0.01 || Math.abs(b) > 0.01 ){
				var c:Number = 0;
				sineCosine( a, b, c );
				// bewegen van de punten
				for( var j:Number=0; j<mcList.length; j++ ) {
					// multiply positions by a x-rotation matrix
					var rx1:Number = mcList[j].cx;
					var ry1:Number = mcList[j].cy * ca + mcList[j].cz * -sa;
					var rz1:Number = mcList[j].cy * sa + mcList[j].cz * ca;
					// multiply new positions by a y-rotation matrix
					var rx2:Number = rx1 * cb + rz1 * sb;
					var ry2:Number = ry1;
					var rz2:Number = rx1 * -sb + rz1 * cb;
					// multiply new positions by a z-rotation matrix
					var rx3:Number = rx2 * cc + ry2 * -sc;
					var ry3:Number = rx2 * sc + ry2 * cc;
					var rz3:Number = rz2;
					// set arrays to new positions
					mcList[j].cx = rx3;
					mcList[j].cy = ry3;
					mcList[j].cz = rz3;
					// add perspective
					var per:Number = d / (d+rz3);
					// setmc position, scale, alpha
					mcList[j].x = (howElliptical * rx3 * per) - (howElliptical*2); //original is mcList[j].x = rx3 * per;
					mcList[j].y = ry3 * per;
					mcList[j].scaleX = mcList[j].scaleY =  per;
					mcList[j].alpha = per/2;
				}
				depthSort();
			}
		}
		
		private function depthSort():void {
			mcList.sortOn( "cz", Array.DESCENDING | Array.NUMERIC );
			var current:Number = 0;
			for( var i:Number=0; i<mcList.length; i++ ){
				holder.setChildIndex( mcList[i], i );
				if( mcList[i].active == true ){
					current = i;
				}
			}
			holder.setChildIndex( mcList[current], mcList.length-1 );
		}
		
		/* See http://blog.massivecube.com/?p=9 */
		private function positionAll():void {		
			var phi:Number = 0;
			var theta:Number = 0;
			var max:Number = mcList.length;
			// mix up the list so not all a' live on the north pole
			mcList.sort( function():Number{ return Math.random()<0.5 ? 1 : -1; } );
			// distibute
			for( var i:Number=1; i<max+1; i++){
				if( distr ){
					phi = Math.acos(-1+(2*i-1)/max);
					theta = Math.sqrt(max*Math.PI)*phi;
				}else{
					phi = Math.random()*(Math.PI);
					theta = Math.random()*(2*Math.PI);
				}
				// Coordinate conversion
				mcList[i-1].cx = radius * Math.cos(theta)*Math.sin(phi);
				mcList[i-1].cy = radius * Math.sin(theta)*Math.sin(phi);
				mcList[i-1].cz = radius * Math.cos(phi);
			}
		}
		
	
		private function mouseExitHandler( e:Event ):void {
			 active = false; 
			// trace("mouseExitHandler, active: " + active);
		}
		private function mouseMoveHandler( e:MouseEvent ):void {
			active = true;
			//trace("mouseMoveHandler, active: " + active);
		}
		private function resizeHandler( e:Event ):void { resizeHolder(); }
		
		private function resizeHolder():void {
			
			holder.x = (this.width/2) - (holder.width/2);
			holder.y = (this.height/2) - (holder.height/2);
			var scale:Number;
			if( width > height ){
				scale = (height/500);
			} else {
				scale = (width/500);
			}
			holder.scaleX = holder.scaleY = scale;
		}
		
		private function sineCosine( a:Number, b:Number, c:Number ):void {
			sa = Math.sin(a * dtr);
			ca = Math.cos(a * dtr);
			sb = Math.sin(b * dtr);
			cb = Math.cos(b * dtr);
			sc = Math.sin(c * dtr);
			cc = Math.cos(c * dtr);
		}
		

		
	}

}