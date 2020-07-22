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
            $sql = "INSERT INTO ". TABLE_ORDERS ."(customers_id, customers_name, customers_company, customers_street_address, customers_suburb, customers_city, customers_postcode, customers_state, customers_country, customers_telephone, customers_email_address, customers_address_format_id, delivery_name, delivery_company,delivery_street_address, delivery_suburb, delivery_city, delivery_postcode, delivery_state, delivery_country, delivery_address_format_id, billing_name, billing_company, billing_street_address, billing_suburb, billing_city, billing_postcode, billing_state, billing_country, billing_address_format_id, payment_method, cc_type, cc_owner,cc_number,cc_expires,last_modified, date_purchased, orders_status, currency, currency_value)
                VALUES(:customers_id, :customers_name, :customers_company, :customers_street_address, :customers_suburb, :customers_city, :customers_postcode, :customers_state, :customers_country, :customers_telephone, :customers_email_address, :customers_address_format_id, :delivery_name, :delivery_company, :delivery_street_address, :delivery_suburb, :delivery_city, :delivery_postcode, :delivery_state, :delivery_country, :delivery_address_format_id, :billing_name, :billing_company, :billing_street_address, :billing_suburb, :billing_city, :billing_postcode, :billing_state, :billing_country, :billing_address_format_id, :payment_method, :cc_type, :cc_owner,:cc_number,:cc_expires, :last_modified, :date_purchased, :order_status, :currency, :currency_value)";
            $statement = $dbConn->prepare($sql);
            $varBlanco='';
            $statement->bindParam (":customers_id",  $_POST['customers_id'] , PDO::PARAM_INT);
            $statement->bindParam (":customers_name",  $_POST['customers_name'] , PDO::PARAM_STR);
            $statement->bindParam (":customers_company",  $varBlanco , PDO::PARAM_STR);
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
            $statement->bindParam (":delivery_company",  $varBlanco, PDO::PARAM_STR);            
            $statement->bindParam (":delivery_street_address",  $_POST['delivery_street_address'] , PDO::PARAM_STR);   
            $statement->bindParam (":delivery_suburb",  $_POST['delivery_suburb'] , PDO::PARAM_STR);   
            $statement->bindParam (":delivery_city",  $_POST['delivery_city'] , PDO::PARAM_STR);   
            $statement->bindParam (":delivery_postcode",  $_POST['delivery_postcode'] , PDO::PARAM_STR);  
            $statement->bindParam (":delivery_state",  $_POST['delivery_state'] , PDO::PARAM_STR);  
            $statement->bindParam (":delivery_country",  $_POST['delivery_country'] , PDO::PARAM_STR);  
            $statement->bindParam (":delivery_address_format_id",  $_POST['delivery_address_format_id'] , PDO::PARAM_INT);
            $statement->bindParam (":billing_name",  $_POST['billing_name'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_company",  $varBlanco , PDO::PARAM_STR);  
            $statement->bindParam (":billing_street_address",  $_POST['billing_street_address'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_suburb",  $_POST['billing_suburb'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_city",  $_POST['billing_city'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_postcode",  $_POST['billing_postcode'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_state",  $_POST['billing_state'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_country",  $_POST['billing_country'] , PDO::PARAM_STR);  
            $statement->bindParam (":billing_address_format_id",  $_POST['billing_address_format_id'] , PDO::PARAM_INT);            
            $statement->bindParam (":payment_method",  $_POST['payment_method'] , PDO::PARAM_STR);  
            $statement->bindParam (":cc_type",  $varBlanco, PDO::PARAM_STR);  
            $statement->bindParam (":cc_owner",  $varBlanco, PDO::PARAM_STR);  
            $statement->bindParam (":cc_number",  $varBlanco, PDO::PARAM_STR);  
            $statement->bindParam (":cc_expires",  $varBlanco, PDO::PARAM_STR);  
            $statement->bindParam (":last_modified",  $_POST['last_modified'] , PDO::PARAM_STR);  
            $statement->bindParam (":date_purchased",  $_POST['date_purchased'] , PDO::PARAM_STR);
            $statement->bindParam (":order_status",  $_POST['order_status'] , PDO::PARAM_INT);
            $statement->bindParam (":currency",  $_POST['currency'] , PDO::PARAM_STR);
            $statement->bindParam (":currency_value",  $_POST['currency_value'] , PDO::PARAM_INT);            
            $statement->execute();
            $lastOrderId = $dbConn->lastInsertId();


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
            $temp9 = "<strong>$".$_POST['ot_value_total'].",00</strong>";
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
        }else if(isset($_POST['insertTag']) ||isset($_POST['insertTagWithTitle']) ){
            // SE INSERTA EN ORDERS_PRODUCTS

            $sql = "INSERT INTO ". TABLE_ORDERS_PRODUCTS ."(orders_id, products_id, products_model, products_name, products_price, final_price, products_tax, products_quantity)
                VALUES(:orders_id, :products_id, :products_model, :products_name, :products_price, :final_price, :products_tax, :products_quantity)";
            $statement = $dbConn->prepare($sql);
            $valorCero = 0;
            $statement->bindParam (":orders_id",  $_POST['orders_id'] , PDO::PARAM_INT);
            $statement->bindParam (":products_id",  $valorCero , PDO::PARAM_INT);
            $statement->bindParam (":products_model",  $_POST['op_products_model'] , PDO::PARAM_STR);
            $statement->bindParam (":products_name",  $_POST['op_products_name'] , PDO::PARAM_STR);
            $statement->bindParam (":final_price",  $_POST['op_final_price'] , PDO::PARAM_INT);
            $statement->bindParam (":products_price",  $_POST['op_products_price'] , PDO::PARAM_INT);
            $statement->bindParam (":products_tax",  $_POST['op_products_tax'] , PDO::PARAM_INT);
            $statement->bindParam (":products_quantity",  $_POST['op_products_quantity'] , PDO::PARAM_INT);
            $statement->execute();
            $idOrderProduct = $dbConn->lastInsertId();

            // SE INSERTA TAGS
            $sql = "INSERT INTO ". TCM_TAGS ."(fills_textured_id, orders_products_id, comment, tcm_tag, customers_id, products_id, icon_width, preview, parent)
            VALUES(:fills_textured_id, :orders_products_id, :comment, :tcm_tag, :customers_id, :products_id, :icon_width, :preview, :parent)";
            $statement = $dbConn->prepare($sql);
            $statement->bindParam (":fills_textured_id",$_POST['t_fills_textured_id'] , PDO::PARAM_INT);
            $statement->bindParam (":orders_products_id",$idOrderProduct, PDO::PARAM_INT);
            $statement->bindParam (":comment",  $_POST['t_comments'] , PDO::PARAM_STR);
            $statement->bindParam (":tcm_tag",  $_POST['t_tcm_tag'] , PDO::PARAM_STR);
            $statement->bindParam (":customers_id",$_POST['t_customers_id'], PDO::PARAM_INT);
            $statement->bindParam (":products_id",$_POST['t_products_id'], PDO::PARAM_INT);
            $statement->bindParam (":icon_width",$_POST['t_icon_width'], PDO::PARAM_INT);
            $statement->bindParam (":preview",  $_POST['t_preview'] , PDO::PARAM_STR);
            $statement->bindParam (":parent",$_POST['t_parent'], PDO::PARAM_INT);
            $statement->execute();
            $idTag = $dbConn->lastInsertId();

            // SE INSERTA TAGS_TEXT_OPTIONS, SI NO EXISTE. SI EXISTE, LO REUTILIZA
            $tto_size = tep_db_prepare_input($_POST['tto_size']);
            $tto_color = tep_db_prepare_input($_POST['tto_color']);
            $tto_effect_bold = tep_db_prepare_input($_POST['tto_effect_bold']);
            $tto_effect_italic = tep_db_prepare_input($_POST['tto_effect_italic']);
            $tto_fonts_id = tep_db_prepare_input($_POST['tto_fonts_id']);
            $sqlSearch = "SELECT * FROM ". TCM_TAG_TEXT_OPTIONS. " WHERE size =".$tto_size." AND color like '" .$tto_color. "' AND effect_bold=".$tto_effect_bold." AND effect_italic=".$tto_effect_italic. " AND fonts_id=" .$tto_fonts_id;
            $statement1 = $dbConn->prepare($sqlSearch);
            $statement1->execute();

            $resultado = $statement1->fetch(PDO::FETCH_ASSOC);

            if($resultado == null || $resultado== '' || $resultado ['tag_text_options_id']== null || $resultado ['tag_text_options_id'] == ''){

                $sql = "INSERT INTO ". TCM_TAG_TEXT_OPTIONS ."(size, color, effect_bold, effect_italic, fonts_id)
                VALUES(:size, :color, :effect_bold, :effect_italic, :fonts_id)";
                $valorCero=0;
                $statement2 = $dbConn->prepare($sql);
                $statement2->bindParam (":size",$tto_size, PDO::PARAM_INT);
                $statement2->bindParam (":color",$tto_color, PDO::PARAM_STR);
                $statement2->bindParam (":effect_bold",$valorCero, PDO::PARAM_INT);
                $statement2->bindParam (":effect_italic",$valorCero, PDO::PARAM_INT);
                $statement2->bindParam (":fonts_id",$tto_fonts_id, PDO::PARAM_INT);

                $statement2->execute();
                $idTagTextOptions = $dbConn->lastInsertId();
            }else{

                $idTagTextOptions = $resultado['tag_text_options_id'];
            }


            // SE INSERTA TCM_TAG_LEGENDS
            $sql = "INSERT INTO ". TCM_TAG_LEGENDS ."(text, type, text_options_id, tags_id)
            VALUES(:text, :type, :text_options_id, :tags_id)";
            $statement = $dbConn->prepare($sql);
            $statement->bindParam (":text",$_POST['tl_text'] , PDO::PARAM_STR);
            $statement->bindParam (":type",  $_POST['tl_type'] , PDO::PARAM_STR);
            $statement->bindParam (":text_options_id",$idTagTextOptions, PDO::PARAM_INT);
            $statement->bindParam (":tags_id",$idTag, PDO::PARAM_INT);
            $statement->execute();
            $idTagLegend = $dbConn->lastInsertId();


            // COMPRUEBA SI TIENE QUE INSERTAR EL TITULO
            if(isset($_POST['insertTagWithTitle']) ){
                // SE INSERTA TAGS_TEXT_OPTIONS, SI NO EXISTE. SI EXISTE, LO REUTILIZA
                $tto_size_title = tep_db_prepare_input($_POST['tto_size_title']);
                $tto_color_title = tep_db_prepare_input($_POST['tto_color_title']);
                $tto_effect_bold_title = tep_db_prepare_input($_POST['tto_effect_bold_title']);
                $tto_effect_italic_title = tep_db_prepare_input($_POST['tto_effect_italic_title']);
                $tto_fonts_id_title = tep_db_prepare_input($_POST['tto_fonts_id_title']);
                $sqlSearch = "SELECT * FROM ". TCM_TAG_TEXT_OPTIONS. " WHERE size =".$tto_size_title." AND color like '" .$tto_color_title. "' AND effect_bold=".$tto_effect_bold_title." AND effect_italic=".$tto_effect_italic_title. " AND fonts_id=" .$tto_fonts_id_title;
                $statement = $dbConn->prepare($sqlSearch);
                $statement->execute();

                $resultado = $statement->fetch(PDO::FETCH_ASSOC);

                if($resultado == null || $resultado== '' || $resultado ['tag_text_options_id']== null || $resultado ['tag_text_options_id'] == ''){

                    $sql = "INSERT INTO ". TCM_TAG_TEXT_OPTIONS ."(size, color, effect_bold, effect_italic, fonts_id)
                    VALUES(:size, :color, :effect_bold, :effect_italic, :fonts_id)";
                    $valorCero=0;
                    $statement = $dbConn->prepare($sql);
                    $statement->bindParam (":size",$tto_size_title, PDO::PARAM_INT);
                    $statement->bindParam (":color",$tto_color_title, PDO::PARAM_STR);
                    $statement->bindParam (":effect_bold",$valorCero, PDO::PARAM_INT);
                    $statement->bindParam (":effect_italic",$valorCero, PDO::PARAM_INT);
                    $statement->bindParam (":fonts_id",$tto_fonts_id_title, PDO::PARAM_INT);

                    $statement->execute();
                    $idTagTextOptionsTitle = $dbConn->lastInsertId();
                }else{
                    $idTagTextOptionsTitle = $resultado['tag_text_options_id'];
                }


                // SE INSERTA TCM_TAG_LEGENDS DEL TITLE
                $sql = "INSERT INTO ". TCM_TAG_LEGENDS ."(text, type, text_options_id, tags_id)
                VALUES(:text, :type, :text_options_id, :tags_id)";
                $statement = $dbConn->prepare($sql);
                $statement->bindParam (":text",$_POST['tl_text_title'] , PDO::PARAM_STR);
                $statement->bindParam (":type",  $_POST['tl_type_title'] , PDO::PARAM_STR);
                $statement->bindParam (":text_options_id",$idTagTextOptionsTitle, PDO::PARAM_INT);
                $statement->bindParam (":tags_id",$idTag, PDO::PARAM_INT);
                $statement->execute();
                $idTagLegendTitle = $dbConn->lastInsertId();                

            }


            header("HTTP/1.1 200 OK");
            echo json_encode( $idTagLegend,JSON_UNESCAPED_UNICODE);
            exit();

        }else{
            echo json_encode(-1,JSON_UNESCAPED_UNICODE);
        }
    }
    exit();
    
        
    //En caso de que ninguna de las opciones anteriores se haya ejecutado
    header("HTTP/1.1 400 Bad Request");
    ?>
