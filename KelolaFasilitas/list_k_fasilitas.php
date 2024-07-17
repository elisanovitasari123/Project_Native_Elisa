<?php

require_once 'koneksiFasilitas.php';

// Memeriksa apakah koneksi berhasil
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Mempersiapkan query SQL untuk mengambil data dari tabel booking, customer, dan room
$sql = "SELECT kelolaFasilitas.id_k_fasilitas, villa.namaVilla, fasilitas.nama_fasilitas, room.namaKamar, kelolaFasilitas.status_fasilitas
        FROM kelolaFasilitas
        JOIN villa ON kelolaFasilitas.id_villa = villa.id_villa JOIN fasilitas ON kelolaFasilitas.id_fasilitas = fasilitas.id_fasilitas
        JOIN room ON kelolaFasilitas.roomid = room.roomid";

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
