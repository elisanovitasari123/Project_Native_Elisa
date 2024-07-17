<?php
require_once 'dbcon.php';

// Mengecek apakah id_pelanggan_bulanan dikirim melalui POST
if (isset($_POST['id_karyawan'])) {
    $id_karyawan = mysqli_real_escape_string($db, $_POST['id_karyawan']);

    // Mempersiapkan query SQL untuk menghapus data berdasarkan id_pelanggan_bulanan
    $sql = "DELETE FROM karyawan WHERE id_karyawan = '$id_karyawan'";
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
    echo json_encode(array('status' => 'error', 'message' => 'ID karyawan tidak dikirim'));
}
