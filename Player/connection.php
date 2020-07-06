<?php

$hostname = '127.0.0.1';
$username = 'root' ;
$password = '' ;
$database = 'db_player' ;

$conn = mysqli_connect($hostname, $username, $password, $database) ;
if(!$conn) {
    echo"gagal";
}

?>