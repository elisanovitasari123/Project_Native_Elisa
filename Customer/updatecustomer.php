<?php
require_once 'koneksi.php';

header('Content-Type: application/json');

// Mengambil data dari POST
$id = isset($_POST['id']) ? $_POST['id'] : '';
$nama = isset($_POST['nama']) ? $_POST['nama'] : '';
$n_hp = isset($_POST['phone']) ? $_POST['phone'] : '';
$alamat = isset($_POST['addres']) ? $_POST['addres'] : '';

if ($db->connect_error) {
    die("Connection failed: " . $db->connect_error);
}

if (!empty($id)) {
    // Jika ada ID pelanggan, maka proses untuk update data
    $stmt = $db->prepare("UPDATE customer SET nama=?, phone=?, addres=? WHERE id=?");
    $stmt->bind_param("sssi", $nama, $n_hp, $alamat, $id);
} else {
    // Jika tidak ada ID pelanggan, maka proses untuk insert data baru
    $stmt = $db->prepare("INSERT INTO customer (nama, phone, addres) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $nama, $n_hp, $alamat);
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
