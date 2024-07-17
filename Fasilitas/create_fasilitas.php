<?php

require_once 'konek.php';

header('Content-Type: application/json');

// Memeriksa apakah semua data POST diterima
if (isset($_POST['nama_fasilitas'], $_POST['kategori'])) {

    $id_fasilitas = uniqid('fasilitas_'); // Menghasilkan ID unik dengan prefix 'room_'
    $nama_fasilitas = $_POST['nama_fasilitas'];
    $kategori = $_POST['kategori'];
    
    // Query SQL untuk menyimpan data ke database
    $sql = "INSERT INTO fasilitas (id_fasilitas, nama_fasilitas, kategori) 
            VALUES ('$id_fasilitas', '$nama_fasilitas', '$kategori')";

    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'success', 'id_fasilitas' => $id_fasilitas));
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Gagal menyimpan data', 'error' => mysqli_error($db)));
    }

} else {
    echo json_encode(array('status' => 'error', 'message' => 'Data tidak lengkap', 'received' => $_POST));
}

mysqli_close($db);
?>
