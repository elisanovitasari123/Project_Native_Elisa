<?php

require_once 'koneksiP.php';

// Memeriksa apakah koneksi berhasil
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Mempersiapkan query SQL untuk mengambil data dari tabel booking, customer, dan room
$sql = "SELECT pembersihan.id_p, room.namaKamar, karyawan.nama_karyawan, pembersihan.tanggal,pembersihan.deskripsi
        FROM pembersihan
        JOIN room ON pembersihan.roomid = room.roomid JOIN karyawan ON pembersihan.id_karyawan = karyawan.id_karyawan";

$query = $conn->query($sql);

// Memeriksa apakah query berhasil dijalankan
if (!$query) {
    // Mengembalikan pesan kesalahan dalam format JSON jika query gagal
    echo json_encode(array('status' => 'error', 'message' => 'Query failed: ' . $conn->error));
    exit;
}

// Membuat array untuk menyimpan data yang diambil
$list_data = array();

// Mengambil data dari hasil query dan menyimpannya dalam array
while ($row = $query->fetch_assoc()) {
    $list_data[] = $row;
}

// Mengatur header agar browser mengetahui bahwa respons berformat JSON
header('Content-Type: application/json');

// Mengembalikan data dalam format JSON
echo json_encode(array('data' => $list_data));

// Menutup koneksi ke database
$conn->close();
?>
