<?php
include_once "./init.php";

$tpl = new Smarty;

$meteo_file=fopen("./res/meteo.txt","r");
$meteo = array();
while($days = fgets($meteo_file)){
	$meteo=explode(";",$days,-1);
}
for($i=0;$i<count($meteo);$i++){
	$meteo[$i]=explode(",",$meteo[$i]);
	$meteo[$i][0]=strstr($meteo[$i][0]," ",true);
} 
$tpl->assign("page","meteo");
$tpl->assign("meteo",$meteo);
$tpl->display('./view/header.html');
$tpl->display('./view/meteo.html');
$tpl->display('./view/footer.html');
?>