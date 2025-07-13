-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:8889
-- Généré le : jeu. 02 jan. 2025 à 18:59
-- Version du serveur : 5.7.39
-- Version de PHP : 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `cli`
--

-- --------------------------------------------------------

--
-- Structure de la table `administrateurs`
--

CREATE TABLE `administrateurs` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) DEFAULT NULL,
  `prenom` varchar(100) DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  `matricule` varchar(100) DEFAULT NULL,
  `date_debut_service` date DEFAULT NULL,
  `poste` varchar(100) DEFAULT NULL,
  `mot_de_passe` varchar(255) DEFAULT NULL,
  `role` varchar(100) DEFAULT 'admin'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `administrateurs`
--

INSERT INTO `administrateurs` (`id`, `nom`, `prenom`, `date_naissance`, `matricule`, `date_debut_service`, `poste`, `mot_de_passe`, `role`) VALUES
(1, 'anas', 'ait', '1980-09-30', 'A10101', '2005-03-10', 'Administrateur Général', '123', 'admin'),
(2, 'Ben Issa', 'Sara', '1983-05-18', 'A10102', '2008-06-22', 'Administrateur de Service', '123', 'admin'),
(3, 'Al Mahdi', 'Abdellah', '1979-11-25', 'A10103', '2007-07-15', 'Chef Administratif', '123', 'admin');

-- --------------------------------------------------------

--
-- Structure de la table `infirmiers`
--

CREATE TABLE `infirmiers` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) DEFAULT NULL,
  `prenom` varchar(100) DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  `matricule` varchar(100) DEFAULT NULL,
  `date_debut_service` date DEFAULT NULL,
  `poste` varchar(100) DEFAULT NULL,
  `specialite` varchar(100) DEFAULT NULL,
  `mot_de_passe` varchar(255) DEFAULT NULL,
  `role` varchar(100) DEFAULT 'infirmiere'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `infirmiers`
--

INSERT INTO `infirmiers` (`id`, `nom`, `prenom`, `date_naissance`, `matricule`, `date_debut_service`, `poste`, `specialite`, `mot_de_passe`, `role`) VALUES
(1, 'aa', 'vv', '1988-12-25', 'I98766', '2016-01-18', 'vv', 'vv', '123', 'infirmiere'),
(8, 'hiba', 'ait', NULL, NULL, NULL, 'ldsa', 'dsd', '123', 'infirmiere'),
(12, 'w', 'DWv', NULL, NULL, '2024-12-26', 'rv', 'vw', 'wec', 'infirmiere'),
(15, 'as', 'ds', NULL, NULL, '2025-01-02', 'wef', 'we', 'q', 'infirmiere'),
(17, 'lina', 'yeder', NULL, NULL, '2025-01-02', 'perma', 'general', 'qmsfv', 'infirmiere');

--
-- Déclencheurs `infirmiers`
--
DELIMITER $$
CREATE TRIGGER `before_insert_infirmiers` BEFORE INSERT ON `infirmiers` FOR EACH ROW BEGIN
    IF NEW.date_debut_service IS NULL THEN
        SET NEW.date_debut_service = CURRENT_DATE;
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `before_insert_patient` BEFORE INSERT ON `infirmiers` FOR EACH ROW BEGIN
    IF NEW.date_debut_service IS NULL THEN
        SET NEW.date_debut_service = CURDATE();
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `medecins`
--

CREATE TABLE `medecins` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) DEFAULT NULL,
  `prenom` varchar(100) DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  `matricule` varchar(100) DEFAULT NULL,
  `date_debut_service` date DEFAULT NULL,
  `poste` varchar(100) DEFAULT 'medecin',
  `specialite` varchar(100) DEFAULT NULL,
  `mot_de_passe` varchar(255) DEFAULT NULL,
  `role` varchar(100) DEFAULT 'medecin'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `medecins`
--

INSERT INTO `medecins` (`id`, `nom`, `prenom`, `date_naissance`, `matricule`, `date_debut_service`, `poste`, `specialite`, `mot_de_passe`, `role`) VALUES
(3, 'Ben Said', 'Rachide', '1982-07-13', 'M12347', '2012-11-05', 'Cardiologue', 'Cardiologue', '123', 'medecin'),
(8, 'yassine', 'ait', NULL, NULL, NULL, 'chef', 'ardiologue', '123', 'medecin'),
(11, 'an', 'benr', NULL, NULL, NULL, 'sds', 'Endocrinologue', '123', 'medecin'),
(12, 'wwws', 'ws', NULL, NULL, NULL, '', 'Orthopédiste', 'qw2', 'medecin'),
(13, 'mounir', 'bel', NULL, NULL, NULL, 'chef', 'Cardiologue', '123m', 'medecin'),
(19, 'dvvqw', 'wv', NULL, NULL, '2024-12-26', 'vw', 'Endocrinologue', '1234', 'medecin'),
(26, 'larbi', 'lhresh', NULL, NULL, '2025-01-02', 'perma', 'Orthopédiste', '12345', 'medecin');

--
-- Déclencheurs `medecins`
--
DELIMITER $$
CREATE TRIGGER `before_insert_medecins` BEFORE INSERT ON `medecins` FOR EACH ROW BEGIN
    IF NEW.date_debut_service IS NULL THEN
        SET NEW.date_debut_service = CURRENT_DATE;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `patients`
--

CREATE TABLE `patients` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `date_naissance` date NOT NULL,
  `date_creation_dossier` date DEFAULT NULL,
  `maladies` text,
  `Telephone` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `patients`
--

INSERT INTO `patients` (`id`, `nom`, `prenom`, `date_naissance`, `date_creation_dossier`, `maladies`, `Telephone`, `email`) VALUES
(1, 'Dupont', 'Marien', '1985-06-15', '2024-12-22', 'Hypertension, Diabète', '0', 'xx@xx.com'),
(2, 'hmed', 'lhbil', '2024-12-17', '2024-12-17', 'corona', '334515704', 'dvw@slnvd.com'),
(3, 'wr', 'efv', '1934-12-23', '2024-12-28', '3rf', 'rgv2', 'wr@gmail.com');

--
-- Déclencheurs `patients`
--
DELIMITER $$
CREATE TRIGGER `before_insert_patients` BEFORE INSERT ON `patients` FOR EACH ROW BEGIN
    IF NEW.date_creation_dossier IS NULL THEN
        SET NEW.date_creation_dossier = CURDATE();
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `Receptionniste`
--

CREATE TABLE `Receptionniste` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  `matricule` varchar(255) DEFAULT NULL,
  `date_debut_service` date DEFAULT NULL,
  `poste` varchar(255) DEFAULT NULL,
  `mot_de_passe` varchar(255) DEFAULT NULL,
  `role` varchar(100) DEFAULT 'receptionniste'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `Receptionniste`
--

INSERT INTO `Receptionniste` (`id`, `nom`, `prenom`, `date_naissance`, `matricule`, `date_debut_service`, `poste`, `mot_de_passe`, `role`) VALUES
(2, 'Martine', 'Sophie', '1992-08-20', 'REC002', '2018-09-15', 'Assistant réceptionniste', '123', 'receptionniste'),
(3, 'jack', 'Paul', '1985-03-12', 'REC003', '2010-01-10', 'Réceptionniste', '1234', 'receptionniste');

--
-- Déclencheurs `Receptionniste`
--
DELIMITER $$
CREATE TRIGGER `before_insert_Receptionniste` BEFORE INSERT ON `Receptionniste` FOR EACH ROW BEGIN
    IF NEW.date_debut_service IS NULL THEN
        SET NEW.date_debut_service = CURRENT_DATE;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `reclamations`
--

CREATE TABLE `reclamations` (
  `id_reclamation` int(11) NOT NULL,
  `id_patient` int(11) NOT NULL,
  `id_medecin` int(11) DEFAULT NULL,
  `id_infirmier` int(11) DEFAULT NULL,
  `motif` varchar(255) NOT NULL,
  `validation_medecin` tinyint(1) DEFAULT '0',
  `date_depot` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `reclamations`
--

INSERT INTO `reclamations` (`id_reclamation`, `id_patient`, `id_medecin`, `id_infirmier`, `motif`, `validation_medecin`, `date_depot`) VALUES
(1, 1, 8, 8, 'maladie', 1, '2024-12-29'),
(2, 2, 8, 1, 'ds', 1, '2024-12-29'),
(4, 3, 8, 12, 'urg', 1, '2024-12-29'),
(7, 3, 8, 12, 'xxxxxx', 0, '2024-12-29'),
(9, 3, 8, 1, 'romati', 0, '2024-12-29'),
(10, 2, 13, 1, 'controle', 0, '2024-12-29'),
(11, 3, 13, 8, 'fd', 0, '2024-12-29'),
(12, 2, 13, 12, 'controle', 1, '2024-12-29'),
(13, 2, 13, 12, '3ss', 0, '2024-12-29'),
(14, 1, 13, 12, 'dsx', 0, '2024-12-29'),
(16, 3, 13, 8, 'eds', 1, '2024-12-29'),
(17, 2, 11, 1, 'maladie', 0, '2024-12-31');

--
-- Déclencheurs `reclamations`
--
DELIMITER $$
CREATE TRIGGER `before_insert_reclamation` BEFORE INSERT ON `reclamations` FOR EACH ROW BEGIN
    IF NEW.date_depot IS NULL THEN
        SET NEW.date_depot = CURDATE();
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `rendez_vous`
--

CREATE TABLE `rendez_vous` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `date_rendez_vous` date NOT NULL,
  `motif` varchar(255) DEFAULT NULL,
  `medecin_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `rendez_vous`
--

INSERT INTO `rendez_vous` (`id`, `patient_id`, `date_rendez_vous`, `motif`, `medecin_id`) VALUES
(1, 1, '2024-12-25', 'Consultation annuelle', 3),
(3, 2, '2024-12-04', 'ibola', 3),
(4, 2, '2024-12-26', 'ADHD', 8),
(5, 2, '2025-12-18', 'visite', 3),
(6, 1, '2026-03-05', 'maladie', 8),
(8, 1, '2025-12-13', 'xxx', 12),
(9, 2, '2026-12-29', 'xxx', 11),
(10, 2, '2024-12-30', 'ciati', 13);

--
-- Déclencheurs `rendez_vous`
--
DELIMITER $$
CREATE TRIGGER `before_insert_rendez_vous` BEFORE INSERT ON `rendez_vous` FOR EACH ROW BEGIN
    IF NEW.date_rendez_vous IS NULL THEN
        SET NEW.date_rendez_vous = CURRENT_DATE;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `test`
--

CREATE TABLE `test` (
  `id` varchar(20) NOT NULL,
  `nom` varchar(20) NOT NULL,
  `prenom` varchar(20) NOT NULL,
  `passwd` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `test`
--

INSERT INTO `test` (`id`, `nom`, `prenom`, `passwd`) VALUES
('1', 'Dupont', 'Jean', 'password123'),
('2', 'Durand', 'Marie', 'admin2024'),
('3', 'Smith', 'John', 'securePass'),
('4', 'admin', 'admin', '123'),
('4', 'admin', 'admin', '123'),
('4', 'admin', 'admin', '123');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `administrateurs`
--
ALTER TABLE `administrateurs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `matricule` (`matricule`);

--
-- Index pour la table `infirmiers`
--
ALTER TABLE `infirmiers`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `matricule` (`matricule`);

--
-- Index pour la table `medecins`
--
ALTER TABLE `medecins`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `matricule` (`matricule`);

--
-- Index pour la table `patients`
--
ALTER TABLE `patients`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nom` (`nom`,`prenom`,`date_naissance`);

--
-- Index pour la table `Receptionniste`
--
ALTER TABLE `Receptionniste`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `matricule` (`matricule`);

--
-- Index pour la table `reclamations`
--
ALTER TABLE `reclamations`
  ADD PRIMARY KEY (`id_reclamation`),
  ADD KEY `id_patient` (`id_patient`),
  ADD KEY `id_medecin` (`id_medecin`),
  ADD KEY `id_infirmier` (`id_infirmier`);

--
-- Index pour la table `rendez_vous`
--
ALTER TABLE `rendez_vous`
  ADD PRIMARY KEY (`id`),
  ADD KEY `patient_id` (`patient_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `administrateurs`
--
ALTER TABLE `administrateurs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `infirmiers`
--
ALTER TABLE `infirmiers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT pour la table `medecins`
--
ALTER TABLE `medecins`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT pour la table `patients`
--
ALTER TABLE `patients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `Receptionniste`
--
ALTER TABLE `Receptionniste`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `reclamations`
--
ALTER TABLE `reclamations`
  MODIFY `id_reclamation` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT pour la table `rendez_vous`
--
ALTER TABLE `rendez_vous`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `reclamations`
--
ALTER TABLE `reclamations`
  ADD CONSTRAINT `reclamations_ibfk_1` FOREIGN KEY (`id_patient`) REFERENCES `patients` (`id`),
  ADD CONSTRAINT `reclamations_ibfk_2` FOREIGN KEY (`id_medecin`) REFERENCES `medecins` (`id`),
  ADD CONSTRAINT `reclamations_ibfk_3` FOREIGN KEY (`id_infirmier`) REFERENCES `infirmiers` (`id`);

--
-- Contraintes pour la table `rendez_vous`
--
ALTER TABLE `rendez_vous`
  ADD CONSTRAINT `rendez_vous_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
