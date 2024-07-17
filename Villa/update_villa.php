<?php
require_once 'conecvilla.php';

header('Content-Type: application/json');

// Mengambil data dari POST
$id_villa = isset($_POST['id_villa']) ? $_POST['id_villa'] : '';
$namaVilla= isset($_POST['namaVilla']) ? $_POST['namaVilla'] : '';
$kontak = isset($_POST['kontak']) ? $_POST['kontak'] : '';
$email = isset($_POST['email']) ? $_POST['email'] : '';
$lokasi = isset($_POST['lokasi']) ? $_POST['lokasi'] : '';

if ($db->connect_error) {
    die("Connection failed: " . $db->connect_error);
}

if (!empty($id)) {
    // Jika ada ID pelanggan, maka proses untuk update data
    $stmt = $db->prepare("UPDATE villa SET namaVilla=?, kontak=?, email=?, lokasi=? WHERE id_villa=?");
    $stmt->bind_param("ssssi", $namaVilla, $kontak, $email,$lokasi, $id_villa);
} else {
    // Jika tidak ada ID pelanggan, maka proses untuk insert data baru
    $stmt = $db->prepare("INSERT INTO villa (namaVilla, kontak, email, lokasi) VALUES (?, ?, ?,?)");
    $stmt->bind_param("ssss", $namaVilla, $kontak, $email,$lokasi);
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
