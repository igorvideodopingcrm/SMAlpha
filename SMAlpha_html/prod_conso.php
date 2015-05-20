<?php
include_once "./init.php";

$conso=array();
$equipements_string=array();
$planning_file=fopen("./res/planning.txt","r");
while($days = fgets($planning_file)){
	$equipements_string=explode(";",$days,-1);
}
fclose($planning_file);
for($i=0;$i<count($equipements_string);$i++){
	$equipements_string[$i]=explode(",",$equipements_string[$i]);
	$equipements_string[$i][0]=strstr($equipements_string[$i][0]," ",true);
}
$equipements=array();
for($i=0;$i<count($equipements_string);$i++){
	foreach($equipements_string[$i] as $field){
		$equipements[$i][strstr($field,"=",true)]=substr(strstr($field,"="),1);
	}
}
for($i=0;$i<24;$i++){
	$conso[$i]=0;
	foreach($equipements as $item){
		if($i>=$item["indice"] && $i < ($item["indice"]+$item["duree"]))
			$conso[$i]+=$item["conso"];
	}
}
$max=0;
$moy=0;
for($i=0;$i<24;$i++){
	if($conso[$i]>$max){
		$max=$conso[$i];
	}
	$moy+=$conso[$i];
}
$moy/=24;
$tpl = new Smarty;
$tpl->assign("page","prod_conso");
$tpl->assign("conso",$conso);
$tpl->assign("max",$max);
$tpl->assign("moy",$moy);
$tpl->display('./view/header.html');
$tpl->display('./view/prod_conso.html');
$tpl->display('./view/footer.html');
?>