<?php
require_once "konek.php";

header('Content-Type: application/json');

$sql = "SELECT id_fasilitas, nama_fasilitas FROM fasilitas";
$result = $db->query($sql);

if ($result->num_rows > 0) {
    $return_arr['fasilitas'] = array();
    while ($row = $result->fetch_assoc()) {
        array_push($return_arr['fasilitas'], array(
            'id_fasilitas' => $row['id_fasilitas'],
            'nama_fasilitas' => $row['nama_fasilitas']
        ));
    }
    echo json_encode($return_arr);
} else {
    echo json_encode(array('fasilitas' => array()));
}

$db->close();
?>
