<?php
require_once 'conec.php';

header('Content-Type: application/json');

// Mengambil data dari POST
$roomid = isset($_POST['roomid']) ? $_POST['roomid'] : '';
$namaKamar = isset($_POST['namaKamar']) ? $_POST['namaKamar'] : '';
$tipeKamar = isset($_POST['tipeKamar']) ? $_POST['tipeKamar'] : '';
$harga = isset($_POST['harga']) ? $_POST['harga'] : '';

if ($db->connect_error) {
    die("Connection failed: " . $db->connect_error);
}

if (!empty($id)) {
    // Jika ada ID pelanggan, maka proses untuk update data
    $stmt = $db->prepare("UPDATE room SET namaKamar=?, tipeKamar=?, harga=? WHERE roomid=?");
    $stmt->bind_param("sssi", $namaKamar, $tipeKamar, $harga, $roomid);
} else {
    // Jika tidak ada ID pelanggan, maka proses untuk insert data baru
    $stmt = $db->prepare("INSERT INTO room (namaKamar, tipeKamar, harga) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $namaKamar, $tipeKamar, $harga);
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
