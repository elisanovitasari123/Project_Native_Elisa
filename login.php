<?php
include 'dbconfig.php';
$conn = mysqli_connect($host, $user, $pw, $db);

// Memastikan respons dalam format JSON
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Mendapatkan nilai
    $username = $_POST['NamaUser'];
    $password = $_POST['Pass'];


    // Log untuk debugging
    error_log("Login attempt with Username: $username and Password: $password");

    // Membuat query SQL
    $sql = "SELECT * FROM user WHERE username='$username' AND pass='$password'";
    $result = mysqli_query($conn, $sql);

    // Memeriksa hasil query
    if (mysqli_num_rows($result) > 0) {
        // Jika login berhasil
        $response = array("status" => "success", "message" => "Login berhasil");
    } else {
        // Jika login gagal
        $response = array("status" => "failure", "message" => "Nama User atau Password SALAH");
    }

    // Menutup koneksi
    mysqli_close($conn);

    // Mengembalikan respons JSON
    echo json_encode($response);
} else {
    echo json_encode(array("status" => "error", "message" => "Invalid Request"));
}
?>
