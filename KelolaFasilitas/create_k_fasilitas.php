<?php
require_once 'koneksiFasilitas.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Debugging: Tampilkan data POST yang diterima
    error_log(print_r($_POST, true));

    // Pastikan semua kunci yang diperlukan ada dalam array POST
    $required_keys = ['namaVilla', 'nama_fasilitas', 'namaKamar', 'status_fasilitas'];
    foreach ($required_keys as $key) {
        if (!isset($_POST[$key])) {
            echo json_encode(array('status' => 'error', 'message' => "Key '$key' tidak ditemukan dalam request"));
            exit;
        }
    }

    // Ambil nilai dari POST
    $namaVilla = trim($_POST['namaVilla']);
    $nama_fasilitas = trim($_POST['nama_fasilitas']);
    $namaKamar = trim($_POST['namaKamar']);
    $status_fasilitas = trim($_POST['status_fasilitas']);

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

        // Memastikan tidak ada entri duplikat di tabel kelolaFasilitas
        $sql_check = "SELECT COUNT(*) as count FROM kelolaFasilitas WHERE id_villa = ? AND id_fasilitas = ? AND roomid = ? AND status_fasilitas = ?";
        $stmt_check = $conn->prepare($sql_check);
        if ($stmt_check === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
            exit;
        }
        $stmt_check->bind_param('iiis', $id_villa, $id_fasilitas, $roomid, $status_fasilitas);
        $stmt_check->execute();
        $result_check = $stmt_check->get_result();
        $check_data = $result_check->fetch_assoc();

        if ($check_data['count'] > 0) {
            echo json_encode(array('status' => 'error', 'message' => 'Data sudah ada di tabel kelolaFasilitas'));
            exit;
        }

        // Simpan data ke tabel kelolaFasilitas
        $sql_insert = "INSERT INTO kelolaFasilitas (id_villa, id_fasilitas, roomid, status_fasilitas) VALUES (?, ?, ?, ?)";
        $stmt_insert = $conn->prepare($sql_insert);
        if ($stmt_insert === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
            exit;
        }
        $stmt_insert->bind_param('iiis', $id_villa, $id_fasilitas, $roomid, $status_fasilitas);

        if ($stmt_insert->execute()) {
            echo json_encode(array('status' => 'success', 'message' => 'Data berhasil disimpan'));
        } else {
            echo json_encode(array('status' => 'error', 'message' => 'Simpan gagal: ' . $stmt_insert->error));
        }
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Nama villa, fasilitas, atau kamar tidak ditemukan di tabel terkait'));
    }

    // Tutup semua pernyataan untuk melepaskan sumber daya
    $stmt_villa->close();
    $stmt_fasilitas->close();
    $stmt_room->close();
    $stmt_check->close();
    $stmt_insert->close();
} else {
    echo json_encode(array('status' => 'error', 'message' => 'Permintaan tidak valid'));
}

// Tutup koneksi
$conn->close();
?>
