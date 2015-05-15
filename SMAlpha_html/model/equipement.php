<?php
function create_equipement($nom,$conso,$debut,$fin,$duree){
	if($duree>24)
		$duree=24;
	else if($duree<0)
		$duree=0;
	if($debut+$duree>24)
		$debut=24-$duree;
	if($debut<0)
		$debut=0;
	if($fin<$debut+$duree){
		if($debut+$duree>24)
			$fin=24;
			$debut=$fin-$duree;
		else
			$fin=$debut+$duree;
	}
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