<?php
    include "config.php";
    include "utils.php";

    $dbConn =  connect($db);
    include $_SERVER['DOCUMENT_ROOT']."\includes\functions\password_funcs.php";
    if ($_SERVER['REQUEST_METHOD'] == 'GET' && isset($_GET['tokenFFL'])&& $_GET['tokenFFL'] == $tokenFFL)
    {
        
	if (isset($_GET['email']) && isset($_GET['psw']))
        {

		echo json_encode("INCLUYO BIEN");
		$password = tep_encrypt_password($_GET['psw']);	
		echo json_encode("ENCRIPTO BIEN:".$password);
		$consulta = "SELECT * FROM customers where customers_email_address = '".$_GET['email']."' and customers_password = '".$password."'";
		echo json_encode($consulta);
		$sql = $dbConn->prepare($consulta);
	      	$sql->execute();
		$sql->setFetchMode(PDO::FETCH_ASSOC);
		header("HTTP/1.1 200 OK");
		echo json_encode( $sql->fetchAll(),JSON_UNESCAPED_UNICODE);
        
	}else{
		$sql = $dbConn->prepare("SELECT * FROM customers where customers_lastname like '%Grassano%'");
	      	$sql->execute();
		$sql->setFetchMode(PDO::FETCH_ASSOC);
		header("HTTP/1.1 200 OK");
		echo json_encode( $sql->fetchAll(),JSON_UNESCAPED_UNICODE);
	}
	exit();
		 
    }
   
    
    //En caso de que ninguna de las opciones anteriores se haya ejecutado
    header("HTTP/1.1 400 Bad Request");
    ?>
