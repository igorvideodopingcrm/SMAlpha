<?php /* Smarty version Smarty-3.1.21-dev, created on 2015-04-21 12:07:54
         compiled from ".\view\header.html" */ ?>
<?php /*%%SmartyHeaderCode:224875523a9af3c7797-84357862%%*/if(!defined('SMARTY_DIR')) exit('no direct access allowed');
$_valid = $_smarty_tpl->decodeProperties(array (
  'file_dependency' => 
  array (
    '1e0130bfb03eff297c651d2ea2c770c56f0d3e00' => 
    array (
      0 => '.\\view\\header.html',
      1 => 1429610726,
      2 => 'file',
    ),
  ),
  'nocache_hash' => '224875523a9af3c7797-84357862',
  'function' => 
  array (
  ),
  'version' => 'Smarty-3.1.21-dev',
  'unifunc' => 'content_5523a9af402121_35431544',
  'variables' => 
  array (
    'page' => 0,
  ),
  'has_nocache_code' => false,
),false); /*/%%SmartyHeaderCode%%*/?>
<?php if ($_valid && !is_callable('content_5523a9af402121_35431544')) {function content_5523a9af402121_35431544($_smarty_tpl) {?><!doctype html>
<html lang="fr">
<head>
  <meta charset="utf-8">
  <title>SMAlpha</title>
  <link href="./css/bootstrap.min.css" rel="stylesheet">
  <?php echo '<script'; ?>
 src="./js/bootstrap.min.js"><?php echo '</script'; ?>
>
  <?php echo '<script'; ?>
 src="./js/jquery-2.1.3.min.js"><?php echo '</script'; ?>
>
</head>
<body>
<div class="col-md-6 col-md-offset-3">
	<div>
		<ul class="nav nav-tabs">
		  <li role="presentation" <?php if ($_smarty_tpl->tpl_vars['page']->value=="index") {?>class="active"<?php }?>><a href="./index.php">Acceuil</a></li>
		  <li role="presentation" <?php if ($_smarty_tpl->tpl_vars['page']->value=="meteo") {?>class="active"<?php }?>><a href="./meteo.php">Météo</a></li>
		  <li role="presentation" <?php if ($_smarty_tpl->tpl_vars['page']->value=="prod_conso") {?>class="active"<?php }?>><a href="./prod_conso.php">prod_conso</a></li>
		  <li role="presentation" <?php if ($_smarty_tpl->tpl_vars['page']->value=="pref") {?>class="active"<?php }?>><a href="./pref.php">Preferences</a></li>
		</ul>
	</div>
</div><?php }} ?>
