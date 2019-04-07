drop table image_links;
drop table articles;
drop table pages;
drop table newspapers;
drop table countries;
drop table languages;

CREATE TABLE `countries`
(
  `name`        varchar(255) NOT NULL,
  `countryCode` varchar(255) DEFAULT NULL,
  `timeZone`    varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `languages`
(
  `id`   varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

# Table, Create Table
CREATE TABLE `newspapers`
(
  `id`          varchar(255) NOT NULL,
  `active`      bit(1)       NOT NULL,
  `name`        varchar(255) DEFAULT NULL,
  `countryName` varchar(255) DEFAULT NULL,
  `languageId`  varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4blv8x93fk99llbqicfupml7m` (`countryName`),
  KEY `FK98cba7cgrtigouhqhphasrsjs` (`languageId`),
  CONSTRAINT `FK4blv8x93fk99llbqicfupml7m` FOREIGN KEY (`countryName`) REFERENCES `countries` (`name`),
  CONSTRAINT `FK98cba7cgrtigouhqhphasrsjs` FOREIGN KEY (`languageId`) REFERENCES `languages` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

# Table, Create Table
CREATE TABLE `pages`
(
  `id`                     varchar(255) NOT NULL,
  `active`                 bit(1)       NOT NULL,
  `firstEditionDateString` varchar(255) DEFAULT NULL,
  `linkFormat`             text         DEFAULT NULL,
  `linkVariablePartFormat` varchar(255) DEFAULT NULL,
  `name`                   varchar(255) DEFAULT NULL,
  `parentPageId`           varchar(255) DEFAULT NULL,
  `weekly`                 bit(1)       NOT NULL,
  `weeklyPublicationDay`   int(11)      DEFAULT NULL,
  `newsPaperId`            varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5m7ck9noyma2252hve2gmeuiu` (`newsPaperId`),
  CONSTRAINT `FK5m7ck9noyma2252hve2gmeuiu` FOREIGN KEY (`newsPaperId`) REFERENCES `newspapers` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `articles`
(
  `id`               varchar(255) NOT NULL,
  `modificationTS`   datetime     DEFAULT NULL,
  `publicationTS`    datetime     DEFAULT NULL,
  `title`            varchar(255) DEFAULT NULL,
  `articleText`      text         DEFAULT NULL,
  `previewImageLink` text         DEFAULT NULL,
  `articleLink`      text         DEFAULT NULL,
  `pageId`           varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6fptm12k5ef7w7fy26su0gvs1` (`pageId`),
  CONSTRAINT `FK6fptm12k5ef7w7fy26su0gvs1` FOREIGN KEY (`pageId`) REFERENCES `pages` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `image_links`
(
  `articleId` varchar(255) NOT NULL,
  `link`      text DEFAULT NULL,
  `caption`   text DEFAULT NULL,
  KEY `FKtarkqvk2kgymilolrr4g2x3ae` (`articleId`),
  CONSTRAINT `FKtarkqvk2kgymilolrr4g2x3ae` FOREIGN KEY (`articleId`) REFERENCES `articles` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;