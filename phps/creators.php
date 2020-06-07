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

		if (isset($_GET['productId']) && isset($_GET['getCreator']) && $_GET['getCreator'])// SE PIDE EL CREADOR
		{
			$productId = tep_db_prepare_input($_GET['productId']);
			$consulta = "SELECT ". TCM_CREATORS . ".creators_id, ". TCM_CREATORS .".name, " . TCM_CREATOR_TYPES . ".name as creator_types_name, dpi, width, height, rounded, round, borders_solid, borders_solid_custom, fill_solid, fill_background, fill_solid_custom, fill_background_custom, icons, icons_custom, text, title, mirror, icons_width, icons_height, icons_position, icons_margin, icons_shape, icons_round FROM ". TCM_CREATORS_PRODUCTS . ", ". TCM_CREATORS . ", " . TCM_CREATOR_TYPES . " where products_id = ".$productId." and ". TCM_CREATORS_PRODUCTS . ".creators_id = ". TCM_CREATORS . ".creators_id and " . TCM_CREATOR_TYPES . ".creator_types_id = " . TCM_CREATORS . ".creator_types_id";
			//echo ($consulta);
			$sql = $dbConn->prepare($consulta);
			$sql->execute();
			$resultado = $sql->fetch(PDO::FETCH_ASSOC);
			if($resultado != null){
                header("HTTP/1.1 200 OK");
                echo json_encode($resultado,JSON_UNESCAPED_UNICODE);
			}else{
				echo json_encode([],JSON_UNESCAPED_UNICODE);
			}
		}elseif (isset($_GET['creatorId']) && isset($_GET['getImages']) && $_GET['getImages'])// SE PIDEN LAS IMAGENES
		{
			$creatorId = tep_db_prepare_input($_GET['creatorId']);
			$consulta = "SELECT ". TCM_FILLS_TEXTURED . ".fills_textured_id, ". TCM_FILLS_TEXTURED .".uploaded_files_id, uniquename, info FROM ". TCM_CREATORS_HAS_FILLS_TEXTURED . ", ". TCM_FILLS_TEXTURED . ", " . TCM_UPLOADED_FILES . " where creators_id = ".$creatorId." and ". TCM_CREATORS_HAS_FILLS_TEXTURED . ".fills_textured_id = ". TCM_FILLS_TEXTURED . ".fills_textured_id and " . TCM_FILLS_TEXTURED . ".uploaded_files_id = " . TCM_UPLOADED_FILES . ".uploaded_files_id";
			//echo ($consulta);
			$sql = $dbConn->prepare($consulta);
			$sql->execute();
			$resultado = $sql->fetchAll(PDO::FETCH_ASSOC);
			if($resultado != null){
                header("HTTP/1.1 200 OK");
                echo json_encode($resultado,JSON_UNESCAPED_UNICODE);
			}else{
				echo json_encode([],JSON_UNESCAPED_UNICODE);
			}
		}
		
		
		
		
		else{
			echo json_encode([],JSON_UNESCAPED_UNICODE);
		}
		exit();
		 
	}
	
    
    
    //En caso de que ninguna de las opciones anteriores se haya ejecutado
    header("HTTP/1.1 400 Bad Request");
    ?>
