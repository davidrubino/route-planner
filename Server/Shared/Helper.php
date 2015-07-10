<?php
	class Helper{
		
		/**
		Encode un tableau d'objets encodables en faisant appel à la fonction encode défini dans chacun d'entre eux
		**/
		public static function encodeArrayOfObject($array)
		{
			$json='[';
			$k=0;
			foreach($array as $i=>$val)
			{
				if(is_int($i)){
					if($k++==0)
						$json.=$val->encode();
					else
						$json.=','.$val->encode();
				}
				else{
					if($k++==0)
						$json.='"'.$val.'"';
					else
						$json.='"'.$val.'"';
				}
			}
			return $json.']';
		}	
	}
?>