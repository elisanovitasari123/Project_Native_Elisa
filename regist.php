<?php
include 'dbconfig.php';
$conn = mysqli_connect($host, $user, $pw, $db);

// Memastikan respons dalam format JSON
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Mendapatkan nilai
    $username = $_POST['NamaUser'];
    $password = $_POST['Pass'];

    // Escape input untuk mencegah SQL injection
    $username = mysqli_real_escape_string($conn, $username);
    $password = mysqli_real_escape_string($conn, $password);

    // Membuat query SQL
    $sql = "INSERT INTO user (username, pass) VALUES ('$username', '$password')";

    // Menjalankan query
    if (mysqli_query($conn, $sql)) {
        // Jika registrasi berhasil
        $response = array("status" => "success", "message" => "Registrasi berhasil");
    } else {
        // Jika registrasi gagal
        $response = array("status" => "failure", "message" => "Registrasi gagal: " . mysqli_error($conn));
    }

    // Menutup koneksi
    mysqli_close($conn);

    // Mengembalikan respons JSON
    echo json_encode($response);
} else {
    echo json_encode(array("status" => "error", "message" => "Invalid Request"));
}
