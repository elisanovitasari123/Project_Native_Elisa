<?php
require_once 'konek.php';

header('Content-Type: application/json');

// Mengambil data dari POST
$id_fasilitas = isset($_POST['id_fasilitas']) ? $_POST['id_fasilitas'] : '';
$nama_fasilitas = isset($_POST['nama_fasilitas']) ? $_POST['nama_fasilitas'] : '';
$kategori = isset($_POST['kategori']) ? $_POST['kategori'] : '';

if ($db->connect_error) {
    die("Connection failed: " . $db->connect_error);
}

if (!empty($id)) {
    // Jika ada ID pelanggan, maka proses untuk update data
    $stmt = $db->prepare("UPDATE fasilitas SET nama_fasilitas=?,kategori=? WHERE id_fasilitas=?");
    $stmt->bind_param("ssi", $nama_fasilitas, $kategori, $id_fasilitas);
} else {
    // Jika tidak ada ID pelanggan, maka proses untuk insert data baru
    $stmt = $db->prepare("INSERT INTO fasilitas (nama_fasilitas, kategori) VALUES (?, ?)");
    $stmt->bind_param("ss", $nama_fasilitas, $kategori);
}

// Eksekusi query ke database
if ($stmt->execute()) {
    // Jika berhasil, kirimkan status 'data_tersimpan'
    echo json_encode(array('status' => 'data_tersimpan'));
} else {
    // Jika gagal, kirimkan status 'gagal_tersimpan'
    echo json_encode(array('status' => 'gagal_tersimpan'));
}

$stmt->close();
$db->close();
