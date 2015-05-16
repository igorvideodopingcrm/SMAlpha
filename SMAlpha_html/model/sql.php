<?php 
function connect() { 
try
	{
		$host="localhost";
		$name="c3po";
		$username="root";
		$password="password";
	$db = new PDO('mysql:host='.$host.';dbname='.$name.'', $username,$password);
	$db -> exec('SET CHARACTER SET utf8');
	return $db;
	}
catch (Exception $e)
	{ die('Erreur cnxbase : '.$e -> getMessage()); }
}

function query($query) {
	$db = connect();
	$req = $db -> prepare($query);
	try 
		{ $req -> execute(); }
	catch (Exception $e)
		{ die('Erreur sql : '.$e -> getMessage()); }

	
	$data = array();

	while ($row = $req -> fetch(PDO::FETCH_ASSOC)) {
			$data[] = $row;
	}
	
	$req -> closeCursor();	
	return $data;
}
?>