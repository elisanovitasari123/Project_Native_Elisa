<?php
require_once "dbcon.php";

header('Content-Type: application/json');

$sql = "SELECT id_karyawan, nama_karyawan FROM karyawan";
$result = $db->query($sql);

if ($result->num_rows > 0) {
    $return_arr['karyawan'] = array();
    while ($row = $result->fetch_assoc()) {
        array_push($return_arr['karyawan'], array(
            'id_karyawan' => $row['id_karyawan'],
            'nama_karyawan' => $row['nama_karyawan']
        ));
    }
    echo json_encode($return_arr);
} else {
    echo json_encode(array('karyawan' => array()));
}

$db->close();
?>
