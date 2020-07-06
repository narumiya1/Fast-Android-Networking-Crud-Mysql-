<?php


include('connection.php');
 
$name       = $_POST['name'];
$number    = $_POST['number'];
$address     = $_POST['address'];
$club       = $_POST['club'];
 
if(!empty($name) && !empty($number)){
 
    $sql = "UPDATE tb_player set name='$name', address='$address', club='$club' WHERE number='$number' ";
 
    $query = mysqli_query($conn,$sql);
 
    if(mysqli_affected_rows($conn) > 0){
        $data['status'] = true;
        $data['result'] = "Berhasil";
    }else{
        $data['status'] = false;
        $data['result'] = "Gagal";
    }
 
}else{
    $data['status'] = false;
    $data['result'] = "Gagal, Number dan Name tidak boleh kosong!";
}
 
 
print_r(json_encode($data));
 
 

?>