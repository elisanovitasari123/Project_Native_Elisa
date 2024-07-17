-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 17, 2024 at 05:17 PM
-- Server version: 10.1.30-MariaDB
-- PHP Version: 7.2.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ehotelmobile`
--

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `booking_id` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `roomid` int(11) NOT NULL,
  `check_in_date` date NOT NULL,
  `check_out_date` date NOT NULL,
  `total_price` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`booking_id`, `id`, `roomid`, `check_in_date`, `check_out_date`, `total_price`) VALUES
(12, 11, 14, '2024-07-14', '2024-07-17', '2500000.00'),
(13, 12, 15, '2024-07-14', '2024-07-27', '500000.00'),
(14, 13, 16, '2024-07-24', '2024-07-31', '99999999.99'),
(15, 12, 14, '2024-07-17', '2024-07-20', '200000.00');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `addres` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id`, `nama`, `phone`, `addres`) VALUES
(10, 'yaya azkita', '0899999', 'rawabening'),
(11, 'fahri Afrizal', '082222222', 'Panam'),
(12, 'Elisa Novita Sari', '081270240000', 'rawa bening'),
(13, 'rizky ananda', '083333333', 'air dingin'),
(14, 'aisyah', '0822355555', 'jl. pendidikan purwodadi');

-- --------------------------------------------------------

--
-- Table structure for table `fasilitas`
--

CREATE TABLE `fasilitas` (
  `id_fasilitas` int(11) NOT NULL,
  `nama_fasilitas` varchar(255) NOT NULL,
  `kategori` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fasilitas`
--

INSERT INTO `fasilitas` (`id_fasilitas`, `nama_fasilitas`, `kategori`) VALUES
(11, 'dua kasur', 'menengah'),
(12, 'ac', 'menengah'),
(13, 'kolam renang', 'mewah'),
(14, 'taman', 'mewah'),
(15, 'double kamar', 'mewah'),
(17, 'double kamar', 'standar'),
(18, 'Kolom Renang', 'Mewah');

-- --------------------------------------------------------

--
-- Table structure for table `kamar`
--

CREATE TABLE `kamar` (
  `id` int(10) NOT NULL,
  `kodehotel` varchar(8) NOT NULL,
  `typekamar` varchar(20) NOT NULL,
  `hargapermalam` int(9) NOT NULL,
  `tglcheckin` date NOT NULL,
  `tglcheckout` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `kamar`
--

INSERT INTO `kamar` (`id`, `kodehotel`, `typekamar`, `hargapermalam`, `tglcheckin`, `tglcheckout`) VALUES
(13, 'KMR009c0', 'Presidential', 2750000, '2021-02-11', '2021-02-15'),
(21, 'KMR01b4', 'Superior', 1850000, '2024-06-11', '2024-06-13'),
(30, '', 'Pilih Tipe Kamar', 0, '0000-00-00', '0000-00-00'),
(32, 'KMR08e9', 'Classic', 800000, '2024-06-18', '2024-06-20'),
(33, 'KMR0805', 'Classic', 800000, '2024-06-30', '2024-07-01');

-- --------------------------------------------------------

--
-- Table structure for table `karyawan`
--

CREATE TABLE `karyawan` (
  `id_karyawan` int(11) NOT NULL,
  `nama_karyawan` varchar(255) NOT NULL,
  `no_hp` varchar(15) NOT NULL,
  `alamat` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `karyawan`
--

INSERT INTO `karyawan` (`id_karyawan`, `nama_karyawan`, `no_hp`, `alamat`) VALUES
(11, 'fahri', '9373774', 'panam'),
(12, 'rizal', '838474', 'Sintong'),
(13, 'lisa', '94847', 'pasir'),
(14, 'novita', '8474849', 'rohul'),
(15, 'sari', '83848', 'rawa bening'),
(16, 'dian sari', '83848', 'rawa bening'),
(17, 'Elisa', '082222', 'rawa bening');

-- --------------------------------------------------------

--
-- Table structure for table `kelolafasilitas`
--

CREATE TABLE `kelolafasilitas` (
  `id_k_fasilitas` int(11) NOT NULL,
  `id_villa` int(11) NOT NULL,
  `id_fasilitas` int(11) NOT NULL,
  `roomid` int(11) NOT NULL,
  `status_fasilitas` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `kelolafasilitas`
--

INSERT INTO `kelolafasilitas` (`id_k_fasilitas`, `id_villa`, `id_fasilitas`, `roomid`, `status_fasilitas`) VALUES
(11, 4, 12, 13, 'di perbaiki'),
(12, 7, 13, 14, 'di booking'),
(13, 8, 13, 15, 'sudah cek out'),
(14, 8, 15, 16, 'sudah cek out'),
(15, 4, 15, 14, 'tersedia');

-- --------------------------------------------------------

--
-- Table structure for table `pembersihan`
--

CREATE TABLE `pembersihan` (
  `id_p` int(11) NOT NULL,
  `roomid` int(11) NOT NULL,
  `id_karyawan` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `deskripsi` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pembersihan`
--

INSERT INTO `pembersihan` (`id_p`, `roomid`, `id_karyawan`, `tanggal`, `deskripsi`) VALUES
(3, 13, 12, '2024-07-12', 'bersih kamar'),
(4, 14, 13, '2024-07-23', 'bersih rumah'),
(5, 16, 14, '2024-07-29', 'bersih kolam renang');

-- --------------------------------------------------------

--
-- Table structure for table `room`
--

CREATE TABLE `room` (
  `roomid` int(11) NOT NULL,
  `namaKamar` varchar(100) NOT NULL,
  `tipeKamar` varchar(50) NOT NULL,
  `harga` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`roomid`, `namaKamar`, `tipeKamar`, `harga`) VALUES
(12, 'presiden sweet', 'deluxe', '50.00'),
(13, 'umum', 'small', '5.00'),
(14, 'super sweet', 'king room', '100.00'),
(15, 'residensil', 'large', '80.00'),
(16, 'united', 'deluxe', '150.00'),
(17, 'cinta', 'deluxe', '150.00'),
(18, 'Melati', 'standar', '200000.00');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `username` varchar(25) NOT NULL,
  `pass` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `pass`) VALUES
(2, 'Elisa', 'elisa1234'),
(3, 'Admin', 'admin'),
(4, 'dimas', '123'),
(5, 'dimas', '123'),
(6, 'awang', '1'),
(34, 'fahri', '1'),
(37, 'aziz', '1'),
(38, 'Afrizal', '2'),
(39, 'rahel', '12'),
(40, 'aa', 'a'),
(41, 'asep', 'asep'),
(42, 'fadil', '1'),
(43, 'ac', '3'),
(44, 'hafis', '123'),
(45, 'fahri', '123');

-- --------------------------------------------------------

--
-- Table structure for table `villa`
--

CREATE TABLE `villa` (
  `id_villa` int(11) NOT NULL,
  `namaVilla` varchar(100) NOT NULL,
  `kontak` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `lokasi` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `villa`
--

INSERT INTO `villa` (`id_villa`, `namaVilla`, `kontak`, `email`, `lokasi`) VALUES
(2, 'sava', '08976544', 'elisa@gmail.com', 'rawa bening'),
(4, 'savana', '08976544', 'elisa@gmail.com', 'rawa bening'),
(7, 'bila ashari', '093736368', 'ashari@gmail.com', 'padang'),
(8, 'bila indah', '847474849', 'indah@gamail.com', 'jakarta'),
(9, 'bila indah', '847474849', 'indah@gamail.com', 'jakarta selatan'),
(10, 'Savana 1', '0837777', 'savana@gmail.com', 'purwodady'),
(11, 'Savana 2', '0837777', 'savana@gmail.com', 'purwodady'),
(12, 'Savana 5', '0837777', 'savana@gmail.com', 'purwodady');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`booking_id`),
  ADD KEY `booking_ibfk_1` (`id`),
  ADD KEY `booking_ibfk_2` (`roomid`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `fasilitas`
--
ALTER TABLE `fasilitas`
  ADD PRIMARY KEY (`id_fasilitas`);

--
-- Indexes for table `kamar`
--
ALTER TABLE `kamar`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `karyawan`
--
ALTER TABLE `karyawan`
  ADD PRIMARY KEY (`id_karyawan`);

--
-- Indexes for table `kelolafasilitas`
--
ALTER TABLE `kelolafasilitas`
  ADD PRIMARY KEY (`id_k_fasilitas`),
  ADD KEY `kelolafasilitas_ibfk_1` (`id_villa`),
  ADD KEY `kelolafasilitas_ibfk_2` (`id_fasilitas`),
  ADD KEY `kelolafasilitas_ibfk_3` (`roomid`);

--
-- Indexes for table `pembersihan`
--
ALTER TABLE `pembersihan`
  ADD PRIMARY KEY (`id_p`),
  ADD KEY `roomid` (`roomid`),
  ADD KEY `id_karyawan` (`id_karyawan`);

--
-- Indexes for table `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`roomid`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `villa`
--
ALTER TABLE `villa`
  ADD PRIMARY KEY (`id_villa`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `booking`
--
ALTER TABLE `booking`
  MODIFY `booking_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `fasilitas`
--
ALTER TABLE `fasilitas`
  MODIFY `id_fasilitas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `kamar`
--
ALTER TABLE `kamar`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `karyawan`
--
ALTER TABLE `karyawan`
  MODIFY `id_karyawan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `kelolafasilitas`
--
ALTER TABLE `kelolafasilitas`
  MODIFY `id_k_fasilitas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `pembersihan`
--
ALTER TABLE `pembersihan`
  MODIFY `id_p` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `room`
--
ALTER TABLE `room`
  MODIFY `roomid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- AUTO_INCREMENT for table `villa`
--
ALTER TABLE `villa`
  MODIFY `id_villa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`id`) REFERENCES `customer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `booking_ibfk_2` FOREIGN KEY (`roomid`) REFERENCES `room` (`roomid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `kelolafasilitas`
--
ALTER TABLE `kelolafasilitas`
  ADD CONSTRAINT `kelolafasilitas_ibfk_1` FOREIGN KEY (`id_villa`) REFERENCES `villa` (`id_villa`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `kelolafasilitas_ibfk_2` FOREIGN KEY (`id_fasilitas`) REFERENCES `fasilitas` (`id_fasilitas`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `kelolafasilitas_ibfk_3` FOREIGN KEY (`roomid`) REFERENCES `room` (`roomid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `pembersihan`
--
ALTER TABLE `pembersihan`
  ADD CONSTRAINT `pembersihan_ibfk_1` FOREIGN KEY (`roomid`) REFERENCES `room` (`roomid`),
  ADD CONSTRAINT `pembersihan_ibfk_2` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`id_karyawan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
