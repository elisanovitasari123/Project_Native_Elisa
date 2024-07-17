<?php
require_once 'koneksiP.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Pastikan semua kunci yang diperlukan ada dalam array POST
    $required_keys = ['id_p', 'namaKamar', 'nama_karyawan', 'tanggal', 'deskripsi'];
    foreach ($required_keys as $key) {
        if (!isset($_POST[$key])) {
            echo json_encode(array('status' => 'error', 'message' => "Key '$key' tidak ditemukan dalam request"));
            exit;
        }
    }

    // Ambil nilai dari POST
    $id_p = intval($_POST['id_p']);
    $namaKamar = trim($_POST['namaKamar']);
    $nama_karyawan = trim($_POST['nama_karyawan']);
    $tanggal = $_POST['tanggal'];
    $deskripsi = $_POST['deskripsi'];

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

    // Mendapatkan id_karyawan berdasarkan nama_karyawan dari tabel karyawan
    $sql_karyawan = "SELECT id_karyawan FROM karyawan WHERE BINARY nama_karyawan = ?";
    $stmt_karyawan = $conn->prepare($sql_karyawan);
    if ($stmt_karyawan === false) {
        echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
        exit;
    }
    $stmt_karyawan->bind_param('s', $nama_karyawan);
    $stmt_karyawan->execute();
    $result_karyawan = $stmt_karyawan->get_result();

    if ($result_room->num_rows > 0 && $result_karyawan->num_rows > 0) {
        $room_data = $result_room->fetch_assoc();
        $karyawan_data = $result_karyawan->fetch_assoc();
        $roomid = $room_data['roomid'];
        $id_karyawan = $karyawan_data['id_karyawan'];

        // Memastikan id dan roomid unik dalam tabel pembersihan
        $sql_unique = "SELECT COUNT(*) as count FROM pembersihan WHERE roomid = ? AND id_karyawan = ? AND id_p != ?";
        $stmt_unique = $conn->prepare($sql_unique);
        if ($stmt_unique === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
            exit;
        }
        $stmt_unique->bind_param('iii', $roomid, $id_karyawan, $id_p);
        $stmt_unique->execute();
        $result_unique = $stmt_unique->get_result();
        $unique_data = $result_unique->fetch_assoc();

        if ($unique_data['count'] > 0) {
            echo json_encode(array('status' => 'error', 'message' => 'Data sudah ada di tabel pembersihan'));
            exit;
        }

        // Update data di tabel pembersihan
        $sql_update = "UPDATE pembersihan SET roomid = ?, id_karyawan = ?, tanggal = ?, deskripsi = ? WHERE id_p = ?";
        $stmt_update = $conn->prepare($sql_update);
        if ($stmt_update === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
            exit;
        }
        $stmt_update->bind_param('iissi', $roomid, $id_karyawan, $tanggal, $deskripsi, $id_p);

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
        echo json_encode(array('status' => 'error', 'message' => 'Nama karyawan atau nama kamar tidak ditemukan di tabel terkait'));
    }
} else {
    echo json_encode(array('status' => 'error', 'message' => 'Permintaan tidak valid'));
}

$conn->close();
?>
