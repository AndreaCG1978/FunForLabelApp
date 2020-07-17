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


    if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['tokenFFL'])&& $_POST['tokenFFL'] == $tokenFFL)
    {
        // Crear tupla en orders
        if(isset($_POST['insertInOrders'])){

            // SE INSERTA EN ORDERS
            $sql = "INSERT INTO ". TABLE_ORDERS ."(customers_id, customers_name, customers_street_address, customers_suburb, customers_city, customers_postcode, customers_state, customers_country, customers_telephone, customers_email_address, customers_address_format_id, delivery_name, delivery_street_address, delivery_suburb, delivery_city, delivery_postcode, delivery_state, delivery_country, delivery_address_format_id, billing_name, billing_street_address, billing_suburb, billing_city, billing_postcode, billing_state, billing_country, billing_address_format_id, payment_method, last_modified, date_purchased, orders_status, currency, currency_value)
                VALUES(:customers_id, :customers_name, :customers_street_address, :customers_suburb, :customers_city, :customers_postcode, :customers_state, :customers_country, :customers_telephone, :customers_email_address, :customers_address_format_id, :delivery_name, :delivery_street_address, :delivery_suburb, :delivery_city, :delivery_postcode, :delivery_state, :delivery_country, :delivery_address_format_id, :billing_name, :billing_street_address, :billing_suburb, :billing_city, :billing_postcode, :billing_state, :billing_country, :billing_address_format_id, :payment_method, :last_modified, :date_purchased, :order_status, :currency, :currency_value)";
            $statement = $dbConn->prepare($sql);
            $statement->bindParam (":customers_id",  $_POST['customers_id'] , PDO::PARAM_INT);
            $statement->bindParam (":customers_name",  $_POST['customers_name'] , PDO::PARAM_STR);
            $statement->bindParam (":customers_street_address",  $_POST['customers_street_address'] , PDO::PARAM_STR);
            $statement->bindParam (":customers_suburb",  $_POST['customers_suburb'] , PDO::PARAM_STR);
            $statement->bindParam (":customers_city",  $_POST['customers_city'] , PDO::PARAM_STR);
            $statement->bindParam (":customers_postcode",  $_POST['customers_postcode'] , PDO::PARAM_STR);
            $statement->bindParam (":customers_state",  $_POST['customers_state'] , PDO::PARAM_STR);
            $statement->bindParam (":customers_country",  $_POST['customers_country'] , PDO::PARAM_STR);
            $statement->bindParam (":customers_telephone",  $_POST['customers_telephone'] , PDO::PARAM_STR);
            $statement->bindParam (":customers_email_address",  $_POST['customers_email_address'] , PDO::PARAM_STR);
            $statement->bindParam (":customers_address_format_id",  $_POST['customers_address_format_id'] , PDO::PARAM_INT);
            $statement->bindParam (":delivery_name",  $_POST['delivery_name'] , PDO::PARAM_STR);            
            $statement->bindParam (":delivery_street_address",  $_POST['delivery_street_address'] , PDO::PARAM_STR);   
            $statement->bindParam (":delivery_suburb",  $_POST['delivery_suburb'] , PDO::PARAM_STR);   
            $statement->bindParam (":delivery_city",  $_POST['delivery_city'] , PDO::PARAM_STR);   
            $statement->bindParam (":delivery_postcode",  $_POST['delivery_postcode'] , PDO::PARAM_STR);  
            $statement->bindParam (":delivery_state",  $_POST['delivery_state'] , PDO::PARAM_STR);  
            $statement->bindParam (":delivery_country",  $_POST['delivery_country'] , PDO::PARAM_STR);  
            $statement->bindParam (":delivery_address_format_id",  $_POST['delivery_address_format_id'] , PDO::PARAM_INT);
            $statement->bindParam (":billing_name",  $_POST['billing_name'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_street_address",  $_POST['billing_street_address'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_suburb",  $_POST['billing_suburb'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_city",  $_POST['billing_city'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_postcode",  $_POST['billing_postcode'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_state",  $_POST['billing_state'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_country",  $_POST['billing_country'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_address_format_id",  $_POST['billing_address_format_id'] , PDO::PARAM_INT);            
            $statement->bindParam (":payment_method",  $_POST['payment_method'] , PDO::PARAM_STR);  
            $statement->bindParam (":last_modified",  $_POST['last_modified'] , PDO::PARAM_STR);  
            $statement->bindParam (":date_purchased",  $_POST['date_purchased'] , PDO::PARAM_STR);
            $statement->bindParam (":order_status",  $_POST['order_status'] , PDO::PARAM_INT);
            $statement->bindParam (":currency",  $_POST['currency'] , PDO::PARAM_STR);
            $statement->bindParam (":currency_value",  $_POST['currency_value'] , PDO::PARAM_INT);            
            $statement->execute();
            $lastOrderId = $dbConn->lastInsertId();
         //   $statement = $dbConn->prepare("SELECT * FROM ". TABLE_ORDERS ." where id = ".$last_id);
          //  $statement->execute();
         //   $statement->setFetchMode(PDO::FETCH_ASSOC);

            // SE INSERTA EN ORDERS_TOTAL:SUB_TOTAL
            $sql = "INSERT INTO ". TABLE_ORDERS_TOTAL ."(orders_id, title, text, value, class, sort_order) VALUES(:orders_id, :title, :text, :value, :class, :sort_order)";
            $statement = $dbConn->prepare($sql);
            $statement->bindParam (":orders_id",$lastOrderId, PDO::PARAM_INT);
            $temp1 = KN_LABEL_SUB_TOTAL;
            $statement->bindParam (":title", $temp1, PDO::PARAM_STR);
            $temp2 = "$".$_POST['ot_value_subtotal'].",00";
            $statement->bindParam (":text",$temp2, PDO::PARAM_STR);
            $statement->bindParam (":value", $_POST['ot_value_subtotal'], PDO::PARAM_STR);
            $temp3 = KN_LABEL_CLASS_SUBTOTAL;
            $statement->bindParam (":class",$temp3 , PDO::PARAM_STR);
            $temp4 = KN_VALUE_SUBTOTAL;
            $statement->bindParam (":sort_order",$temp4, PDO::PARAM_INT);
            $statement->execute();

            // SE INSERTA EN ORDERS_TOTAL:SHIPPING
            $sql = "INSERT INTO ". TABLE_ORDERS_TOTAL ."(orders_id, title, text, value, class, sort_order) VALUES(:orders_id, :title, :text, :value, :class, :sort_order)";
            $statement = $dbConn->prepare($sql);
            $statement->bindParam (":orders_id",$lastOrderId, PDO::PARAM_INT);
            $statement->bindParam (":title", $_POST['ot_title_shipping'], PDO::PARAM_STR);
            $temp5 = "$".$_POST['ot_value_shipping'].",00";
            $statement->bindParam (":text",$temp5, PDO::PARAM_STR);
            $statement->bindParam (":value", $_POST['ot_value_shipping'], PDO::PARAM_STR);
            $temp6 = KN_LABEL_CLASS_SHIPPING;
            $statement->bindParam (":class", $temp6, PDO::PARAM_STR);
            $temp7= KN_VALUE_SHIPPING;
            $statement->bindParam (":sort_order",$temp7, PDO::PARAM_INT);
            $statement->execute();

            // SE INSERTA EN ORDERS_TOTAL:TOTAL
            $sql = "INSERT INTO ". TABLE_ORDERS_TOTAL ."(orders_id, title, text, value, class, sort_order) VALUES(:orders_id, :title, :text, :value, :class, :sort_order)";
            $statement = $dbConn->prepare($sql);
            $statement->bindParam (":orders_id",$lastOrderId, PDO::PARAM_INT);
            $temp8 = KN_LABEL_TOTAL;
            $statement->bindParam (":title",$temp8, PDO::PARAM_STR);
            $temp9 = "$".$_POST['ot_value_total'].",00";
            $statement->bindParam (":text",$temp9, PDO::PARAM_STR);
            $statement->bindParam (":value", $_POST['ot_value_total'], PDO::PARAM_STR);
            $temp10 = KN_LABEL_CLASS_TOTAL;
            $statement->bindParam (":class",$temp10, PDO::PARAM_STR);
            $temp11 = KN_VALUE_TOTAL;
            $statement->bindParam (":sort_order",$temp11, PDO::PARAM_INT);
            $statement->execute();

            // SE INSERTA EN ORDERS_STATUS_HISTORY
            $sql = "INSERT INTO ". TABLE_ORDERS_STATUS_HISTORY ."(orders_id, orders_status_id, date_added, customer_notified, comments) 
            VALUES(:orders_id, :orders_status_id, :date_added, :customer_notified, :comments)";
            $statement = $dbConn->prepare($sql);
            $statement->bindParam (":orders_id",$lastOrderId, PDO::PARAM_INT);
            $temp12 = KN_ORDER_STATUS_ID;
            $statement->bindParam (":orders_status_id",$temp12 , PDO::PARAM_INT);
            $statement->bindParam (":date_added", $_POST['osh_date_added'], PDO::PARAM_STR);
            $temp13 = KN_CUSTOMER_NOTIFIED;
            $statement->bindParam (":customer_notified",$temp13, PDO::PARAM_INT);
            $statement->bindParam (":comments",$_POST['osh_comments'], PDO::PARAM_STR);
            $statement->execute();            

            header("HTTP/1.1 200 OK");
            echo json_encode( $lastOrderId,JSON_UNESCAPED_UNICODE);
            exit();
        }else if(isset($_POST['insertInOrderProduct'])){
            $sql = "INSERT INTO ". TABLE_ORDERS_PRODUCTS ."(orders_id, products_id, products_model, products_name, products_price, final_price, products_tax, products_quantity)
                VALUES(:orders_id, :products_id, :products_model, :products_name, :products_price, :final_price, :products_tax, :products_quantity)";
            $statement = $dbConn->prepare($sql);
            $statement->bindParam (":orders_id",  $_POST['orders_id'] , PDO::PARAM_INT);
            $statement->bindParam (":products_id",  $_POST['products_id'] , PDO::PARAM_INT);
            $statement->bindParam (":products_model",  $_POST['products_model'] , PDO::PARAM_STR);
            $statement->bindParam (":products_price",  $_POST['products_price'] , PDO::PARAM_STR);
            $statement->bindParam (":final_price",  $_POST['final_price'] , PDO::PARAM_STR);
            $statement->bindParam (":products_tax",  $_POST['products_tax'] , PDO::PARAM_STR);
            $statement->bindParam (":products_quantity",  $_POST['products_quantity'] , PDO::PARAM_INT);
            $statement->execute();
            $last_id = $dbConn->lastInsertId();
         //   $statement = $dbConn->prepare("SELECT * FROM ". TABLE_ORDERS ." where id = ".$last_id);
          //  $statement->execute();
         //   $statement->setFetchMode(PDO::FETCH_ASSOC);
            header("HTTP/1.1 200 OK");
            echo json_encode( $last_id,JSON_UNESCAPED_UNICODE);
            exit();
        }else{
            echo json_encode([],JSON_UNESCAPED_UNICODE);
        }
    }
    exit();
    
        
    //En caso de que ninguna de las opciones anteriores se haya ejecutado
    header("HTTP/1.1 400 Bad Request");
    ?>
