<?php


include_once "./init.php";
$prefs=get_pref();

if(isset($_POST["update"])){
	foreach($prefs as $key=>$value){
		$prefs[$key]=$_POST[$key];
	}
	update_pref($prefs);
	if(isset($prefs["température"])){
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
		$conso=$prefs["température"]-$temperature;
		if($conso<0)
			$conso=0;
		if($exist){
			update_equipement($id,"sys_chauffage",$conso,0,24,24);
		}else{
			create_equipement("sys_chauffage",$conso,0,24,24);
		}
	}

}

$tpl = new Smarty;
$tpl->assign("page","pref");
$tpl->assign("prefs",$prefs);
$tpl->display('./view/header.html');
$tpl->display('./view/pref.html');
$tpl->display('./view/footer.html');
?>