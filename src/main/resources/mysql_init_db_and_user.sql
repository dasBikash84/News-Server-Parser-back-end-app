DROP DATABASE if exists `news_server_parser2`;

CREATE DATABASE `news_server_parser2`;

CREATE TABLE `news_server_parser2`.`countries`
(
    `name`        varchar(255) NOT NULL,
    `countryCode` varchar(255) DEFAULT NULL,
    `timeZone`    varchar(255) DEFAULT NULL,
    `created`     datetime     DEFAULT CURRENT_TIMESTAMP,
    `modified`    datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`name`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE `news_server_parser2`.`languages`
(
    `id`       varchar(255) NOT NULL,
    `name`     varchar(255) DEFAULT NULL,
    `created`  datetime     DEFAULT CURRENT_TIMESTAMP,
    `modified` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `language_name_unique_key` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `news_server_parser2`.`newspapers`
(
    `id`          varchar(255) NOT NULL,
    `active`      bit(1)       NOT NULL,
    `name`        varchar(255) DEFAULT NULL,
    `countryName` varchar(255) DEFAULT NULL,
    `languageId`  varchar(255) DEFAULT NULL,
    `created`     datetime     DEFAULT CURRENT_TIMESTAMP,
    `modified`    datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `newspapers_name_countryName_unique_key` (`countryName`, `name`),
    KEY `newspapers_countryName_key` (`countryName`),
    KEY `newspapers_languageId_key` (`languageId`),
    CONSTRAINT `newspaper_countryName_fk` FOREIGN KEY (`countryName`) REFERENCES `countries` (`name`),
    CONSTRAINT `newspaper_languageId_fk` FOREIGN KEY (`languageId`) REFERENCES `languages` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `news_server_parser2`.`pages`
(
    `id`                     varchar(255) NOT NULL,
    `active`                 bit(1)       NOT NULL,
    `firstEditionDateString` varchar(255) DEFAULT NULL,
    `linkFormat`             text,
    `linkVariablePartFormat` varchar(255) DEFAULT NULL,
    `name`                   varchar(255) DEFAULT NULL,
    `parentPageId`           varchar(255) DEFAULT NULL,
    `weekly`                 bit(1)       NOT NULL,
    `weeklyPublicationDay`   int(11)      DEFAULT NULL,
    `newsPaperId`            varchar(255) DEFAULT NULL,
    `created`                datetime     DEFAULT CURRENT_TIMESTAMP,
    `modified`               datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `pages_name_newsPaperId_parentPageId_unique_key` (`newsPaperId`, `name`, `parentPageId`),
    KEY `pages_newsPaperId_key` (`newsPaperId`),
    CONSTRAINT `pages_newsPaperId_fkey_constraint` FOREIGN KEY (`newsPaperId`) REFERENCES `newspapers` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `news_server_parser2`.`page_groups`
(
    `id`      int(11)      NOT NULL AUTO_INCREMENT,
    `name`    varchar(255) NOT NULL,
    `active`  bit(1)   DEFAULT b'1',
    `created` datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `news_server_parser2`.`page_group_entries`
(
    `id`          int(11)      NOT NULL AUTO_INCREMENT,
    `pageGroupId` int(11)      NOT NULL,
    `pageId`      varchar(255) NOT NULL,
    `created`     datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `page_group_entries_pageGroupId_pageId_unique_key` (`pageGroupId`, `pageId`),
    KEY `page_group_entries_pageId_fk` (`pageId`),
    CONSTRAINT `page_group_entries_pageGroupId_fk` FOREIGN KEY (`pageGroupId`) REFERENCES `page_groups` (`id`),
    CONSTRAINT `page_group_entries_pageId_fk` FOREIGN KEY (`pageId`) REFERENCES `pages` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `news_server_parser2`.`articles`
(
    `serial`           int(11)      NOT NULL AUTO_INCREMENT,
    `id`               varchar(255) NOT NULL,
    `modificationTS`   datetime     DEFAULT NULL,
    `publicationTS`    datetime     DEFAULT NULL,
    `title`            varchar(255) DEFAULT NULL,
    `articleText`      text,
    `previewImageLink` text,
    `articleLink`      text,
    `pageId`           varchar(255) DEFAULT NULL,
    `modified`         datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`serial`),
    UNIQUE KEY `article_id_unique_key` (`id`),
    KEY `articles_pageId_key` (`pageId`),
    CONSTRAINT `articles_pageId_fkey_constraint` FOREIGN KEY (`pageId`) REFERENCES `pages` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `news_server_parser2`.`image_links`
(
    `articleId` int(11) NOT NULL,
    `link`      text,
    `caption`   text,
    `created`   datetime DEFAULT CURRENT_TIMESTAMP,
    KEY `FKtarkqvk2kgymilolrr4g2x3ae` (`articleId`),
    CONSTRAINT `FKtarkqvk2kgymilolrr4g2x3ae` FOREIGN KEY (`articleId`) REFERENCES `articles` (`serial`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `news_server_parser2`.`general_log`
(
    `id`         int(11) NOT NULL AUTO_INCREMENT,
    `logMessage` text,
    `created`    datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `news_server_parser2`.`exception_log`
(
    `id`                       int(11) NOT NULL AUTO_INCREMENT,
    `exceptionClassFullName`   varchar(255) DEFAULT NULL,
    `exceptionClassSimpleName` varchar(255) DEFAULT NULL,
    `exceptionCause`           text,
    `exceptionMessage`         text,
    `stackTrace`               text,
    `created`                  datetime     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `news_server_parser2`.`page_parsing_history`
(
    `id`                int(11) NOT NULL AUTO_INCREMENT,
    `pageId`            varchar(255) DEFAULT NULL,
    `pageNumber`        int(11) NOT NULL,
    `articleCount`      int(11) NOT NULL,
    `parsingLogMessage` text    NOT NULL,
    `created`           datetime     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK6fptm1kw30duw7fy26su0gvs1` (`pageId`),
    KEY `FK6fptm1kw30dulow56ngu0gvs1` (`pageNumber`),
    CONSTRAINT `FK6fptm12k5esfhkitw6su0gvs1` FOREIGN KEY (`pageId`) REFERENCES `pages` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `news_server_parser2`.`tokens`
(
    `token`     varchar(255) NOT NULL,
    `expiresOn` datetime     NOT NULL,
    `created`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`token`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `news_server_parser2`.`rest_activity_log`
(
    `id`                  int(11)      NOT NULL AUTO_INCREMENT,
    `requestURL`          varchar(255) NOT NULL,
    `requestMethod`       varchar(255) NOT NULL,
    `remoteHost`          varchar(255) NOT NULL,
    `methodSignature`     varchar(255) NOT NULL,
    `exceptionClassName`  varchar(255) DEFAULT NULL,
    `timeTakenMs`         int(5)       NOT NULL,
    `returnedEntiryCount` int(3)       DEFAULT 0,
    `acceptHeader`        VARCHAR(45)  DEFAULT NULL,
    `userAgentHeader`     VARCHAR(255) DEFAULT NULL,
    `created`             datetime     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8;

CREATE TABLE `news_server_parser2`.`np_opmode_entry`
(
    `id`          int(11)                       NOT NULL AUTO_INCREMENT,
    `opMode`      enum ('RUNNING','GET_SYNCED') NOT NULL,
    `newsPaperId` varchar(255) DEFAULT NULL,
    `created`     datetime     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `np_opmode_entry_newsPaperId_fkey_constraint` FOREIGN KEY (`newsPaperId`) REFERENCES `newspapers` (`id`),
    PRIMARY KEY (`id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8;

CREATE TABLE `news_server_parser2`.`page_parsing_interval`
(
    `id`                INT(4)      NOT NULL AUTO_INCREMENT,
    `pageId`            VARCHAR(45) NOT NULL,
    `parsingIntervalMS` INT(11)     NOT NULL,
    `modified`          DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `pageId_UNIQUE` (`pageId` ASC),
    CONSTRAINT `fk_page_parsing_interval_pageID` FOREIGN KEY (`pageId`) REFERENCES `news_server_parser2`.`pages` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
CREATE TABLE `news_server_parser2`.`page_download_request`
(
    `id`                      INT(11)                                       NOT NULL AUTO_INCREMENT,
    `pageId`                  VARCHAR(45)                                   NOT NULL,
    `link`                    VARCHAR(5000)                                 NOT NULL,
    `pageDownloadRequestMode` ENUM ('ARTICLE_BODY', 'ARTICLE_PREVIEW_PAGE') NOT NULL,
    `responseDocumentId`      VARCHAR(255)                                  NOT NULL,
    `requestKey`              VARCHAR(255)                                  NOT NULL DEFAULT "",
    `active`                  BIT(1)                                        NOT NULL DEFAULT b'1',
    `responseContent`         MEDIUMBLOB                                    NULL,
    `created`                 DATETIME                                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified`                DATETIME                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `serverNodeName_UNIQUE` (`serverNodeName` ASC),
    INDEX `fk_page_download_request_pageId_idx` (`pageId` ASC),
    CONSTRAINT `fk_page_download_request_pageId`
        FOREIGN KEY (`pageId`)
            REFERENCES `news_server_parser2`.`pages` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

drop user if exists 'nsp_app_user'@'localhost';
drop user if exists 'nsp_rest_user'@'localhost';

create user 'nsp_app_user'@'localhost' identified by 'nsp_app_user';
create user 'nsp_rest_user'@'localhost' identified by 'nsp_rest_user';

grant select, insert, update, delete on news_server_parser2.* to 'nsp_app_user'@'localhost';

grant select on news_server_parser2.* to 'nsp_rest_user'@'localhost';
grant insert, update on news_server_parser2.tokens to 'nsp_rest_user'@'localhost';
grant insert on news_server_parser2.rest_activity_log to 'nsp_rest_user'@'localhost';
grant insert on news_server_parser2.np_opmode_entry to 'nsp_rest_user'@'localhost';
grant update on news_server_parser2.newspapers to 'nsp_rest_user'@'localhost';
grant insert on news_server_parser2.general_log to 'nsp_rest_user'@'localhost';
grant delete on news_server_parser2.general_log to 'nsp_rest_user'@'localhost';
grant delete on news_server_parser2.exception_log to 'nsp_rest_user'@'localhost';
grant delete on news_server_parser2.page_parsing_history to 'nsp_rest_user'@'localhost';