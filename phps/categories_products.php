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
        
		if (isset($_GET['parentId']) && isset($_GET['parentId']))
		{
			$parent_id = tep_db_prepare_input($_GET['parentId']);
			$consulta = "SELECT ". TABLE_CATEGORIES . ".categories_id, categories_name, categories_image, sort_order FROM ". TABLE_CATEGORIES . ", ". TABLE_CATEGORIES_DESCRIPTION . " where parent_id = ".$parent_id." and ". TABLE_CATEGORIES . ".categories_id = ". TABLE_CATEGORIES_DESCRIPTION . ".categories_id and language_id = 2";
			$sql = $dbConn->prepare($consulta);
			$sql->execute();
			$resultado = $sql->fetch(PDO::FETCH_ASSOC);
			if($resultado != null){
                header("HTTP/1.1 200 OK");
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
	
    
    
    //En caso de que ninguna de las opciones anteriores se haya ejecutado
    header("HTTP/1.1 400 Bad Request");
    ?>
