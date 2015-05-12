<?php
include_once "./init.php";

$tpl = new Smarty;

$meteo_file=fopen("./res/meteo","r");
$meteo = array();
while($day = fgets($meteo_file)){
	$meteo[]=explode(":",$day);
}

$tpl->assign("page","meteo");
$tpl->assign("meteo",$meteo);
$tpl->display('./view/header.html');
$tpl->display('./view/meteo.html');
$tpl->display('./view/footer.html');
?>