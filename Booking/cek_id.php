<?php
include "../Booking/koneksiBooking.php";
    // Ambil nilai dari POST
    $nama = trim($_POST['nama']);
    $namaKamar = trim($_POST['namaKamar']);
    $check_in_date = $_POST['check_in_date'];
    $check_out_date = $_POST['check_out_date'];
    $total_price = $_POST['total_price'];


    

    
$db->close();
