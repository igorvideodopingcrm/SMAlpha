<?php


include_once "./init.php";

$tpl = new Smarty;
$tpl->assign("page","pref");

$tpl->display('./view/header.html');
$tpl->display('./view/pref.html');
$tpl->display('./view/footer.html');
?>