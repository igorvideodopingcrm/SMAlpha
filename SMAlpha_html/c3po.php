<?php
include_once "./init.php";
if(isset($_GET["meteo"])){
	$regex="/^(([0-9]{2}\/){2}([0-9]){4}_(([0-9]){2}(:)?){3},(-)?([0-9]){0,2},([a-z](_)?)+;){1,7}$/";
	if(!preg_match($regex,$_GET["meteo"])){
		echo json_encode(array(array("meteo"=> "bad format")));//afin que tous les messages envoyés à l'agent aient le même format
		exit();
	}
	$id=0;
	$equipements=get_equipements();
	$exist=false;
	foreach($equipements as $item){
		if($item["nom"]=="sys_chauffage"){
			$exist=true;
			$id=$item["id"];
			break;
		}
	}
	$meteo_file=fopen("./res/meteo.txt","r");
	$meteo = array();
	while($days = fgets($meteo_file)){
		$meteo=explode(";",$days,-1);
	}
	fclose($meteo_file);
	for($i=0;$i<count($meteo);$i++){
		$meteo[$i]=explode(",",$meteo[$i]);
		$meteo[$i][0]=strstr($meteo[$i][0]," ",true);
	}
	$temperature=$meteo[0][1];
	$prefs=get_pref();
	$conso=0;
	if(isset($prefs["température"])){
		$conso=$prefs["température"]-$temperature;
		if($conso<0)
			$conso=0;
	}
	if($exist){
		update_equipement($id,"sys_chauffage",$conso,0,24,24);
	}else{
		create_equipement("sys_chauffage",$conso,0,24,24);
	}
	$f=fopen("./res/meteo.txt","w");
	fwrite($f,str_replace("_"," ",$_GET["meteo"]));
	fclose($f);
	echo json_encode(array(array("meteo"=> "ok")));
	exit();
}else if(isset($_GET["equipements"])){
	echo json_encode(get_equipements());
	exit();
}else if(isset($_GET["planning"])){
	$f=fopen("./res/planning.txt","w");
	fwrite($f,$_GET["planning"]);
	fclose($f);
	echo json_encode(array(array("planning"=> "recu")));
}
?>