<?php
include 'dbconfig.php';

$KodeHotel = $_POST['kodehotel'];
$TypeKamar  = $_POST['typekamar'];
$HargaPerMalam = $_POST['hargapermalam'];
$TglCheckIn= $_POST['tglcheckin'];
$TglCheckOut = $_POST['tglcheckout'];
$id = $_POST['id'];

// Create connection
$conn = mysqli_connect($host, $user, $pw, $db);
// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

/* grab the posts from the db */
$query = "INSERT INTO kamar (kodehotel, typekamar , hargapermalam, tglcheckin, tglcheckout) values('".$KodeHotel."','".$TypeKamar ."','".$HargaPerMalam."','".$TglCheckIn."','".$TglCheckOut."')";

if ($id != "0"){
$query = "UPDATE kamar set typekamar ='".$TypeKamar ."',tglcheckout='".$TglCheckOut."',hargapermalam='".$HargaPerMalam."',tglcheckin='".$TglCheckIn."' where id =".$id;
}
$response = mysqli_query($conn, $query) or die('Error query:  '.$query);
$lid = mysqli_insert_id($conn);
$result["errormsg"]="Penginapan Berhasil";
$result["lid"]="$lid";
if ($response == 1){
	echo json_encode($result);
}
else{
	$result["errormsg"]="Fail";
	echo json_encode($result);
}
?>
