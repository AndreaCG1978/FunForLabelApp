<?php


    if (is_uploaded_file($_FILES['uploaded_file']['tmp_name'])) {
        $uploads_dir = './images/tcm/thumbs/';
        $tmp_name = $_FILES['uploaded_file']['tmp_name'];
        $pic_name = $_FILES['uploaded_file']['name'];
        move_uploaded_file($tmp_name, $uploads_dir.$pic_name);
    }else{
         echo "File not uploaded successfully.";
    }





   /* ini_set('display_errors', 1);
    ini_set('display_startup_errors', 1);
    error_reporting(E_ALL);
    $uploads_dir = '/images/tcm/thumbs';
    $status = -1;
    $entroIF = -1;
    if ($_FILES["uploaded_file"]["error"] == UPLOAD_ERR_OK) {
        $tmp_name = $_FILES["uploaded_file"]["tmp_name"];
        $entroIF = 1;
        $name = $_FILES["uploaded_file"]["name"];
    //    $status = move_uploaded_file($tmp_name, "$uploads_dir/$name");
        $status = move_uploaded_file($tmp_name, "/$name");
    }

    $response["status"] = $status;
    $response["entroIF"] = $entroIF;
    echo json_encode($response);*/
/*    
    $file_path = "";
    $file_path = $file_path . basename( $_FILES['uploaded_file']['name']);
    if(move_uploaded_file($_FILES['uploaded_file'] ['tmp_name'], $file_path)) {
          echo "success";
    }else{
        echo "fail";
    }*/
  ?>