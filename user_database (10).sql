-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 21, 2024 at 03:24 PM
-- Server version: 10.4.18-MariaDB
-- PHP Version: 8.0.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `user_database`
--

-- --------------------------------------------------------

--
-- Table structure for table `bmi_records`
--

CREATE TABLE `bmi_records` (
  `id` int(11) NOT NULL,
  `nis` varchar(20) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `berat` decimal(5,2) NOT NULL,
  `tinggi` int(11) NOT NULL,
  `bmi` decimal(5,2) NOT NULL,
  `kategori` varchar(50) NOT NULL,
  `rekomendasi` text NOT NULL,
  `tanggal_pemeriksaan` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `bmi_records`
--

INSERT INTO `bmi_records` (`id`, `nis`, `nama`, `berat`, `tinggi`, `bmi`, `kategori`, `rekomendasi`, `tanggal_pemeriksaan`) VALUES
(12, '123456789', 'Nanda Rayhan', '65.00', 170, '22.49', 'Berat Badan Normal', 'Anda berada dalam kategori berat badan yang normal. Pertahankan pola makan sehat dan olahraga teratur.', '2024-12-04 08:04:13');

-- --------------------------------------------------------

--
-- Table structure for table `edukasi_kesehatan`
--

CREATE TABLE `edukasi_kesehatan` (
  `id` int(11) NOT NULL,
  `judul` varchar(255) NOT NULL,
  `konten` text NOT NULL,
  `gambar` varchar(255) DEFAULT NULL,
  `video` varchar(255) DEFAULT NULL,
  `tanggal_dibuat` timestamp NOT NULL DEFAULT current_timestamp(),
  `video_file` varchar(255) DEFAULT NULL,
  `user_id` int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `edukasi_kesehatan`
--

INSERT INTO `edukasi_kesehatan` (`id`, `judul`, `konten`, `gambar`, `video`, `tanggal_dibuat`, `video_file`, `user_id`) VALUES
(51, 'Jarang Olahraga? Lakukan 10 Menit Stretching Full Body ini!', 'Hai FitSKWAD! Kamu sadar nggak sih kalau stretching fullbody itu banyak banget manfaatnya untuk tubuh kamu? Selain untuk melatih fleksibilitas dan memperbaiki postur tubuh, stretching fullbody juga bisa mengurangi stres, lho. Nggak perlu lama-lama kok, cukup 10 menit aja dengan ngikutin gerakan-gerakan di sepanjang video di atas. Jangan lupa untuk nonton sampe abis ya, karena akan ada beberapa tips yang bisa kamu dapetin!', 'uploads/Screenshot 2024-12-04 150800.png', 'https://youtu.be/pi5WhX24uS4?si=cyrUxbQijRhGVl5-', '2024-12-04 08:08:47', '', 13),
(59, 'Edukasi Mengenal Diabetes Melitus', 'informasi yg disampaikan mengenai penyebab dan gejala diabetes melitus secara ringkas, jelas dan padat dengan desain animasi yg menarik membuat audiens yg melihat tidak bosan dan informasi yg mudah dipahami hal ini bagus agar kita selalu aware terhadap kesehatan tubuh kita sendiri', 'uploads/Screenshot 2024-12-20 190151.png', 'https://www.youtube.com/watch?v=ypmoHwmLPF0', '2024-12-20 12:02:03', '', 13);

-- --------------------------------------------------------

--
-- Table structure for table `inventaris_uks`
--

CREATE TABLE `inventaris_uks` (
  `id_barang` int(11) NOT NULL,
  `nama_barang` varchar(255) NOT NULL,
  `kategori` varchar(100) DEFAULT NULL,
  `jumlah` int(11) NOT NULL,
  `satuan` varchar(50) DEFAULT NULL,
  `tanggal_masuk` date DEFAULT NULL,
  `tanggal_kadaluarsa` date DEFAULT NULL,
  `kondisi` enum('Baik','Rusak','Habis') DEFAULT 'Baik',
  `lokasi` varchar(100) DEFAULT NULL,
  `keterangan` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `inventaris_uks`
--

INSERT INTO `inventaris_uks` (`id_barang`, `nama_barang`, `kategori`, `jumlah`, `satuan`, `tanggal_masuk`, `tanggal_kadaluarsa`, `kondisi`, `lokasi`, `keterangan`) VALUES
(17, 'tempat tidur', 'Perlengkapan', 3, 'Pcs', '2024-12-04', NULL, 'Baik', 'Rak 1', 'kasur tempat nyaman');

-- --------------------------------------------------------

--
-- Table structure for table `monitoringkesehatan`
--

CREATE TABLE `monitoringkesehatan` (
  `id` int(11) NOT NULL,
  `tanggal` datetime DEFAULT current_timestamp(),
  `nama` varchar(100) NOT NULL,
  `nis` varchar(100) DEFAULT NULL,
  `kelas` varchar(50) NOT NULL,
  `keluhan` text DEFAULT NULL,
  `diagnosis` text DEFAULT NULL,
  `pertolongan_pertama` text DEFAULT NULL,
  `suhu` float DEFAULT NULL,
  `status` varchar(10) NOT NULL,
  `pengingat_id` int(15) NOT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `monitoringkesehatan`
--

INSERT INTO `monitoringkesehatan` (`id`, `tanggal`, `nama`, `nis`, `kelas`, `keluhan`, `diagnosis`, `pertolongan_pertama`, `suhu`, `status`, `pengingat_id`, `user_id`) VALUES
(123456897, '2024-12-16 01:14:17', 'Musa Habibullah', '123434242', '11', 'sakit perut', 'maag', 'istirahat', 36, 'Sakit', 149, 41);

-- --------------------------------------------------------

--
-- Table structure for table `penghubung`
--

CREATE TABLE `penghubung` (
  `id` int(11) NOT NULL,
  `patient_id` varchar(20) NOT NULL,
  `device_token` varchar(255) NOT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `pengingatobat`
--

CREATE TABLE `pengingatobat` (
  `id` int(11) NOT NULL,
  `patient_id` varchar(20) NOT NULL,
  `condition_name` varchar(255) NOT NULL,
  `severity` varchar(50) NOT NULL,
  `time_intervals` varchar(255) DEFAULT NULL,
  `monitoring_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `nama_obat` varchar(255) NOT NULL,
  `jumlah` int(11) DEFAULT 1,
  `notification_date` text DEFAULT NULL,
  `description` text DEFAULT NULL,
  `waktu_pengingat` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pengingatobat`
--

INSERT INTO `pengingatobat` (`id`, `patient_id`, `condition_name`, `severity`, `time_intervals`, `monitoring_id`, `user_id`, `nama_obat`, `jumlah`, `notification_date`, `description`, `waktu_pengingat`) VALUES
(149, '123434242', 'maag', 'Medium', '20:19', NULL, 41, 'bodrex', 1, '2024-12-20', 'Saatnya Minum Obat :)', '00:00:00'),
(150, 'AUTO', 'AUTO', 'Normal', NULL, NULL, NULL, 'raffi', 1, NULL, NULL, '01:38:00');

-- --------------------------------------------------------

--
-- Table structure for table `rekam_kesehatan`
--

CREATE TABLE `rekam_kesehatan` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `nis` varchar(20) NOT NULL,
  `keluhan` text NOT NULL,
  `diagnosis` text NOT NULL,
  `Pertolongan_Pertama` text NOT NULL,
  `tanggal` timestamp NOT NULL DEFAULT current_timestamp(),
  `user_id` int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `rekam_kesehatan`
--

INSERT INTO `rekam_kesehatan` (`id`, `nama`, `nis`, `keluhan`, `diagnosis`, `Pertolongan_Pertama`, `tanggal`, `user_id`) VALUES
(123456870, 'Musa Habibullah', '123434242', 'sakit perut', 'maag', 'istirahat', '2024-12-15 18:14:17', 41);

-- --------------------------------------------------------

--
-- Table structure for table `siswa`
--

CREATE TABLE `siswa` (
  `id` int(11) NOT NULL,
  `Nama` text NOT NULL,
  `Kelas` text NOT NULL,
  `Suhu` double NOT NULL,
  `Status` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `stok_obat`
--

CREATE TABLE `stok_obat` (
  `id_stok` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `jumlah` int(10) NOT NULL,
  `dosis` varchar(100) NOT NULL,
  `diperbarui` date NOT NULL,
  `tanggal_kadaluarsa` varchar(20) NOT NULL,
  `id_pengingat` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `stok_obat`
--

INSERT INTO `stok_obat` (`id_stok`, `nama`, `jumlah`, `dosis`, `diperbarui`, `tanggal_kadaluarsa`, `id_pengingat`) VALUES
(16, 'bodrex', 20, '3X1', '2024-12-05', '2024-12-28', NULL),
(18, 'Panadol', 18, '2x1', '2024-12-04', '2024-12-02', NULL),
(19, 'raffi', 11, '2x1', '2024-12-16', '2024-12-17', 150);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `foto` varchar(255) DEFAULT NULL,
  `nama_lengkap` varchar(100) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `nis` varchar(20) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `telepon` varchar(20) DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `tanggal_lahir` date DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(10) NOT NULL DEFAULT 'siswa',
  `kelas` varchar(15) NOT NULL,
  `reset_password_otp` text NOT NULL,
  `reset_password_created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `foto`, `nama_lengkap`, `username`, `nis`, `email`, `telepon`, `alamat`, `tanggal_lahir`, `password`, `role`, `kelas`, `reset_password_otp`, `reset_password_created_at`) VALUES
(13, NULL, NULL, 'admin', NULL, 'admin@example.com', NULL, NULL, NULL, '0192023a7bbd73250516f069df18b500', 'admin', '', '', '2024-12-10 10:29:44'),
(23, 'avatar_Nanda.png', 'Nanda Rayhan', 'Nanda', '123456789', 'budinanda177@gmail.com', '081357510843', 'perum taman gading', '2005-04-21', '$2y$10$.ul0YDYKBFrwhY7tCYgrduKZkJc.gQ3VOxH7Iq/FrNKnM0jKTRq7q', 'siswa', '', '697941', '2024-12-19 06:07:43'),
(24, NULL, 'Nanda Rayhan', 'rafi', '123456', 'smamuhammadiyah@gmail.com', NULL, NULL, NULL, '7f69f8319fc83ccf5ac97bbcc38d64ac', 'siswa', '', '', '2024-12-10 10:29:44'),
(35, '', 'Bimasaka', 'Musa', '21321313', 'Bimasaka@gmail.com', '0852360095414', NULL, '2004-11-16', '$2y$10$wkgR.Qfohnv2AujRnErc..xb.9Qjp9M3pT1.P1Vmrh9Zsrt60ljUy', 'siswa', '12', '411157', '2024-12-14 12:00:41'),
(41, NULL, 'Musa Habibullah', 'bima', '123434242', 'zixuanwakashikoi@gmail.com', '082132477156', NULL, '2003-12-11', '$2y$10$HJHALspCWT44wMIYnHcCmODlwYaPRuo.jyG5k7M61QzKI2lwRgGYe', 'siswa', '12', '302856', '2024-12-14 10:50:20'),
(43, NULL, 'ananda', 'ananda', '188294945', 'budinanda177@gmail.com', '082132485938', NULL, '2007-12-29', '$2y$10$.ul0YDYKBFrwhY7tCYgrduKZkJc.gQ3VOxH7Iq/FrNKnM0jKTRq7q', 'siswa', '10', '697941', '2024-12-19 06:07:43'),
(45, NULL, 'musa habibulloh', 'musaa', '13132123', 'novaforge3danimation@gmail.com', '082132384738', NULL, '2003-12-20', '$2a$10$Grcg5XSrzyJkk9MagIh/O.VczyAkEUMDZ2lreEDcmiSAjOxGdOece', 'siswa', '11', '823865', '2024-12-20 05:22:22');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bmi_records`
--
ALTER TABLE `bmi_records`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `edukasi_kesehatan`
--
ALTER TABLE `edukasi_kesehatan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `inventaris_uks`
--
ALTER TABLE `inventaris_uks`
  ADD PRIMARY KEY (`id_barang`);

--
-- Indexes for table `monitoringkesehatan`
--
ALTER TABLE `monitoringkesehatan`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nis` (`nis`),
  ADD KEY `user_id` (`pengingat_id`),
  ADD KEY `fk_monitoring_user` (`user_id`);

--
-- Indexes for table `penghubung`
--
ALTER TABLE `penghubung`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id_2` (`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `pengingatobat`
--
ALTER TABLE `pengingatobat`
  ADD PRIMARY KEY (`id`),
  ADD KEY `monitoring_id` (`monitoring_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `rekam_kesehatan`
--
ALTER TABLE `rekam_kesehatan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `siswa`
--
ALTER TABLE `siswa`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `stok_obat`
--
ALTER TABLE `stok_obat`
  ADD PRIMARY KEY (`id_stok`),
  ADD UNIQUE KEY `id_pengingat` (`id_pengingat`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nis` (`nis`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bmi_records`
--
ALTER TABLE `bmi_records`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `edukasi_kesehatan`
--
ALTER TABLE `edukasi_kesehatan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;

--
-- AUTO_INCREMENT for table `inventaris_uks`
--
ALTER TABLE `inventaris_uks`
  MODIFY `id_barang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `monitoringkesehatan`
--
ALTER TABLE `monitoringkesehatan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=123456898;

--
-- AUTO_INCREMENT for table `penghubung`
--
ALTER TABLE `penghubung`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pengingatobat`
--
ALTER TABLE `pengingatobat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=151;

--
-- AUTO_INCREMENT for table `rekam_kesehatan`
--
ALTER TABLE `rekam_kesehatan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=123456871;

--
-- AUTO_INCREMENT for table `stok_obat`
--
ALTER TABLE `stok_obat`
  MODIFY `id_stok` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `edukasi_kesehatan`
--
ALTER TABLE `edukasi_kesehatan`
  ADD CONSTRAINT `edukasi_kesehatan_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `monitoringkesehatan`
--
ALTER TABLE `monitoringkesehatan`
  ADD CONSTRAINT `fk_monitoring_pengingat` FOREIGN KEY (`pengingat_id`) REFERENCES `pengingatobat` (`id`),
  ADD CONSTRAINT `fk_monitoring_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `pengingatobat`
--
ALTER TABLE `pengingatobat`
  ADD CONSTRAINT `pengingatobat_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `rekam_kesehatan`
--
ALTER TABLE `rekam_kesehatan`
  ADD CONSTRAINT `rekam_kesehatan_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `stok_obat`
--
ALTER TABLE `stok_obat`
  ADD CONSTRAINT `stok_obat_ibfk_1` FOREIGN KEY (`id_pengingat`) REFERENCES `pengingatobat` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
