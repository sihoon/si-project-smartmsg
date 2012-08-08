package lib
{
	import mx.collections.ArrayCollection;

	public class Gv
	{
		public static const SMS_BYTE:uint = 90;
		public static const LMS_BYTE:uint = 2000;
		public static const MMS_BYTE:uint = 2000;
		
		public static const spcData:Array = new Array(
			"＃","＆","＊","＠","※","☆","★","○","●","◎","◇","◆","§",
			"□","■","△","▲","▽","▼","→","←","↑","↓","↔","〓","◁",
			"◀","▷","▶","♤","♠","♡","♥","♧","♣","∠","◈","▣","◐",
			"◑","▒","▤","▥","▨","▧","▦","▩","♨","☏","☎","☜","☞",
			"¶","†","‡","↕","↗","↙","↖","↘","♭","♩","♪","♬","㉿",
			"㈜","№","㏇","™","㏂","㏘","℡","®","ª","º","！","˝","＇",
			"．","／","：","；","？","＾","＿","｀","｜","￣","、","。","·",
			"」","『","』","【","】","＋","－","√","＝","∽","±","×","÷",
			"≠","≤","≥","∞","♂","♀","∝","∵","∫","∬","∈","∋","⊆",
			"⊇","⊂","⊃","∪","∩","∧","∨","￢","⇒","⇔","∀","∃","∮",
			"∑","∏","＋","－","＜","＝","＞","±","×","÷","≠","≤","≥",
			"∞","∴","♂","♀","∠","⊥","⌒","∂","∇","≡","≒","≡","≒",
			"‥","¨","…","〃","⊥","⌒","∥","＼","∴","´","～","ˇ","˘",
			"˚","˙","¸","˛","¡","¿","∂","，","＂","（","）","［","］",
			"｛","｝","‘","’","“","”","〔","〕","〈","〉","《","》","「",
			"㉠","㉡","㉢","㉣","㉤","㉥","㉦","㉧","㉨","㉩","㉪","㉫","㉬",
			"㉭","㉮","㉯","㉰","㉱","㉲","㉳","㉴","㉵","㉶","㉷","㉸","㉹",
			"㉺","㉻","ⓐ","ⓑ","ⓒ","ⓓ","ⓔ","ⓕ","ⓖ","ⓗ","ⓘ","ⓙ","ⓚ",
			"ⓛ","ⓜ","ⓝ","ⓞ","ⓟ","ⓠ","ⓡ","ⓢ","ⓣ","ⓤ","ⓥ","ⓦ","ⓧ",
			"ⓨ","ⓩ","①","②","③","④","⑤","⑥","⑦","⑧","⑨","⑩","½"
		);
		
		public static var bLogin:Boolean = false;
		public static var user_id:String = "";
		public static var point:uint = 0;
		
		public static var addressGroupList:ArrayCollection = new ArrayCollection;
		
		public function Gv(){}
		
	}
}