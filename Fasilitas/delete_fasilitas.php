<?php
require_once 'konek.php';

// Mengecek apakah id_pelanggan_bulanan dikirim melalui POST
if (isset($_POST['id_fasilitas'])) {
    $id_fasilitas = mysqli_real_escape_string($db, $_POST['id_fasilitas']);

    // Mempersiapkan query SQL untuk menghapus data berdasarkan id_pelanggan_bulanan
    $sql = "DELETE FROM fasilitas WHERE id_fasilitas = '$id_fasilitas'";
    $query = mysqli_query($db, $sql);

    // Memeriksa apakah query berhasil dijalankan
    if ($query) {
        // Mengembalikan respons sukses dalam format JSON
        echo json_encode(array('status' => 'success', 'message' => 'Data berhasil dihapus'));
    } else {
        // Mengembalikan pesan kesalahan dalam format JSON jika query gagal
        echo json_encode(array('status' => 'error', 'message' => 'Gagal menghapus data: ' . mysqli_error($db)));
    }
} else {
    // Mengembalikan pesan kesalahan dalam format JSON jika id_pelanggan_bulanan tidak dikirim
    echo json_encode(array('status' => 'error', 'message' => 'ID fasiitas tidak dikirim'));
}
