<?php
require_once 'dbcon.php';

header('Content-Type: application/json');

// Mengambil data dari POST
$id_karyawan = isset($_POST['id_karyawan']) ? $_POST['id_karyawan'] : '';
$nama_karyawan = isset($_POST['nama_karyawan']) ? $_POST['nama_karyawan'] : '';
$no_hp = isset($_POST['no_hp']) ? $_POST['no_hp'] : '';
$alamat = isset($_POST['alamat']) ? $_POST['alamat'] : '';

if ($db->connect_error) {
    die("Connection failed: " . $db->connect_error);
}

if (!empty($id)) {
    // Jika ada ID pelanggan, maka proses untuk update data
    $stmt = $db->prepare("UPDATE karyawan SET nama_karyawan=?,no_hp=?,alamat=?  WHERE id_karyawan=?");
    $stmt->bind_param("sssi", $nama_karyawan, $no_hp,$alamat, $id_karyawan);
} else {
    // Jika tidak ada ID pelanggan, maka proses untuk insert data baru
    $stmt = $db->prepare("INSERT INTO karyawan (nama_karyawan, no_hp, alamat) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $nama_karyawan, $no_hp, $alamat);
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
