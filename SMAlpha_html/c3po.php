<?php
include_once "./init.php";
if(isset($_GET["meteo"])){
	$regex="/^(([0-9]{2}\/){2}([0-9]){4}_(([0-9]){2}(:)?){3},(-)?([0-9]){0,2},([a-z](_)?)+;){1,7}$/";
	if(!preg_match($regex,$_GET["meteo"])){
		echo json_encode(array("meteo"=> "bad format"));
		exit();
	}
	$f=fopen("./res/meteo.txt","w");
	fwrite($f,str_replace("_"," ",$_GET["meteo"]));
	fclose($f);
	echo json_encode(array("meteo"=> "ok"));
	exit();
}else if(isset($_GET["equipements"])){
	echo json_encode(get_equipements());
	exit();
}else if(isset($_GET["planning"])){
	$f=fopen("./res/planning.txt","w");
	fwrite($f,str_replace("_"," ",$_GET["planning"]));
	fclose($f);
	echo json_encode(array("planning"=> "recu"));
}
?>