<?php
    include "config.php";
    include "utils.php";

    $dbConn =  connect($db);

    ini_set('display_errors', 1);
    ini_set('display_startup_errors', 1);
    error_reporting(E_ALL);
    include $_SERVER['DOCUMENT_ROOT']."/includes/functions/password_funcs.php";
    include $_SERVER['DOCUMENT_ROOT']."/includes/functions/database.php";
    include $_SERVER['DOCUMENT_ROOT']."/includes/functions/general.php";
    include $_SERVER['DOCUMENT_ROOT']."/includes/classes/passwordhash.php";
    if ($_SERVER['REQUEST_METHOD'] == 'GET' && isset($_GET['tokenFFL'])&& $_GET['tokenFFL'] == $tokenFFL)
    {
        
	if (isset($_GET['email']) && isset($_GET['psw']))
        {
		

 		$email_address = tep_db_prepare_input($_GET['email']);
    		$password = tep_db_prepare_input($_GET['psw']);

		// Check if email exists
    		$check_customer_query = tep_db_query("select customers_id, customers_firstname, customers_password, customers_email_address, customers_default_address_id from customers where 			customers_email_address = '" . tep_db_input($email_address) . "'");
    		if (!tep_db_num_rows($check_customer_query)) {
      			$error = true;
    		} else {
			$check_customer = tep_db_fetch_array($check_customer_query);
			// Check that password is good
			if (!tep_validate_password($password, $check_customer['customers_password'])) {
			        $error = true;
			} else {
				echo ($check_customer);
		        }
		}

/*
		$password = tep_encrypt_password($_GET['psw']);
		$consulta = "SELECT * FROM customers where customers_email_address = '".$_GET['email']."' and customers_password = '".$password."'";
		echo ($consulta);
		$sql = $dbConn->prepare($consulta);
	      	$sql->execute();
		$sql->setFetchMode(PDO::FETCH_ASSOC);
		header("HTTP/1.1 200 OK");
		echo json_encode( $sql->fetchAll(),JSON_UNESCAPED_UNICODE);*/
        
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
