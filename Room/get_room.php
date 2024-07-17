<?php
require_once "conec.php";

header('Content-Type: application/json');

$sql = "SELECT roomid, namaKamar FROM room";
$result = $db->query($sql);

if ($result->num_rows > 0) {
    $return_arr['room'] = array();
    while ($row = $result->fetch_assoc()) {
        array_push($return_arr['room'], array(
            'roomid' => $row['roomid'],
            'namaKamar' => $row['namaKamar']
        ));
    }
    echo json_encode($return_arr);
} else {
    echo json_encode(array('room' => array()));
}

$db->close();
?>
