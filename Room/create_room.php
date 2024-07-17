<?php

require_once 'conec.php';

header('Content-Type: application/json');

// Memeriksa apakah semua data POST diterima
if (isset($_POST['namaKamar'], $_POST['tipeKamar'], $_POST['harga'])) {

    $roomid = uniqid('room_'); // Menghasilkan ID unik dengan prefix 'room_'
    $namaKamar = $_POST['namaKamar'];
    $tipeKamar = $_POST['tipeKamar'];
    $harga = $_POST['harga'];
    
    // Query SQL untuk menyimpan data ke database
    $sql = "INSERT INTO room (roomid, namaKamar, tipeKamar, harga) 
            VALUES ('$roomid', '$namaKamar', '$tipeKamar', '$harga')";

    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'success', 'roomid' => $roomid));
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Gagal menyimpan data', 'error' => mysqli_error($db)));
    }

} else {
    echo json_encode(array('status' => 'error', 'message' => 'Data tidak lengkap', 'received' => $_POST));
}

mysqli_close($db);
?>
