CREATE Database IF NOT EXISTS sophodb;

USE sophodb;

CREATE TABLE IF NOT EXISTS `apothikiparadosi`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eponimo` varchar(45) DEFAULT NULL,
  `onoma` varchar(45) DEFAULT NULL,
  `patronimo` varchar(45) DEFAULT NULL,
  `dieuthinsi` varchar(45) DEFAULT NULL,
  `tilefono` varchar(45) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `antikeimeno` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `apothikiparalavi`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eponimo` varchar(45) DEFAULT NULL,
  `onoma` varchar(45) DEFAULT NULL,
  `patronimo` varchar(45) DEFAULT NULL,
  `dieuthinsi` varchar(245) DEFAULT NULL,
  `tilefono` varchar(45) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `antikeimeno` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `eidinames`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `eidiofeloumenoi`(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `barcode` varchar(45) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `eponimo` varchar(45) DEFAULT NULL,
  `onoma` varchar(45) DEFAULT NULL,
  `patronimo` varchar(45) DEFAULT NULL,
  `eidos1` int(11) DEFAULT NULL,
  `eidos2` int(11) DEFAULT NULL,
  `eidos3` int(11) DEFAULT NULL,
  `eidos4` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `filoksenoumenoi`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eponimo` varchar(45) DEFAULT NULL,
  `onoma` varchar(45) DEFAULT NULL,
  `patronimo` varchar(45) DEFAULT NULL,
  `dieuthinsi` varchar(245) DEFAULT NULL,
  `tilefono` varchar(45) DEFAULT NULL,
  `tautotita` varchar(45) DEFAULT NULL,
  `aitia` varchar(245) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `loipa` longtext,
  `dateApoxorisis` date DEFAULT NULL,
  `apoxorisi` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE IF NOT EXISTS TABLE images (
  id int(11) NOT NULL AUTO_INCREMENT,
  photoID varchar(45) DEFAULT NULL,
  image mediumblob,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `kathigites`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eponimo` varchar(45) DEFAULT NULL,
  `onoma` varchar(45) DEFAULT NULL,
  `patronimo` varchar(45) DEFAULT NULL,
  `dieuthinsi` varchar(245) DEFAULT NULL,
  `tilefono` varchar(45) DEFAULT NULL,
  `tilefono2` varchar(45) DEFAULT NULL,
  `mathimata` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `katoxiromenestheseis`(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ergodotisEponimia` varchar(45) DEFAULT NULL,
  `ergodotisEponimo` varchar(45) DEFAULT NULL,
  `ergodotisOnoma` varchar(45) DEFAULT NULL,
  `ergodotisPatronimo` varchar(45) DEFAULT NULL,
  `ergodotisDieuthinsi` varchar(245) DEFAULT NULL,
  `ergodotisTilefono` varchar(45) DEFAULT NULL,
  `eponimo` varchar(45) DEFAULT NULL,
  `onoma` varchar(45) DEFAULT NULL,
  `patronimo` varchar(45) DEFAULT NULL,
  `dieuthinsi` varchar(245) DEFAULT NULL,
  `tilefono` varchar(45) DEFAULT NULL,
  `thesi` varchar(45) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `mathites`(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `eponimo` varchar(45) DEFAULT NULL,
  `onoma` varchar(45) DEFAULT NULL,
  `patronimo` varchar(45) DEFAULT NULL,
  `dieuthinsi` varchar(245) DEFAULT NULL,
  `tilefono` varchar(45) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `mathimata` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `ofeloumenoi`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `barcode` varchar(45) NOT NULL,
  `eponimo` varchar(45) DEFAULT NULL,
  `onoma` varchar(45) DEFAULT NULL,
  `patronimo` varchar(45) DEFAULT NULL,
  `mitronimo` varchar(45) DEFAULT NULL,
  `imGennisis` date DEFAULT NULL,
  `dieuthinsi` varchar(245) DEFAULT NULL,
  `dimos` varchar(45) DEFAULT NULL,
  `tilefono` varchar(45) DEFAULT NULL,
  `anergos` int(11) DEFAULT NULL,
  `epaggelma` varchar(45) DEFAULT NULL,
  `eisodima` varchar(45) DEFAULT NULL,
  `eksartiseis` varchar(45) DEFAULT NULL,
  `photoID` varchar(45) DEFAULT NULL,
  `afm` varchar(45) DEFAULT NULL,
  `tautotita` varchar(45) DEFAULT NULL,
  `ethnikotita` varchar(45) DEFAULT NULL,
  `metanastis` int(11) DEFAULT NULL,
  `roma` int(11) DEFAULT NULL,
  `oikKatastasi` int(11) DEFAULT NULL,
  `hasTekna` int(11) DEFAULT NULL,
  `arithmosTeknon` int(11) DEFAULT NULL,
  `ilikiesTeknon` longtext,
  `politeknos` int(11) DEFAULT NULL,
  `monogoneiki` int(11) DEFAULT NULL,
  `mellousaMama` int(11) DEFAULT NULL,
  `amea` int(11) DEFAULT NULL,
  `asfForeas` int(11) DEFAULT NULL,
  `xronios` int(11) DEFAULT NULL,
  `pathisi` varchar(45) DEFAULT NULL,
  `anoTon60` int(11) DEFAULT NULL,
  `monaxikos` int(11) DEFAULT NULL,
  `emfiliVia` int(11) DEFAULT NULL,
  `spoudastis` int(11) DEFAULT NULL,
  `anenergos` int(11) DEFAULT NULL,
  `loipa` longtext,
  `editing` int(11) DEFAULT 0,
  `registerDate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `barcode_UNIQUE` (`barcode`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `theseisergasias`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eponimia` varchar(45) DEFAULT NULL,
  `eponimo` varchar(45) DEFAULT NULL,
  `onoma` varchar(45) DEFAULT NULL,
  `patronimo` varchar(45) DEFAULT NULL,
  `dieuthinsi` varchar(245) DEFAULT NULL,
  `tilefono` varchar(45) DEFAULT NULL,
  `thesi` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `users`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pass` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS`vivliothiki`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `siggrafeas` varchar(145) DEFAULT NULL,
  `titlos` varchar(250) DEFAULT NULL,
  `ekdotikos` varchar(45) DEFAULT NULL,
  `katigoria` varchar(45) DEFAULT NULL,
  `selides` varchar(45) DEFAULT NULL,
  `isbn` varchar(45) DEFAULT NULL,
  `daneismeno` int(11) DEFAULT NULL,
  `eponimo` varchar(45) DEFAULT NULL,
  `onoma` varchar(45) DEFAULT NULL,
  `patronimo` varchar(45) DEFAULT NULL,
  `dieuthinsi` varchar(245) DEFAULT NULL,
  `tilefono` varchar(45) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `vivliothikiistoriko` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `siggrafeas` varchar(145) DEFAULT NULL,
  `titlos` varchar(250) DEFAULT NULL,
  `ekdotikos` varchar(45) DEFAULT NULL,
  `katigoria` varchar(45) DEFAULT NULL,
  `selides` varchar(45) DEFAULT NULL,
  `isbn` varchar(45) DEFAULT NULL,
  `eponimo` varchar(45) DEFAULT NULL,
  `onoma` varchar(45) DEFAULT NULL,
  `patronimo` varchar(45) DEFAULT NULL,
  `dieuthinsi` varchar(245) DEFAULT NULL,
  `tilefono` varchar(45) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `dateEpistrofis` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `zitounergasia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eponimo` varchar(45) DEFAULT NULL,
  `onoma` varchar(45) DEFAULT NULL,
  `patronimo` varchar(45) DEFAULT NULL,
  `imGennisis` date DEFAULT NULL,
  `tilefono` varchar(45) DEFAULT NULL,
  `dieuthinsi` varchar(245) DEFAULT NULL,
  `dimos` varchar(45) DEFAULT NULL,
  `photoID` varchar(45) DEFAULT NULL,
  `eidikotita` varchar(245) DEFAULT NULL,
  `ye` int(11) DEFAULT NULL,
  `de` int(11) DEFAULT NULL,
  `te` int(11) DEFAULT NULL,
  `pe` int(11) DEFAULT NULL,
  `diploma` varchar(45) DEFAULT NULL,
  `empeiria` longtext,
  `oikKatastasi` int(11) DEFAULT NULL,
  `loipa` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;