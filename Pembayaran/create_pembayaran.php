<?php

require_once 'koneksip.php';

header('Content-Type: application/json');

// Memeriksa apakah semua data POST diterima
if (isset($_POST['customer_id'], $_POST['kamar_id'], $_POST['metode_pembayaran'], $_POST['catatan'], $_POST['tanggal_pembayaran'])) {

    // Menghasilkan ID unik untuk pembayaran dengan prefix 'bay_'
    $id = uniqid('bay_');

    // Mengambil data dari POST
    $customer_id = $_POST['customer_id'];
    $kamar_id = $_POST['kamar_id'];
    $metode_pembayaran = $_POST['metode_pembayaran'];
    $catatan = $_POST['catatan'];
    $tanggal_pembayaran = $_POST['tanggal_pembayaran'];

    // Mencoba melakukan query untuk menyimpan data pembayaran
    $sql = "INSERT INTO pembayaran (id, customer_id, kamar_id, metode_pembayaran, catatan, tanggal_pembayaran) VALUES (?, ?, ?, ?, ?, ?)";
    
    $stmt = $db->prepare($sql);
    
    if ($stmt) {
        $stmt->bind_param("ssssss", $id, $customer_id, $kamar_id, $metode_pembayaran, $catatan, $tanggal_pembayaran);
        
        if ($stmt->execute()) {
            echo json_encode(array('status' => 'data_tersimpan', 'id' => $id));
        } else {
            echo json_encode(array('status' => 'gagal_tersimpan', 'error' => $stmt->error));
        }
        
        $stmt->close();
    } else {
        echo json_encode(array('status' => 'gagal_mempersiapkan_query', 'error' => $db->error));
    }

} else {
    echo json_encode(array('status' => 'data_tidak_lengkap', 'received' => $_POST));
}

mysqli_close($db);
?>
