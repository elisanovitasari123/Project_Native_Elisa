<?php
require_once 'koneksiBooking.php';

// Mengecek apakah id_pelanggan_hutang dikirim melalui POST
if (isset($_POST['booking_id'])) {
    $booking_id = mysqli_real_escape_string($conn, $_POST['booking_id']);

    // Mempersiapkan query SQL untuk menghapus data berdasarkan id_pelanggan_hutang
    $sql = "DELETE FROM booking WHERE booking_id = '$booking_id'";
    $query = mysqli_query($conn, $sql);

    // Memeriksa apakah query berhasil dijalankan
    if ($query) {
        // Mengembalikan respons sukses dalam format JSON
        echo json_encode(array('status' => 'success', 'message' => 'Data berhasil dihapus'));
    } else {
        // Mengembalikan pesan kesalahan dalam format JSON jika query gagal
        echo json_encode(array('status' => 'error', 'message' => 'Gagal menghapus data: ' . mysqli_error($conn)));
    }
} else {
    // Mengembalikan pesan kesalahan dalam format JSON jika id_pelanggan_hutang tidak dikirim
    echo json_encode(array('status' => 'error', 'message' => 'ID booking tidak dikirim'));
}
