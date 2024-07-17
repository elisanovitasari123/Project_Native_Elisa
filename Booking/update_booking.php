<?php
require_once 'koneksiBooking.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Pastikan semua kunci yang diperlukan ada dalam array POST
    $required_keys = ['booking_id', 'nama', 'namaKamar', 'check_in_date', 'check_out_date', 'total_price'];
    foreach ($required_keys as $key) {
        if (!isset($_POST[$key])) {
            echo json_encode(array('status' => 'error', 'message' => "Key '$key' tidak ditemukan dalam request"));
            exit;
        }
    }

    // Ambil nilai dari POST
    $booking_id = intval($_POST['booking_id']);
    $nama = trim($_POST['nama']);
    $namaKamar = trim($_POST['namaKamar']);
    $check_in_date = $_POST['check_in_date'];
    $check_out_date = $_POST['check_out_date'];
    $total_price = $_POST['total_price'];

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

        // Memastikan id dan roomid unik dalam tabel booking
        $sql_unique = "SELECT COUNT(*) as count FROM booking WHERE id = ? AND roomid = ? AND booking_id != ?";
        $stmt_unique = $conn->prepare($sql_unique);
        if ($stmt_unique === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
            exit;
        }
        // $stmt_unique->bind_param('iii', $id, $roomid, $booking_id);
        // $stmt_unique->execute();
        // $result_unique = $stmt_unique->get_result();
        // $unique_data = $result_unique->fetch_assoc();

        // if ($unique_data['count'] > 0) {
        //     echo json_encode(array('status' => 'error', 'message' => 'Data sudah ada di tabel booking'));
        //     exit;
        // }

        // Update data di tabel booking tanpa mengubah id dan roomid
        $sql_update = "UPDATE booking SET check_in_date = ?, check_out_date = ?, total_price = ? WHERE booking_id = ?";
        $stmt_update = $conn->prepare($sql_update);
        if ($stmt_update === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $conn->error));
            exit;
        }
        $stmt_update->bind_param('sssi', $check_in_date, $check_out_date, $total_price, $booking_id);

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
        echo json_encode(array('status' => 'error', 'message' => 'Nama customer atau nama kamar tidak ditemukan di tabel terkait'));
    }
} else {
    echo json_encode(array('status' => 'error', 'message' => 'Permintaan tidak valid'));
}

$conn->close();
?>
