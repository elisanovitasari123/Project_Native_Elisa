<?php
$host = "localhost";
$user = "root";
$pw = "";
$db = "ehotelmobile";
// Coba untuk koneksi ke database
$conn = mysqli_connect($host, $user, $pw, $db);

// Jika koneksi gagal, tampilkan pesan error dan hentikan eksekusi
if (!$conn) {
    die("Koneksi ke database gagal: " . mysqli_connect_error());
}
