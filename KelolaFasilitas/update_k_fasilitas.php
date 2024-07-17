<?php
require_once 'koneksiFasilitas.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Pastikan semua kunci yang diperlukan ada dalam array POST
    $required_keys = ['id_k_fasilitas', 'namaVilla', 'nama_fasilitas', 'namaKamar', 'status_fasilitas'];
    foreach ($required_keys as $key) {
        if (!isset($_POST[$key])) {
            echo json_encode(array('status' => 'error', 'message' => "Key '$key' tidak ditemukan dalam request"));
            exit;
        }
    }

    // Ambil nilai dari POST
    $id_k_fasilitas = intval($_POST['id_k_fasilitas']);
    $namaVilla = trim($_POST['namaVilla']);
    $nama_fasilitas = trim($_POST['nama_fasilitas']);
    $namaKamar = trim($_POST['namaKamar']);
    $status_fasilitas = $_POST['status_fasilitas'];

    // Mendapatkan id berdasarkan nama dari tabel villa
    $sql_villa = "SELECT id_villa FROM villa WHERE BINARY namaVilla = ?";
    $stmt_villa = $conn->prepare($sql_villa);
    if ($stmt_villa === false) {
        echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
        exit;
    }
    $stmt_villa->bind_param('s', $namaVilla);
    $stmt_villa->execute();
    $result_villa = $stmt_villa->get_result();

    // Mendapatkan id berdasarkan nama dari tabel fasilitas
    $sql_fasilitas = "SELECT id_fasilitas FROM fasilitas WHERE BINARY nama_fasilitas = ?";
    $stmt_fasilitas = $conn->prepare($sql_fasilitas);
    if ($stmt_fasilitas === false) {
        echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
        exit;
    }
    $stmt_fasilitas->bind_param('s', $nama_fasilitas);
    $stmt_fasilitas->execute();
    $result_fasilitas = $stmt_fasilitas->get_result();

    // Mendapatkan roomid berdasarkan namaKamar dari tabel room
    $sql_room = "SELECT roomid FROM room WHERE BINARY namaKamar = ?";
    $stmt_room = $conn->prepare($sql_room);
    if ($stmt_room === false) {
        echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
        exit;
    }
    $stmt_room->bind_param('s', $namaKamar);
    $stmt_room->execute();
    $result_room = $stmt_room->get_result();

    if ($result_villa->num_rows > 0 && $result_fasilitas->num_rows > 0 && $result_room->num_rows > 0) {
        $villa_data = $result_villa->fetch_assoc();
        $fasilitas_data = $result_fasilitas->fetch_assoc();
        $room_data = $result_room->fetch_assoc();
        $id_villa = $villa_data['id_villa'];
        $id_fasilitas = $fasilitas_data['id_fasilitas'];
        $roomid = $room_data['roomid'];

        // Update data di tabel kelolaFasilitas
        $sql_update = "UPDATE kelolaFasilitas SET status_fasilitas = ? WHERE id_k_fasilitas = ?";
        $stmt_update = $conn->prepare($sql_update);
        if ($stmt_update === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
            exit;
        }
        $stmt_update->bind_param('si', $status_fasilitas, $id_k_fasilitas);

        if ($stmt_update->execute()) {
            if ($stmt_update->affected_rows > 0) {
                echo json_encode(array('status' => 'success', 'message' => 'Data berhasil diperbarui'));
            } else {
                echo json_encode(array('status' => 'error', 'message' => 'Tidak ada perubahan dilakukan pada data'));
            }
        } else {
            echo json_encode(array('status' => 'error', 'message' => 'Update gagal: ' . $stmt_update->error));
        }
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Nama villa, fasilitas, atau kamar tidak ditemukan di tabel terkait'));
    }
} else {
    echo json_encode(array('status' => 'error', 'message' => 'Permintaan tidak valid'));
}

// Tutup koneksi
$conn->close();
?>
