<?php


include_once "./init.php";

$tpl = new Smarty;
$tpl->assign("page","index");

$tpl->display('./view/header.html');
$tpl->display('./view/index.html');
$tpl->display('./view/footer.html');
?>