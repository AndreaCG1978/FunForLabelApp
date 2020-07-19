    <?php

      $tokenFFL = 27029085;
      //Abrir conexion a la base de datos
      function connect($db)
      {
          try {
              $conn = new PDO("mysql:host={$db['host']};port={$db['port']};dbname={$db['db']};charset=utf8", $db['username'], $db['password']);
              // set the PDO error mode to exception
	      $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
              return $conn;
          } catch (PDOException $exception) {
              exit($exception->getMessage());
          }
      }
     //Obtener parametros para updates
     function getParams($input)
     {
        $filterParams = [];
        foreach($input as $param => $value)
        {
                $filterParams[] = "$param=:$param";
        }
        return implode(", ", $filterParams);
      }
      //Asociar todos los parametros a un sql
      function bindAllValues($statement, $params)
      {
        foreach($params as $param => $value)
        {
            $statement->bindValue(':'.$param, $value);
        }
        return $statement;
       }

       define('KN_LABEL_SUB_TOTAL', 'Sub-Total:');
       define('KN_LABEL_TOTAL', 'Total:');
       define('KN_LABEL_CLASS_TOTAL', 'ot_total');
       define('KN_LABEL_CLASS_SUBTOTAL', 'ot_subtotal');
       define('KN_LABEL_CLASS_SHIPPING', 'ot_shipping');
       define('KN_VALUE_SHIPPING', 2);
       define('KN_VALUE_TOTAL', 6);
       define('KN_VALUE_SUBTOTAL', 1);
       define('KN_ORDER_STATUS_ID', 6);
       define('KN_CUSTOMER_NOTIFIED', 1);

     ?>
