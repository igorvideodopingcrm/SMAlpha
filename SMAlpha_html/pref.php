<?php


include_once "./init.php";
$prefs=get_pref();

if(isset($_POST["update"])){
	foreach($prefs as $key=>$value){
		$prefs[$key]=$_POST[$key];
	}
	update_pref($prefs);
}
$tpl = new Smarty;
$tpl->assign("page","pref");
$tpl->assign("prefs",$prefs);
$tpl->display('./view/header.html');
$tpl->display('./view/pref.html');
$tpl->display('./view/footer.html');
?>