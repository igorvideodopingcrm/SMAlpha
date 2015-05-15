<?php
function create_equipement($nom,$conso,$debut,$fin,$duree){
	if($fin<$debut+$duree)
		$fin=$debut+$duree;
	$query="INSERT INTO `equipement`(`nom`, `conso`, `debut_min`, `fin_max`, `duree`) VALUES ('".$nom."','".$conso."','".$debut."','".$fin."','".$duree."')";
// var_dump($query);
	query($query);
}

function get_equipements(){
	$query="SELECT * FROM `equipement`";
	$data=query($query);
	return $data;
}

function update_equipement($id,$nom,$conso,$debut,$fin,$duree){
	$query="UPDATE `equipement` SET `nom`='".$nom."',`conso`='".$conso."',`debut_min`='".$debut."',`fin_max`='".$fin."',`duree`='".$duree."' WHERE id=".$id;
// var_dump($query);	
	query($query);
}

function delete_equipement($id){
	$query="DELETE FROM `equipement` WHERE id=".$id;
	query($query);
}

?>