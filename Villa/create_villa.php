<?php

require_once 'conecvilla.php';

header('Content-Type: application/json');

// Memeriksa apakah semua data POST diterima
if (isset($_POST['namaVilla'], $_POST['kontak'], $_POST['email'], $_POST['lokasi'])) {

    $id_villa = uniqid('villa_'); // Menghasilkan ID unik dengan prefix 'room_'
    $namaVilla = $_POST['namaVilla'];
    $kontak = $_POST['kontak'];
    $email = $_POST['email'];
    $lokasi = $_POST['lokasi'];
    
    // Query SQL untuk menyimpan data ke database
    $sql = "INSERT INTO villa (id_villa, namaVilla, kontak, email,lokasi) 
            VALUES ('$id_villa', '$namaVilla', '$kontak', '$email','$lokasi')";

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
