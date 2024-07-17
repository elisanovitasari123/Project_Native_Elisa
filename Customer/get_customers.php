<?php
require_once "koneksi.php";

header('Content-Type: application/json');

$sql = "SELECT id, nama FROM customer";
$result = $db->query($sql);

if ($result->num_rows > 0) {
    $return_arr['customer'] = array();
    while ($row = $result->fetch_assoc()) {
        array_push($return_arr['customer'], array(
            'id' => $row['id'],
            'nama' => $row['nama']
        ));
    }
    echo json_encode($return_arr);
} else {
    echo json_encode(array('customer' => array()));
}

$db->close();
?>
