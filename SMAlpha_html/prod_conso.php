<?php


include_once "./init.php";

$tpl = new Smarty;
$tpl->assign("page","prod_conso");

$tpl->display('./view/header.html');
$tpl->display('./view/prod_conso.html');
$tpl->display('./view/footer.html');
?>