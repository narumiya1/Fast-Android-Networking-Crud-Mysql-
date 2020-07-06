<?php

include('connection.php') ;

$number = $_POST['number'];

if(!empty($number)) {

    $sql = " DELETE FROM tb_player WHERE number='$number' " ;

    $query = mysqli_query($conn, $sql) ;

    $data['status'] = true ;
    $data['berhasil']='berhasil' ;

}else {
    $data['status'] = false ;
    $data['result'] = 'gagal' ;
}

print_r(json_encode($data)) ;



?>