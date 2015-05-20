<?php
function get_pref(){

	$pref_file=fopen("./res/pref.txt","r");
	$prefs = array();
	while($pref = fgets($pref_file)){
		$prefs[strstr($pref,"=",true)]=substr(strstr($pref,"="),1);
	}
	return $prefs;
}
function update_pref($prefs){
	$f=fopen("./res/pref.txt","w");
	foreach($prefs as $key=>$value){
		fwrite($f,$key."=".$value."\n");
	}
	fclose($f);
}
?>