<?php
$server = "localhost";
$user = "root";
$password = "";
$nama_db = "ehotelmobile";

// Membuat koneksi ke database
$conn = mysqli_connect($server, $user, $password, $nama_db);

// Memeriksa apakah koneksi berhasil
if (!$conn) {
    die("Koneksi gagal: " . mysqli_connect_error());
}
