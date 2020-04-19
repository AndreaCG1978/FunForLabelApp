<?php
    include "config.php";
    include "utils.php";

    $dbConn =  connect($db);

    ini_set('display_errors', 1);
    ini_set('display_startup_errors', 1);
    error_reporting(E_ALL);
    include $_SERVER['DOCUMENT_ROOT']."/includes/functions/password_funcs.php";
	include $_SERVER['DOCUMENT_ROOT']."/includes/functions/database.php";
	include $_SERVER['DOCUMENT_ROOT']."/includes/database_tables.php";
    include $_SERVER['DOCUMENT_ROOT']."/includes/functions/general.php";
    include $_SERVER['DOCUMENT_ROOT']."/includes/classes/passwordhash.php";
    if ($_SERVER['REQUEST_METHOD'] == 'GET' && isset($_GET['tokenFFL'])&& $_GET['tokenFFL'] == $tokenFFL)
    {
        
		if (isset($_GET['email']) && isset($_GET['psw']))
			{
			$email_address = tep_db_prepare_input($_GET['email']);
			$pswEntry = tep_db_prepare_input($_GET['psw']);
			$consulta = "SELECT * FROM ". TABLE_CUSTOMERS . " where customers_email_address = '".$email_address."'";
			$sql = $dbConn->prepare($consulta);
			$sql->execute();
			$resultado = $sql->fetch(PDO::FETCH_ASSOC);
			if($resultado != null){
				$psw = $resultado['customers_password'];
				$validacion = tep_validate_password($pswEntry, $psw);
				if ($validacion != null && $validacion==true) {
					header("HTTP/1.1 200 OK");
					echo json_encode([$resultado],JSON_UNESCAPED_UNICODE);
				}else{
					echo json_encode([],JSON_UNESCAPED_UNICODE);
				}
			}else{
				echo json_encode([],JSON_UNESCAPED_UNICODE);
			}
			
		}else{
			echo json_encode([],JSON_UNESCAPED_UNICODE);
		}
		exit();
		 
    }
   
    
    //En caso de que ninguna de las opciones anteriores se haya ejecutado
    header("HTTP/1.1 400 Bad Request");
    ?>
