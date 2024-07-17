<?php
require_once 'koneksiBooking.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Debugging: Tampilkan data POST yang diterima
    error_log(print_r($_POST, true));

    // Pastikan semua kunci yang diperlukan ada dalam array POST
    $required_keys = ['nama', 'namaKamar', 'check_in_date', 'check_out_date', 'total_price'];
    foreach ($required_keys as $key) {
        if (!isset($_POST[$key])) {
            echo json_encode(array('status' => 'error', 'message' => "Key '$key' tidak ditemukan dalam request"));
            exit;
        }
    }

    // Ambil nilai dari POST
    $nama = trim($_POST['nama']);
    $namaKamar = trim($_POST['namaKamar']);
    $check_in_date = trim($_POST['check_in_date']);
    $check_out_date = trim($_POST['check_out_date']);
    $total_price = trim($_POST['total_price']);

    // Mendapatkan id berdasarkan nama dari tabel customer
    $sql_customer = "SELECT id FROM customer WHERE BINARY nama = ?";
    $stmt_customer = $conn->prepare($sql_customer);
    if ($stmt_customer === false) {
        echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
        exit;
    }
    $stmt_customer->bind_param('s', $nama);
    $stmt_customer->execute();
    $result_customer = $stmt_customer->get_result();

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

    if ($result_customer->num_rows > 0 && $result_room->num_rows > 0) {
        $customer_data = $result_customer->fetch_assoc();
        $room_data = $result_room->fetch_assoc();
        $id = $customer_data['id'];
        $roomid = $room_data['roomid'];

        // Memastikan tidak ada entri duplikat di tabel booking
        $sql_check = "SELECT COUNT(*) as count FROM booking WHERE id = ? AND roomid = ? AND check_in_date = ? AND check_out_date = ?";
        $stmt_check = $conn->prepare($sql_check);
        if ($stmt_check === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
            exit;
        }
        $stmt_check->bind_param('iiss', $id, $roomid, $check_in_date, $check_out_date);
        $stmt_check->execute();
        $result_check = $stmt_check->get_result();
        $check_data = $result_check->fetch_assoc();

        if ($check_data['count'] > 0) {
            echo json_encode(array('status' => 'error', 'message' => 'Data sudah ada di tabel booking'));
            exit;
        }

        // Simpan data ke tabel booking
        $sql_insert = "INSERT INTO booking (id, roomid, check_in_date, check_out_date, total_price) VALUES (?, ?, ?, ?, ?)";
        $stmt_insert = $conn->prepare($sql_insert);
        if ($stmt_insert === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
            exit;
        }
        $stmt_insert->bind_param('iisss', $id, $roomid, $check_in_date, $check_out_date, $total_price);

        if ($stmt_insert->execute()) {
            echo json_encode(array('status' => 'success', 'message' => 'Data berhasil disimpan'));
        } else {
            echo json_encode(array('status' => 'error', 'message' => 'Simpan gagal: ' . $stmt_insert->error));
        }
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Nama customer atau nama kamar tidak ditemukan di tabel terkait'));
    }

    // Tutup semua pernyataan untuk melepaskan sumber daya
    $stmt_customer->close();
    $stmt_room->close();
    $stmt_check->close();
    $stmt_insert->close();
} else {
    echo json_encode(array('status' => 'error', 'message' => 'Permintaan tidak valid'));
}

// Tutup koneksi
$conn->close();
?>
