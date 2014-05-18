-- phpMyAdmin SQL Dump
-- version 4.1.4
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Dim 18 Mai 2014 à 16:14
-- Version du serveur :  5.6.15-log
-- Version de PHP :  5.5.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `raidzultats`
--
CREATE DATABASE IF NOT EXISTS `raidzultats` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `raidzultats`;

-- --------------------------------------------------------

--
-- Structure de la table `avoir`
--

CREATE TABLE IF NOT EXISTS `avoir` (
  `idMB` int(11) NOT NULL,
  `idEquipe` int(11) NOT NULL,
  `tempsTotalMalusBonus` time DEFAULT NULL,
  `idCompetition` int(11) NOT NULL,
  PRIMARY KEY (`idEquipe`,`idMB`,`idCompetition`),
  KEY `FK_Avoir_idEquipe` (`idEquipe`),
  KEY `idMB` (`idMB`),
  KEY `idCompetition` (`idCompetition`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `balise`
--

CREATE TABLE IF NOT EXISTS `balise` (
  `idBalise` double NOT NULL,
  `idCompetition` int(11) NOT NULL,
  PRIMARY KEY (`idBalise`,`idCompetition`),
  KEY `FK_competition_idCompetition` (`idCompetition`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `competition`
--

CREATE TABLE IF NOT EXISTS `competition` (
  `idCompetition` int(11) NOT NULL AUTO_INCREMENT,
  `nomCompetition` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`idCompetition`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=0 ;

-- --------------------------------------------------------

--
-- Structure de la table `doigt`
--

CREATE TABLE IF NOT EXISTS `doigt` (
  `idDoigt` double NOT NULL,
  `idCompetition` int(11) NOT NULL,
  PRIMARY KEY (`idDoigt`,`idCompetition`),
  KEY `idCompetition` (`idCompetition`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `epreuve`
--

CREATE TABLE IF NOT EXISTS `epreuve` (
  `idEpreuve` int(11) NOT NULL AUTO_INCREMENT,
  `nomEpreuve` varchar(30) DEFAULT NULL,
  `typeEpreuve` varchar(30) DEFAULT NULL,
  `difficulte` varchar(30) DEFAULT NULL,
  `dateHeureEpreuve` datetime DEFAULT NULL,
  `dureeEpreuve` time DEFAULT NULL,
  `idCompetition` int(11) NOT NULL,
  PRIMARY KEY (`idEpreuve`),
  KEY `FK_competition_idCompetition` (`idCompetition`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=0 ;

-- --------------------------------------------------------

--
-- Structure de la table `equipe`
--

CREATE TABLE IF NOT EXISTS `equipe` (
  `idEquipe` int(11) NOT NULL AUTO_INCREMENT,
  `nomEquipe` varchar(30) DEFAULT NULL,
  `nomGroupe` varchar(30) DEFAULT NULL,
  `typeDifficulte` varchar(30) DEFAULT NULL,
  `typeEquipe` varchar(30) DEFAULT NULL,
  `dossard` int(11) NOT NULL,
  `idCompetition` int(11) NOT NULL,
  PRIMARY KEY (`idEquipe`,`idCompetition`),
  KEY `idCompetition` (`idCompetition`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=0 ;

-- --------------------------------------------------------

--
-- Structure de la table `malusbonus`
--

CREATE TABLE IF NOT EXISTS `malusbonus` (
  `idMB` int(11) NOT NULL AUTO_INCREMENT,
  `nomMalusBonus` varchar(30) NOT NULL,
  `malus` tinyint(1) DEFAULT NULL,
  `tempsMalusBonus` time DEFAULT NULL,
  `idCompetition` int(11) NOT NULL,
  PRIMARY KEY (`idMB`),
  KEY `idCompetition` (`idCompetition`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=0 ;

-- --------------------------------------------------------

--
-- Structure de la table `pointer`
--

CREATE TABLE IF NOT EXISTS `pointer` (
  `idBalise` double NOT NULL,
  `idEpreuve` int(11) NOT NULL,
  `idDoigt` double NOT NULL,
  `dateHeurePointage` datetime DEFAULT NULL,
  `idCompetition` int(11) NOT NULL,
  PRIMARY KEY (`idBalise`,`idEpreuve`,`idDoigt`,`idCompetition`),
  KEY `FK_Pointer_idEpreuve` (`idEpreuve`),
  KEY `FK_Pointer_idDoigt` (`idDoigt`),
  KEY `idCompetition` (`idCompetition`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `posséder`
--

CREATE TABLE IF NOT EXISTS `posséder` (
  `idDoigt` double NOT NULL,
  `idEquipe` int(11) NOT NULL,
  `dateHeureAttribution` datetime DEFAULT NULL,
  `idCompetition` int(11) NOT NULL,
  PRIMARY KEY (`idDoigt`,`idEquipe`,`idCompetition`),
  KEY `FK_Posséder_idEquipe` (`idEquipe`),
  KEY `idCompetition` (`idCompetition`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `scorer`
--

CREATE TABLE IF NOT EXISTS `scorer` (
  `idEquipe` int(11) NOT NULL AUTO_INCREMENT,
  `idEpreuve` int(11) NOT NULL,
  `tempsRealise` time DEFAULT NULL,
  `idCompetition` int(11) NOT NULL,
  PRIMARY KEY (`idEquipe`,`idEpreuve`,`idCompetition`),
  KEY `FK_Scorer_idEpreuve` (`idEpreuve`),
  KEY `idCompetition` (`idCompetition`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=0 ;

-- --------------------------------------------------------

--
-- Structure de la table `valoir`
--

CREATE TABLE IF NOT EXISTS `valoir` (
  `idBalise` double NOT NULL,
  `idEpreuve` int(11) NOT NULL,
  `type` varchar(30) NOT NULL,
  `valeurBalise` int(11) DEFAULT NULL,
  `idCompetition` int(11) NOT NULL,
  PRIMARY KEY (`idBalise`,`idEpreuve`,`idCompetition`),
  KEY `FK_Valoir_idEpreuve` (`idEpreuve`),
  KEY `idCompetition` (`idCompetition`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `avoir`
--
ALTER TABLE `avoir`
  ADD CONSTRAINT `avoir_ibfk_3` FOREIGN KEY (`idCompetition`) REFERENCES `competition` (`idCompetition`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `avoir_ibfk_1` FOREIGN KEY (`idEquipe`) REFERENCES `equipe` (`idEquipe`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `avoir_ibfk_2` FOREIGN KEY (`idMB`) REFERENCES `malusbonus` (`idMB`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `balise`
--
ALTER TABLE `balise`
  ADD CONSTRAINT `balise_ibfk_1` FOREIGN KEY (`idCompetition`) REFERENCES `competition` (`idCompetition`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `doigt`
--
ALTER TABLE `doigt`
  ADD CONSTRAINT `doigt_ibfk_1` FOREIGN KEY (`idCompetition`) REFERENCES `competition` (`idCompetition`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `epreuve`
--
ALTER TABLE `epreuve`
  ADD CONSTRAINT `epreuve_ibfk_1` FOREIGN KEY (`idCompetition`) REFERENCES `competition` (`idCompetition`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `equipe`
--
ALTER TABLE `equipe`
  ADD CONSTRAINT `equipe_ibfk_1` FOREIGN KEY (`idCompetition`) REFERENCES `competition` (`idCompetition`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `malusbonus`
--
ALTER TABLE `malusbonus`
  ADD CONSTRAINT `malusbonus_ibfk_2` FOREIGN KEY (`idCompetition`) REFERENCES `competition` (`idCompetition`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `pointer`
--
ALTER TABLE `pointer`
  ADD CONSTRAINT `pointer_ibfk_3` FOREIGN KEY (`idDoigt`) REFERENCES `doigt` (`idDoigt`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `pointer_ibfk_1` FOREIGN KEY (`idBalise`) REFERENCES `balise` (`idBalise`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `pointer_ibfk_2` FOREIGN KEY (`idEpreuve`) REFERENCES `epreuve` (`idEpreuve`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `pointer_ibfk_4` FOREIGN KEY (`idCompetition`) REFERENCES `competition` (`idCompetition`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `posséder`
--
ALTER TABLE `posséder`
  ADD CONSTRAINT `posséder_ibfk_1` FOREIGN KEY (`idDoigt`) REFERENCES `doigt` (`idDoigt`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `posséder_ibfk_2` FOREIGN KEY (`idEquipe`) REFERENCES `equipe` (`idEquipe`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `posséder_ibfk_3` FOREIGN KEY (`idCompetition`) REFERENCES `competition` (`idCompetition`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `scorer`
--
ALTER TABLE `scorer`
  ADD CONSTRAINT `scorer_ibfk_3` FOREIGN KEY (`idCompetition`) REFERENCES `competition` (`idCompetition`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `scorer_ibfk_1` FOREIGN KEY (`idEquipe`) REFERENCES `equipe` (`idEquipe`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `scorer_ibfk_2` FOREIGN KEY (`idEpreuve`) REFERENCES `epreuve` (`idEpreuve`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `valoir`
--
ALTER TABLE `valoir`
  ADD CONSTRAINT `valoir_ibfk_1` FOREIGN KEY (`idBalise`) REFERENCES `balise` (`idBalise`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `valoir_ibfk_2` FOREIGN KEY (`idEpreuve`) REFERENCES `epreuve` (`idEpreuve`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `valoir_ibfk_3` FOREIGN KEY (`idCompetition`) REFERENCES `competition` (`idCompetition`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
