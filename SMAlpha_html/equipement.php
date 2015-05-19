<?php
include_once "./init.php";
if(isset($_POST["create"])){
	create_equipement($_POST["nom"],$_POST["conso"],$_POST["debut"],$_POST["fin"],$_POST["duree"]);
}else if(isset($_POST["update"])){
	update_equipement($_POST["id"],$_POST["nom"],$_POST["conso"],$_POST["debut"],$_POST["fin"],$_POST["duree"]);
}else if(isset($_POST["delete"])){
	delete_equipement($_POST["id"]);
}
//a penser, une intérogation depuis le java pour recupérer la liste des equipements
$tpl = new Smarty;
$equipements=get_equipements();
$tpl->assign("page","equipement");
$tpl->assign("equipements",$equipements);
$tpl->display('./view/header.html');
$tpl->display('./view/equipement.html');
$tpl->display('./view/footer.html');
?>