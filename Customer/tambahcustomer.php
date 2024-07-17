<?php

require_once 'koneksi.php';

header('Content-Type: application/json');

// Memeriksa apakah semua data POST diterima
if (isset($_POST['nama'], $_POST['phone'], $_POST['addres'])) {

    $id = uniqid('pel_'); // Menghasilkan ID unik dengan prefix 'pel_'
    $nama = $_POST['nama'];
    $phone = $_POST['phone'];
    $addres = $_POST['addres'];
    
    // Query SQL yang dikoreksi
    $sql = "INSERT INTO customer (id, nama, phone, addres) 
            VALUES ('$id', '$nama', '$phone', '$addres' )";

    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'data_tersimpan', 'id' => $id));
    } else {
        echo json_encode(array('status' => 'gagal_tersimpan', 'error' => mysqli_error($db)));
    }

} else {
    echo json_encode(array('status' => 'data_tidak_lengkap', 'received' => $_POST));
}

mysqli_close($db);
