<?php
require_once 'koneksip.php';

header('Content-Type: application/json');

// Mengambil data dari POST
$id = isset($_POST['id']) ? $_POST['id'] : '';
$customer_id = isset($_POST['customer_id']) ? $_POST['customer_id'] : '';
$kamar_id = isset($_POST['kamar_id']) ? $_POST['kamar_id'] : '';
$metode_pembayaran = isset($_POST['metode_pembayaran']) ? $_POST['metode_pembayaran'] : '';
$catatan = isset($_POST['catatan']) ? $_POST['catatan'] : '';
$tanggal_pembayaran = isset($_POST['tanggal_pembayaran']) ? $_POST['tanggal_pembayaran'] : '';

if ($db->connect_error) {
    die("Connection failed: " . $db->connect_error);
}

if (!empty($id)) {
    // Jika ada ID pembayaran, maka proses untuk update data
    $stmt = $db->prepare("UPDATE pembayaran SET customer_id=?, kamar_id=?, metode_pembayaran=?, catatan=?, tanggal_pembayaran=? WHERE id=?");
    $stmt->bind_param("sssssi", $customer_id, $kamar_id, $metode_pembayaran, $catatan, $tanggal_pembayaran, $id);
} else {
    // Jika tidak ada ID pembayaran, maka proses untuk insert data baru
    $stmt = $db->prepare("INSERT INTO pembayaran (id, customer_id, kamar_id, metode_pembayaran, catatan, tanggal_pembayaran) VALUES (?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("ssssss", $id, $customer_id, $kamar_id, $metode_pembayaran, $catatan, $tanggal_pembayaran);
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
?>
