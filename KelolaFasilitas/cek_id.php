<?php
include "../KelolaFasilitas/koneksiFasilitas.php";
    // Ambil nilai dari POST
    $namaVilla = trim($_POST['namaVilla']);
    $nama_fasilitas = trim($_POST['nama_fasilitas']);
    $namaKamar = $_POST['namaKamar'];
    $status = $_POST['status'];
    


    

    
$db->close();
