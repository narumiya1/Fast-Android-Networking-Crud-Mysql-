<?php

include('connection.php') ;

$number     = $_POST['number'];
$name       = $_POST['name'];
$address    = $_POST['address'];
$club       = $_POST['club'];

if(!empty($name) && !empty($number)){

    $sqlCheck = "SELECT COUNT(*) FROM tb_player WHERE number='$number' AND name= '$name'" ;
    $queryCheck = mysqli_query($conn,$sqlCheck) ;
    $hasilCheck = mysqli_fetch_array($queryCheck) ;

    if($hasilCheck[0] == 0) {
        $sql = "INSERT INTO tb_player (number,name,address,club) VALUES ('$number',' $name', '$address', '$club')" ;
    
        $query = mysqli_query($conn,$sql);

        if(mysqli_affected_rows($conn) > 0) {
            $data['status'] = true ;
            $data['result'] = "berhasil" ;
        }else{
            $data['status'] = false ;
            $data['result'] = "gagal" ;
        }
    }else{
        $data['status'] = false ;
        $data['result'] = "gagal, data sudah ada" ;
    }
    
    
}
else{
        $data['status'] = false;
        $data['result']	= "Gagal, Number dan Nama tidak boleh kosong!";
    
    }

    print_r(json_encode($data)) ;







?>