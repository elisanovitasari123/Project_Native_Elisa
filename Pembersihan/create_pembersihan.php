<?php
require_once 'koneksiP.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Debugging: Tampilkan data POST yang diterima
    error_log(print_r($_POST, true));

    // Pastikan semua kunci yang diperlukan ada dalam array POST
    $required_keys = [ 'namaKamar', 'nama_karyawan', 'tanggal', 'deskripsi'];
    foreach ($required_keys as $key) {
        if (!isset($_POST[$key])) {
            echo json_encode(array('status' => 'error', 'message' => "Key '$key' tidak ditemukan dalam request"));
            exit;
        }
    }

    // Ambil nilai dari POST
   
    $namaKamar = trim($_POST['namaKamar']);
    $nama_karyawan = trim($_POST['nama_karyawan']);
    $tanggal = trim($_POST['tanggal']);
    $deskripsi = trim($_POST['deskripsi']);

    // Mendapatkan id berdasarkan nama dari tabel customer


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
        $id_karaywan = $karyawan_data['id_karyawan'];

        // Memastikan tidak ada entri duplikat di tabel booking
        $sql_check = "SELECT COUNT(*) as count FROM pembersihan WHERE roomid = ? AND id_karyawan = ? AND tanggal = ? AND deskripsi = ?";
        $stmt_check = $conn->prepare($sql_check);
        if ($stmt_check === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
            exit;
        }
        $stmt_check->bind_param('iiss', $id_karaywan, $roomid, $tanggal, $deskripsi);
        $stmt_check->execute();
        $result_check = $stmt_check->get_result();
        $check_data = $result_check->fetch_assoc();

        if ($check_data['count'] > 0) {
            echo json_encode(array('status' => 'error', 'message' => 'Data sudah ada di tabel booking'));
            exit;
        }

        // Simpan data ke tabel booking
        $sql_insert = "INSERT INTO pembersihan ( roomid, id_karyawan, tanggal, deskripsi) VALUES (?, ?, ?, ?)";
        $stmt_insert = $conn->prepare($sql_insert);
        if ($stmt_insert === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
            exit;
        }
        $stmt_insert->bind_param('iiss', $roomid, $id_karaywan, $tanggal, $deskripsi);

        if ($stmt_insert->execute()) {
            echo json_encode(array('status' => 'success', 'message' => 'Data berhasil disimpan'));
        } else {
            echo json_encode(array('status' => 'error', 'message' => 'Simpan gagal: ' . $stmt_insert->error));
        }
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Nama customer atau nama kamar tidak ditemukan di tabel terkait'));
    }

    // Tutup semua pernyataan untuk melepaskan sumber daya
    
    $stmt_room->close();
    $stmt_karyawan->close();
    $stmt_check->close();
    $stmt_insert->close();
} else {
    echo json_encode(array('status' => 'error', 'message' => 'Permintaan tidak valid'));
}

// Tutup koneksi
$conn->close();
?>
