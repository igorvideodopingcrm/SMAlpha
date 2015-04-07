<?php
/**
 * Example Application
 *
 * @package Example-application
 */

require './libs/Smarty.class.php';

$smarty = new Smarty;

$smarty->display('./view/header.html');
$smarty->display('./view/index.html');
$smarty->display('./view/footer.html');
