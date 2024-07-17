<?php
require_once 'konek.php';

// Mempersiapkan query SQL untuk mengambil data 
$sql = "SELECT * FROM fasilitas ORDER BY id_fasilitas DESC";
$query = mysqli_query($db, $sql);

// Memeriksa apakah query berhasil dijalankan
if (!$query) {
    // Mengembalikan pesan kesalahan dalam format JSON jika query gagal
    echo json_encode(array('status' => 'error', 'message' => 'Query failed: ' . mysqli_error($db)));
    exit;
}

// Membuat array untuk menyimpan data yang diambil
$list_data = array();

// Mengambil data dari hasil query dan menyimpannya dalam array
while ($data = mysqli_fetch_assoc($query)) {
    $list_data[] = array(
        'id_fasilitas' => $data['id_fasilitas'],
        'nama_fasilitas' => $data['nama_fasilitas'],
        'kategori' => $data['kategori']
        
    );
}

// Mengatur header agar browser mengetahui bahwa respons berformat JSON
header('Content-Type: application/json');

// Mengembalikan data dalam format JSON
echo json_encode(array('data' => $list_data));

