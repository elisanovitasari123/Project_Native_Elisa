<?php
require_once "conecvilla.php";

header('Content-Type: application/json');

$sql = "SELECT id_villa, namaVilla FROM villa";
$result = $db->query($sql);

if ($result->num_rows > 0) {
    $return_arr['villa'] = array();
    while ($row = $result->fetch_assoc()) {
        array_push($return_arr['villa'], array(
            'id_villa' => $row['id_villa'],
            'namavilla' => $row['namaVilla']
        ));
    }
    echo json_encode($return_arr);
} else {
    echo json_encode(array('villa' => array()));
}

$db->close();
?>
