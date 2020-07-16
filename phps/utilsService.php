<?php
    include "config.php";
    include "utils.php";

    $dbConn =  connect($db);

    ini_set('display_errors', 1);
    ini_set('display_startup_errors', 1);
    error_reporting(E_ALL);
    
	include $_SERVER['DOCUMENT_ROOT']."/includes/functions/database.php";
	include $_SERVER['DOCUMENT_ROOT']."/includes/database_tables.php";
    include $_SERVER['DOCUMENT_ROOT']."/includes/functions/general.php";
	
	
    if ($_SERVER['REQUEST_METHOD'] == 'GET' && isset($_GET['tokenFFL'])&& $_GET['tokenFFL'] == $tokenFFL)
    {
        
		if (isset($_GET['getAllShippingMethod'])){

			$consulta = "SELECT * FROM shipping_method";
			$sql = $dbConn->prepare($consulta);
			$sql->execute();
			$resultado = $sql->fetchAll(PDO::FETCH_ASSOC);
			if($resultado != null){
				echo json_encode($resultado,JSON_UNESCAPED_UNICODE);
			}else{
				echo json_encode([],JSON_UNESCAPED_UNICODE);
			}
			
		}else if(isset($_GET['getAllPaymentMethod'])){

			$consulta = "SELECT * FROM payment_method";
			$sql = $dbConn->prepare($consulta);
			$sql->execute();
			$resultado = $sql->fetchAll(PDO::FETCH_ASSOC);
			if($resultado != null){
				echo json_encode($resultado,JSON_UNESCAPED_UNICODE);
			}else{
				echo json_encode([],JSON_UNESCAPED_UNICODE);
			}
			
		}else
		{
			echo json_encode([],JSON_UNESCAPED_UNICODE);
		}
		exit();
		 
	}

    
    //En caso de que ninguna de las opciones anteriores se haya ejecutado
    header("HTTP/1.1 400 Bad Request");
    ?>
