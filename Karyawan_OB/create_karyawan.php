<?php

require_once 'dbcon.php';

header('Content-Type: application/json');

// Memeriksa apakah semua data POST diterima
if (isset($_POST['nama_karyawan'], $_POST['no_hp'], $_POST['alamat'])) {

    $id_karyawan = uniqid('karyawan_'); // Menghasilkan ID unik dengan prefix 'room_'
    $nama_karyawan = $_POST['nama_karyawan'];
    $no_hp = $_POST['no_hp'];
    $alamat = $_POST['alamat'];
    
    // Query SQL untuk menyimpan data ke database
    $sql = "INSERT INTO karyawan (id_karyawan, nama_karyawan, no_hp, alamat) 
            VALUES ('$id_karyawan', '$nama_karyawan', '$no_hp','$alamat')";

    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'success', 'id_karyawan' => $id_karyawan));
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Gagal menyimpan data', 'error' => mysqli_error($db)));
    }

} else {
    echo json_encode(array('status' => 'error', 'message' => 'Data tidak lengkap', 'received' => $_POST));
}

mysqli_close($db);
?>
