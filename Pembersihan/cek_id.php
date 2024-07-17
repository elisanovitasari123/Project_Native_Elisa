<?php
include "../Pembersihan/koneksiP.php";
    // Ambil nilai dari POST
   
    $namaKamar = trim($_POST['namaKamar']);
    $nama_karyawan = $_POST['nama_karyawan'];
    $tanggal = $_POST['tanggal'];
    $deskripsi = $_POST['deskripsi'];


    

    
$db->close();
