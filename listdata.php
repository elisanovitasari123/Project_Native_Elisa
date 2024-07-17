<?php

include 'dbconfig.php';

// Create connection
$conn = mysqli_connect($host, $user, $pw, $db);
// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}
$result["errorcode"]="0";
$sql = "SELECT * FROM kamar ORDER BY kodehotel";
$res = mysqli_query($conn, $sql);
$items = array();
if (mysqli_num_rows($res) > 0) {
	while($row = mysqli_fetch_object($res)){

	    array_push($items, $row);

	}
$result["errorcode"] = "1";
    $result["data"] = $items;
}else{
     $result["errormsg"] = "Tidak ada data";
}
echo json_encode($result);
mysqli_close($conn);
?>