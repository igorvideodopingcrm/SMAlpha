<?php
function create_equipement($nom,$conso,$debut,$fin,$duree){
	if($fin<$debut+$duree)
		$fin=$debut+$duree;
	$query="INSERT INTO `equipement`(`nom`, `conso`, `debut_min`, `fin_max`, `durée`) VALUES ('".$nom."','".$conso."','".$debut."','".$fin."','".$duree."')";
	query($query);
}

function get_equipement(){
	
}

function update_equipement(){
	
}

function delete_equipement(){
	
}

?>