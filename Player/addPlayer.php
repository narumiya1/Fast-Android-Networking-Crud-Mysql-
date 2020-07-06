<?php

include('connection.php');

$name 		= $_POST['name'];
$number	= $_POST['number'];
$address		= $_POST['address'];
$club 		= $_POST['club'];

if(!empty($nama) && !empty($noinduk)){

	$sqlCheck = "SELECT COUNT(*) FROM tb_player WHERE number='$number' AND name='$name'";
	$queryCheck = mysqli_query($conn,$sqlCheck);
	$hasilCheck = mysqli_fetch_array($queryCheck);
	
	if($hasilCheck[0] == 0){
		$sql = "INSERT INTO tb_player (name,number,address,club) VALUES('$name','$noinduk','$alamat','$hobi')";

		$query = mysqli_query($conn,$sql);

		if(mysqli_affected_rows($conn) > 0){
			$data['status'] = true;
			$data['result']	= "Berhasil";
		}else{
			$data['status'] = false;
			$data['result']	= "Gagal";
		}
	}else{
		$data['status'] = false;
		$data['result']	= "Gagal, Data Sudah Ada";
	}

	

}
else{
	$data['status'] = false;
	$data['result']	= "Gagal, Nomor Induk dan Nama tidak boleh kosong!";
}


print_r(json_encode($data));




?>