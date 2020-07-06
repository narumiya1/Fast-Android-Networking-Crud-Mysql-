<?php

include('connection.php') ;

$sql = "SELECT * FROM tb_player" ;

$query = mysqli_query($conn,$sql);

if(mysqli_num_rows($query)>0){
    while($row = mysqli_fetch_object($query)) {
        $data['status'] = true ;
        $data['result'][]= $row ;

    }
}else{
    $data['status'] = false ;
    $data['result'] = "data not found" ;
}

print_r(json_encode($data)) ;

?>