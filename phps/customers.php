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
			$consulta = "SELECT customers_id, customers_gender, customers_firstname, customers_lastname, customers_email_address, customers_default_address_id, customers_telephone, customers_fax, customers_password, customers_newsletter FROM ". TABLE_CUSTOMERS . " where customers_email_address = '".$email_address."'";
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
			
		}else if(isset($_GET['email']) && !isset($_GET['psw'])){
			$email_address = tep_db_prepare_input($_GET['email']);
			$consulta = "SELECT customers_id, customers_gender, customers_firstname, customers_lastname, customers_email_address, customers_default_address_id, customers_telephone, customers_fax, customers_password, customers_newsletter  FROM ". TABLE_CUSTOMERS . " where customers_email_address = '".$email_address."'";
			$sql = $dbConn->prepare($consulta);
			$sql->execute();
			$resultado = $sql->fetch(PDO::FETCH_ASSOC);
			if($resultado != null){
				echo json_encode([$resultado],JSON_UNESCAPED_UNICODE);
			}else{
				echo json_encode([],JSON_UNESCAPED_UNICODE);
			}
		}else if(isset($_GET['idCustomer'])){
			$idCustomer = tep_db_prepare_input($_GET['idCustomer']);
			$consulta = "SELECT * FROM ". TABLE_ADDRESS_BOOK . " where customers_id = ".$idCustomer;
			$sql = $dbConn->prepare($consulta);
			$sql->execute();
			$resultado = $sql->fetch(PDO::FETCH_ASSOC);
			if($resultado != null){
				echo json_encode([$resultado],JSON_UNESCAPED_UNICODE);
			}else{
				echo json_encode([],JSON_UNESCAPED_UNICODE);
			}
		}else
		{
			echo json_encode([],JSON_UNESCAPED_UNICODE);
		}
		exit();
		 
	}
	
	// Crear cuenta
	if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['tokenFFL'])&& $_POST['tokenFFL'] == $tokenFFL)
    {

		// ME FIJO SI EXISTE UNA CUENTA CON EL USUARIO
		$email_address = tep_db_prepare_input($_POST['customers_email_address']);
		$consultaBuscoCuenta = "SELECT * FROM ". TABLE_CUSTOMERS . " where customers_email_address = '".$email_address."'";
		$sqlGetUsuarioExistente = $dbConn->prepare($consultaBuscoCuenta);
		$sqlGetUsuarioExistente->execute();
		$existeUsuario = $sqlGetUsuarioExistente->fetch(PDO::FETCH_ASSOC);
		if($existeUsuario == null){//NO EXISTE UN USUARIO CON EL MAIL PASADO


			// OBTENGO EL ID DE ARGENTINA
			$sqlGetIdCountry = "SELECT countries_id FROM countries where countries_iso_code_3 = 'ARG'";
			$statement = $dbConn->prepare($sqlGetIdCountry);
			$statement->execute();
			$idCountry = $statement->fetch();


			// OBTENGO EL ID DE LA ZONA (PROVINCIA) SELECCIONADA
			$idProvincia = tep_db_prepare_input($_POST['customers_provincia']);
			$sqlGetIdZone = "SELECT zone_id FROM zones where zone_country_id = ".$idCountry[0]." and zone_name = '".$idProvincia."'";
			$statement = $dbConn->prepare($sqlGetIdZone);
			$statement->execute();
			$idZone = $statement->fetch();

			if(isset($idZone)&& count($idZone)>0){

				
				$gender = tep_db_prepare_input($_POST['customers_gender']);
				$firstname = tep_db_prepare_input($_POST['customers_firstname']);
				$lastname = tep_db_prepare_input($_POST['customers_lastname']);
				$street_address = tep_db_prepare_input($_POST['customers_direccion']);
				$suburb = tep_db_prepare_input($_POST['customers_suburbio']);
				$postcode = tep_db_prepare_input($_POST['customers_cp']);
				$city = tep_db_prepare_input($_POST['customers_ciudad']);
				$state = tep_db_prepare_input($_POST['customers_provincia']);
				$zone_id = $idZone[0];
				$country = $idCountry[0];
				$telephone = tep_db_prepare_input($_POST['customers_telephone']);
				$fax = tep_db_prepare_input($_POST['customers_fax']);
				$newsletter = tep_db_prepare_input($_POST['customers_newsletter']);
				$password = tep_db_prepare_input($_POST['customers_password']);
				
			/*	$sql_data_array = array('customers_firstname' => $firstname,
				'customers_lastname' => $lastname,
				'customers_email_address' => $email_address,
				'customers_telephone' => $telephone,
				'customers_fax' => $fax,
				'customers_newsletter' => $newsletter,
				'customers_password' => tep_encrypt_password($password));
				$sql_data_array['customers_gender'] = $gender;*/

				//tep_db_perform(TABLE_CUSTOMERS, $sql_data_array);
				$queryInsertCustomers = "INSERT INTO ".TABLE_CUSTOMERS."(customers_firstname, customers_lastname,customers_email_address, customers_telephone, customers_fax, customers_newsletter, customers_password, customers_gender)
				VALUES(:customers_firstname, :customers_lastname, :customers_email_address, :customers_telephone, :customers_fax, :customers_newsletter, :customers_password, :customers_gender)";
				
				$statementInsertCustomers = $dbConn->prepare($queryInsertCustomers);
		
				$passwordEncriptado = tep_encrypt_password($password);
			
				$statementInsertCustomers->bindParam (":customers_firstname",  $firstname , PDO::PARAM_STR);
				$statementInsertCustomers->bindParam (":customers_lastname",   $lastname , PDO::PARAM_STR);
				$statementInsertCustomers->bindParam (":customers_email_address",  $email_address , PDO::PARAM_STR);
				$statementInsertCustomers->bindParam (":customers_telephone",  $telephone, PDO::PARAM_STR);
				$statementInsertCustomers->bindParam (":customers_fax", $fax , PDO::PARAM_STR);
				$statementInsertCustomers->bindParam (":customers_newsletter", $newsletter , PDO::PARAM_STR);
				$statementInsertCustomers->bindParam (":customers_password",$passwordEncriptado, PDO::PARAM_STR);
				$statementInsertCustomers->bindParam (":customers_gender",  $gender , PDO::PARAM_STR);
				
				$statementInsertCustomers->execute();
				
			//	$customer_id = tep_db_insert_id();

				$customer_id = $dbConn->lastInsertId();

			//	echo("EJECUTO BIEN EL INSERT EN TABLE_CUSTOMERS, EL ID ES:".$customer_id);

			/*	$sql_data_array = array('customers_id' => $customer_id,
							'entry_firstname' => $firstname,
							'entry_lastname' => $lastname,
							'entry_street_address' => $street_address,
							'entry_postcode' => $postcode,
							'entry_city' => $city,
							'entry_country_id' => $country);

				$sql_data_array['entry_gender'] = $gender;
				$sql_data_array['entry_suburb'] = $suburb;
				$sql_data_array['entry_zone_id'] = $zone_id;
				$sql_data_array['entry_state'] = $state;*/



				$queryInsertAddressBook = "INSERT INTO ".TABLE_ADDRESS_BOOK."(customers_id, entry_firstname, entry_lastname, entry_street_address, entry_postcode, entry_city, entry_country_id, entry_gender, entry_suburb, entry_zone_id, entry_state)
				VALUES(:customers_id, :entry_firstname, :entry_lastname, :entry_street_address, :entry_postcode, :entry_city, :entry_country_id, :entry_gender, :entry_suburb, :entry_zone_id, :entry_state)";
				
				$statementInsertAddressBook = $dbConn->prepare($queryInsertAddressBook);
		
			
				$statementInsertAddressBook->bindParam (":customers_id",  $customer_id , PDO::PARAM_INT);
				$statementInsertAddressBook->bindParam (":entry_firstname",  $firstname , PDO::PARAM_STR);
				$statementInsertAddressBook->bindParam (":entry_lastname",  $lastname , PDO::PARAM_STR);
				$statementInsertAddressBook->bindParam (":entry_street_address",  $street_address , PDO::PARAM_STR);
				$statementInsertAddressBook->bindParam (":entry_postcode",  $postcode , PDO::PARAM_STR);
				$statementInsertAddressBook->bindParam (":entry_city",  $city , PDO::PARAM_STR);
				$statementInsertAddressBook->bindParam (":entry_country_id",  $country , PDO::PARAM_STR);
				$statementInsertAddressBook->bindParam (":entry_gender",  $gender , PDO::PARAM_STR);
				$statementInsertAddressBook->bindParam (":entry_suburb",  $suburb , PDO::PARAM_STR);
				$statementInsertAddressBook->bindParam (":entry_zone_id", $zone_id , PDO::PARAM_INT);
				$statementInsertAddressBook->bindParam (":entry_state",  $state , PDO::PARAM_STR);


				$statementInsertAddressBook->execute();

				$address_id = $dbConn->lastInsertId();

	//			tep_db_perform(TABLE_ADDRESS_BOOK, $sql_data_array);
			//	echo("EJECUTO BIEN EL INSERT EN TABLE_ADDRESS_BOOK, EL ID ES:".$address_id);

				$queryUpdateCustomers = "update " . TABLE_CUSTOMERS . " set customers_default_address_id = '" . (int)$address_id . "' where customers_id = '" . (int)$customer_id . "'";
				//tep_db_query("update " . TABLE_CUSTOMERS . " set customers_default_address_id = '" . (int)$address_id . "' where customers_id = '" . (int)$customer_id . "'");
				
				$statementUpdateCustomers = $dbConn->prepare($queryUpdateCustomers);
				$statementUpdateCustomers->execute();
				
			//	echo("EJECUTO BIEN EL UPDATE EN TABLE_CUSTOMERS");

				
				$queryInsertCustomersInfo = "insert into " . TABLE_CUSTOMERS_INFO . " (customers_info_id, customers_info_number_of_logons, customers_info_date_account_created) values ('" . (int)$customer_id . "', '0', now())";
				$statementInsertCustomersInfo = $dbConn->prepare($queryInsertCustomersInfo);
				$statementInsertCustomersInfo->execute();
				


			//	tep_db_query("insert into " . TABLE_CUSTOMERS_INFO . " (customers_info_id, customers_info_number_of_logons, customers_info_date_account_created) values ('" . (int)$customer_id . "', '0', now())");
			//	echo("EJECUTO BIEN EL INSERT EN TABLE_CUSTOMERS_INFO");

				$consulta = "SELECT * FROM ". TABLE_CUSTOMERS . " where customers_id = '".(int)$customer_id."'";
				$sql = $dbConn->prepare($consulta);
				$sql->execute();
				$resultado = $sql->fetch(PDO::FETCH_ASSOC);
				if($resultado != null){
					echo json_encode([$resultado],JSON_UNESCAPED_UNICODE);
				}else{
					echo json_encode([],JSON_UNESCAPED_UNICODE);
				}

	/*
				$sqlInsertAddressBook = "INSERT INTO address_book(customers_id,entry_gender, entry_company, entry_firstname, entry_lastname, 
									entry_street_address, entry_suburb, entry_postcode, entry_city, entry_state, entry_country_id, entry_zone_id)
									VALUES(:customers_id, :customers_gender, :customers_company, :customers_firstname, :customers_lastname,
									:customers_direccion, :customers_suburbio, :customers_cp, :customers_ciudad, :customers_provincia, :customers_country_id,:customers_zone_id)";*/
			}else{
				echo json_encode([],JSON_UNESCAPED_UNICODE);
			}
		}else{
			echo json_encode([],JSON_UNESCAPED_UNICODE);
		}
		exit();
	}

	//Actualizar
    if ($_SERVER['REQUEST_METHOD'] == 'PUT')
    {
		parse_str(file_get_contents("php://input"), $_PUT);
		if(!($_PUT['tokenFFL']== null || $_PUT['tokenFFL'] != $tokenFFL)){
			foreach ($_PUT as $key => $value)
			{
				unset($_PUT[$key]);
				if($key != 'tokenFFL'){	
					$_PUT[str_replace('amp;', '', $key)] = $value;
				}
			}
			$_REQUEST = array_merge($_REQUEST, $_PUT);
			$input = $_PUT;
			$email = $_PUT['email'];
			$plainPsw = $_PUT['newPassword'];
			$encriptedPsw = tep_encrypt_password($plainPsw);
			echo("PASSWORD PLANO:".$plainPsw);
			echo("PASSWORD ENCRIPTADO:".$encriptedPsw);

			$fields = getParams($input);
			$sql = "update " . TABLE_CUSTOMERS . " set customers_password = '" . $encriptedPsw . "' where customers_email_address = '".$email."'";
			echo("SQL ES:".$sql);
		//	$sql = "UPDATE forms  SET $fields  WHERE id=$itemId ";
			$statement = $dbConn->prepare($sql);
		//	bindAllValues($statement, $input);
			$result = $statement->execute();
		}

        exit();
    }
    
    
    //En caso de que ninguna de las opciones anteriores se haya ejecutado
    header("HTTP/1.1 400 Bad Request");
    ?>
