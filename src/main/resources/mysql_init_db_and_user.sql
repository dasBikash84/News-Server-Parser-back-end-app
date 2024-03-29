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

CREATE TABLE `news_server_parser2`.`news_categories`
(
    `id`       VARCHAR(50)  NOT NULL,
    `name`     VARCHAR(255) NOT NULL,
    `created`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO `news_server_parser2`.`news_categories`
    (`id`, `name`)
VALUES ('NEWS_CAT_001', 'Latest (সর্বশেষ)'),
       ('NEWS_CAT_002', 'Politics (রাজনীতি)'),
       ('NEWS_CAT_003', 'Business & Economy (অর্থনীতি/বাণিজ্য)'),
       ('NEWS_CAT_004', 'Sports (খেলার খবর)'),
       ('NEWS_CAT_005', 'Editorial (সম্পাদকীয়)'),
       ('NEWS_CAT_006', 'IT (বিজ্ঞান ও প্রযুক্তি)'),
       ('NEWS_CAT_007', 'Entertainment (বিনোদন)'),
       ('NEWS_CAT_008', 'Magazine (ফিচার)'),
       ('NEWS_CAT_009', 'Style & Fashion (স্টাইল ও ফ্যাশন)'),
       ('NEWS_CAT_010', 'Lifestyle (জীবনযাপন)'),
       ('NEWS_CAT_011', 'Stock Markets (শেয়ার বাজার)'),
       ('NEWS_CAT_012', 'Environment (পরিবেশ)'),
       ('NEWS_CAT_013', 'Cricket (ক্রিকেট)'),
       ('NEWS_CAT_014', 'Football (ফুটবল)'),
       ('NEWS_CAT_015', 'Bangladesh (বাংলাদেশ)'),
       ('NEWS_CAT_016', 'India (ভারত)'),
       ('NEWS_CAT_017', 'Pakistan (পাকিস্তান)'),
       ('NEWS_CAT_018', 'Dhaka (রাজধানী)'),
       ('NEWS_CAT_019', 'International (আন্তর্জাতিক)'),
       ('NEWS_CAT_020', 'Hollywood (হলিউড)'),
       ('NEWS_CAT_021', 'Bollywood (বলিউড)'),
       ('NEWS_CAT_022', 'Dhallywood (ঢালিউড)'),
       ('NEWS_CAT_023', 'UK news (যুক্তরাজ্য)'),
       ('NEWS_CAT_024', 'US News (আমেরিকা)'),
       ('NEWS_CAT_025', 'Asia (এশিয়া)'),
       ('NEWS_CAT_026', 'Europe (ইউরোপ)'),
       ('NEWS_CAT_027', 'North America (উত্তর আমেরিকা)'),
       ('NEWS_CAT_028', 'Travel (পর্যটন)'),
       ('NEWS_CAT_029', 'Food (খাবারদাবার)'),
       ('NEWS_CAT_030', 'Art & Literature (শিল্প ও সাহিত্য)'),
       ('NEWS_CAT_031', 'Interview (সাক্ষাৎকার)'),
       ('NEWS_CAT_032', 'Opinion (মতামত)'),
       ('NEWS_CAT_033', 'Law (আইন-আদালত)'),
       ('NEWS_CAT_034', 'Obituaries'),
       ('NEWS_CAT_035', 'Picture Gallery (ছবি)'),
       ('NEWS_CAT_036', 'Miscellaneous (বিবিধ)');

UPDATE pages
set parentPageId = 'PAGE_ID_0'
WHERE id
          IN ('PAGE_ID_1154',
              'PAGE_ID_1155',
              'PAGE_ID_1152',
              'PAGE_ID_1151',
              'PAGE_ID_715',
              'PAGE_ID_690',
              'PAGE_ID_691',
              'PAGE_ID_656',
              'PAGE_ID_677',
              'PAGE_ID_1106',
              'PAGE_ID_1122',
              'PAGE_ID_1111',
              'PAGE_ID_1123',
              'PAGE_ID_1109',
              'PAGE_ID_1137',
              'PAGE_ID_1101',
              'PAGE_ID_1115',
              'PAGE_ID_981',
              'PAGE_ID_26',
              'PAGE_ID_98',
              'PAGE_ID_46',
              'PAGE_ID_45',
              'PAGE_ID_71',
              'PAGE_ID_115',
              'PAGE_ID_69',
              'PAGE_ID_77',
              'PAGE_ID_44',
              'PAGE_ID_72',
              'PAGE_ID_105',
              'PAGE_ID_78',
              'PAGE_ID_108',
              'PAGE_ID_43',
              'PAGE_ID_42',
              'PAGE_ID_41',
              'PAGE_ID_30',
              'PAGE_ID_23',
              'PAGE_ID_52',
              'PAGE_ID_106',
              'PAGE_ID_79',
              'PAGE_ID_997',
              'PAGE_ID_1011',
              'PAGE_ID_1024',
              'PAGE_ID_1023',
              'PAGE_ID_1038',
              'PAGE_ID_1058',
              'PAGE_ID_1048',
              'PAGE_ID_1022',
              'PAGE_ID_1039',
              'PAGE_ID_1047',
              'PAGE_ID_1021',
              'PAGE_ID_1020',
              'PAGE_ID_1019',
              'PAGE_ID_1014',
              'PAGE_ID_1008',
              'PAGE_ID_1030',
              'PAGE_ID_1057',
              'PAGE_ID_1046',
              'PAGE_ID_836',
              'PAGE_ID_849',
              'PAGE_ID_848',
              'PAGE_ID_872',
              'PAGE_ID_881',
              'PAGE_ID_847',
              'PAGE_ID_873',
              'PAGE_ID_882',
              'PAGE_ID_846',
              'PAGE_ID_890',
              'PAGE_ID_851',
              'PAGE_ID_850',
              'PAGE_ID_893',
              'PAGE_ID_864',
              'PAGE_ID_884',
              'PAGE_ID_546',
              'PAGE_ID_530',
              'PAGE_ID_543',
              'PAGE_ID_541',
              'PAGE_ID_364',
              'PAGE_ID_553',
              'PAGE_ID_496',
              'PAGE_ID_504',
              'PAGE_ID_531',
              'PAGE_ID_494',
              'PAGE_ID_542',
              'PAGE_ID_495',
              'PAGE_ID_493',
              'PAGE_ID_233',
              'PAGE_ID_236',
              'PAGE_ID_766',
              'PAGE_ID_759',
              'PAGE_ID_756',
              'PAGE_ID_604',
              'PAGE_ID_589',
              'PAGE_ID_590',
              'PAGE_ID_155',
              'PAGE_ID_186',
              'PAGE_ID_152',
              'PAGE_ID_200',
              'PAGE_ID_201',
              'PAGE_ID_185',
              'PAGE_ID_184',
              'PAGE_ID_202',
              'PAGE_ID_156',
              'PAGE_ID_176',
              'PAGE_ID_153');

CREATE TABLE `news_server_parser2`.`news_category_entry`
(
    `id`             INT          NOT NULL AUTO_INCREMENT,
    `newsCategoryId` VARCHAR(50)  NOT NULL,
    `pageId`         VARCHAR(255) NOT NULL,
    `created`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `newsCategoryId_fk_idx` (`newsCategoryId` ASC),
    INDEX `fk_news_category_entry_pageId_idx` (`pageId` ASC),
    UNIQUE INDEX `uk_newsCategoryId_pageId` (`newsCategoryId` ASC, `pageId` ASC),
    CONSTRAINT `fk_news_category_entry_newsCategoryId`
        FOREIGN KEY (`newsCategoryId`)
            REFERENCES `news_server_parser2`.`news_categories` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_news_category_entry_pageId`
        FOREIGN KEY (`pageId`)
            REFERENCES `news_server_parser2`.`pages` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)ENGINE = InnoDB
 DEFAULT CHARSET = utf8mb4;

INSERT INTO `news_server_parser2`.`news_category_entry`
(`newsCategoryId`,`pageId`)
VALUES
('NEWS_CAT_001','PAGE_ID_1140'),
('NEWS_CAT_001','PAGE_ID_637'),
('NEWS_CAT_001','PAGE_ID_639'),
('NEWS_CAT_001','PAGE_ID_1088'),
('NEWS_CAT_001','PAGE_ID_249'),
('NEWS_CAT_001','PAGE_ID_248'),
('NEWS_CAT_001','PAGE_ID_257'),
('NEWS_CAT_001','PAGE_ID_961'),
('NEWS_CAT_001','PAGE_ID_369'),
('NEWS_CAT_001','PAGE_ID_6'),
('NEWS_CAT_001','PAGE_ID_5'),
('NEWS_CAT_001','PAGE_ID_4'),
('NEWS_CAT_001','PAGE_ID_1'),
('NEWS_CAT_001','PAGE_ID_982'),
('NEWS_CAT_001','PAGE_ID_808'),
('NEWS_CAT_001','PAGE_ID_809'),
('NEWS_CAT_001','PAGE_ID_280'),
('NEWS_CAT_001','PAGE_ID_273'),
('NEWS_CAT_001','PAGE_ID_383'),
('NEWS_CAT_001','PAGE_ID_755'),
('NEWS_CAT_001','PAGE_ID_574'),
('NEWS_CAT_001','PAGE_ID_139'),
('NEWS_CAT_002','PAGE_ID_1151'),
('NEWS_CAT_002','PAGE_ID_1101'),
('NEWS_CAT_002','PAGE_ID_802'),
('NEWS_CAT_002','PAGE_ID_777'),
('NEWS_CAT_002','PAGE_ID_367'),
('NEWS_CAT_002','PAGE_ID_811'),
('NEWS_CAT_002','PAGE_ID_575'),
('NEWS_CAT_003','PAGE_ID_1146'),
('NEWS_CAT_003','PAGE_ID_1139'),
('NEWS_CAT_003','PAGE_ID_640'),
('NEWS_CAT_003','PAGE_ID_1089'),
('NEWS_CAT_003','PAGE_ID_250'),
('NEWS_CAT_003','PAGE_ID_957'),
('NEWS_CAT_003','PAGE_ID_959'),
('NEWS_CAT_003','PAGE_ID_806'),
('NEWS_CAT_003','PAGE_ID_781'),
('NEWS_CAT_003','PAGE_ID_372'),
('NEWS_CAT_003','PAGE_ID_7'),
('NEWS_CAT_003','PAGE_ID_984'),
('NEWS_CAT_003','PAGE_ID_752'),
('NEWS_CAT_003','PAGE_ID_1000'),
('NEWS_CAT_003','PAGE_ID_815'),
('NEWS_CAT_003','PAGE_ID_384'),
('NEWS_CAT_003','PAGE_ID_215'),
('NEWS_CAT_003','PAGE_ID_757'),
('NEWS_CAT_003','PAGE_ID_754'),
('NEWS_CAT_003','PAGE_ID_582'),
('NEWS_CAT_003','PAGE_ID_143'),
('NEWS_CAT_004','PAGE_ID_1141'),
('NEWS_CAT_004','PAGE_ID_641'),
('NEWS_CAT_004','PAGE_ID_1090'),
('NEWS_CAT_004','PAGE_ID_251'),
('NEWS_CAT_004','PAGE_ID_787'),
('NEWS_CAT_004','PAGE_ID_370'),
('NEWS_CAT_004','PAGE_ID_9'),
('NEWS_CAT_004','PAGE_ID_986'),
('NEWS_CAT_004','PAGE_ID_742'),
('NEWS_CAT_004','PAGE_ID_1001'),
('NEWS_CAT_004','PAGE_ID_816'),
('NEWS_CAT_004','PAGE_ID_276'),
('NEWS_CAT_004','PAGE_ID_385'),
('NEWS_CAT_004','PAGE_ID_218'),
('NEWS_CAT_004','PAGE_ID_760'),
('NEWS_CAT_004','PAGE_ID_585'),
('NEWS_CAT_004','PAGE_ID_576'),
('NEWS_CAT_004','PAGE_ID_146'),
('NEWS_CAT_005','PAGE_ID_1092'),
('NEWS_CAT_005','PAGE_ID_252'),
('NEWS_CAT_005','PAGE_ID_963'),
('NEWS_CAT_005','PAGE_ID_989'),
('NEWS_CAT_005','PAGE_ID_823'),
('NEWS_CAT_005','PAGE_ID_275'),
('NEWS_CAT_005','PAGE_ID_236'),
('NEWS_CAT_005','PAGE_ID_216'),
('NEWS_CAT_005','PAGE_ID_761'),
('NEWS_CAT_006','PAGE_ID_642'),
('NEWS_CAT_006','PAGE_ID_1096'),
('NEWS_CAT_006','PAGE_ID_1094'),
('NEWS_CAT_006','PAGE_ID_794'),
('NEWS_CAT_006','PAGE_ID_373'),
('NEWS_CAT_006','PAGE_ID_15'),
('NEWS_CAT_006','PAGE_ID_985'),
('NEWS_CAT_006','PAGE_ID_744'),
('NEWS_CAT_006','PAGE_ID_1006'),
('NEWS_CAT_006','PAGE_ID_819'),
('NEWS_CAT_006','PAGE_ID_279'),
('NEWS_CAT_006','PAGE_ID_217'),
('NEWS_CAT_006','PAGE_ID_579'),
('NEWS_CAT_006','PAGE_ID_141'),
('NEWS_CAT_006','PAGE_ID_142'),
('NEWS_CAT_007','PAGE_ID_1091'),
('NEWS_CAT_007','PAGE_ID_792'),
('NEWS_CAT_007','PAGE_ID_371'),
('NEWS_CAT_007','PAGE_ID_10'),
('NEWS_CAT_007','PAGE_ID_987'),
('NEWS_CAT_007','PAGE_ID_741'),
('NEWS_CAT_007','PAGE_ID_1002'),
('NEWS_CAT_007','PAGE_ID_817'),
('NEWS_CAT_007','PAGE_ID_277'),
('NEWS_CAT_007','PAGE_ID_386'),
('NEWS_CAT_007','PAGE_ID_219'),
('NEWS_CAT_008','PAGE_ID_1148'),
('NEWS_CAT_008','PAGE_ID_644'),
('NEWS_CAT_008','PAGE_ID_645'),
('NEWS_CAT_008','PAGE_ID_1097'),
('NEWS_CAT_008','PAGE_ID_266'),
('NEWS_CAT_008','PAGE_ID_267'),
('NEWS_CAT_008','PAGE_ID_259'),
('NEWS_CAT_008','PAGE_ID_270'),
('NEWS_CAT_008','PAGE_ID_271'),
('NEWS_CAT_008','PAGE_ID_262'),
('NEWS_CAT_008','PAGE_ID_260'),
('NEWS_CAT_008','PAGE_ID_261'),
('NEWS_CAT_008','PAGE_ID_964'),
('NEWS_CAT_008','PAGE_ID_783'),
('NEWS_CAT_008','PAGE_ID_21'),
('NEWS_CAT_008','PAGE_ID_19'),
('NEWS_CAT_008','PAGE_ID_351'),
('NEWS_CAT_008','PAGE_ID_743'),
('NEWS_CAT_008','PAGE_ID_1004'),
('NEWS_CAT_008','PAGE_ID_825'),
('NEWS_CAT_008','PAGE_ID_763'),
('NEWS_CAT_009','PAGE_ID_105'),
('NEWS_CAT_009','PAGE_ID_106'),
('NEWS_CAT_009','PAGE_ID_1057'),
('NEWS_CAT_009','PAGE_ID_546'),
('NEWS_CAT_009','PAGE_ID_543'),
('NEWS_CAT_009','PAGE_ID_541'),
('NEWS_CAT_009','PAGE_ID_542'),
('NEWS_CAT_009','PAGE_ID_578'),
('NEWS_CAT_009','PAGE_ID_580'),
('NEWS_CAT_009','PAGE_ID_200'),
('NEWS_CAT_010','PAGE_ID_1143'),
('NEWS_CAT_010','PAGE_ID_1095'),
('NEWS_CAT_010','PAGE_ID_1098'),
('NEWS_CAT_010','PAGE_ID_265'),
('NEWS_CAT_010','PAGE_ID_268'),
('NEWS_CAT_010','PAGE_ID_791'),
('NEWS_CAT_010','PAGE_ID_797'),
('NEWS_CAT_010','PAGE_ID_799'),
('NEWS_CAT_010','PAGE_ID_379'),
('NEWS_CAT_010','PAGE_ID_14'),
('NEWS_CAT_010','PAGE_ID_988'),
('NEWS_CAT_010','PAGE_ID_1003'),
('NEWS_CAT_010','PAGE_ID_831'),
('NEWS_CAT_010','PAGE_ID_818'),
('NEWS_CAT_010','PAGE_ID_278'),
('NEWS_CAT_010','PAGE_ID_387'),
('NEWS_CAT_010','PAGE_ID_220'),
('NEWS_CAT_010','PAGE_ID_584'),
('NEWS_CAT_010','PAGE_ID_148'),
('NEWS_CAT_011','PAGE_ID_1115'),
('NEWS_CAT_011','PAGE_ID_958'),
('NEWS_CAT_011','PAGE_ID_52'),
('NEWS_CAT_011','PAGE_ID_997'),
('NEWS_CAT_011','PAGE_ID_1030'),
('NEWS_CAT_011','PAGE_ID_864'),
('NEWS_CAT_011','PAGE_ID_176'),
('NEWS_CAT_012','PAGE_ID_1142'),
('NEWS_CAT_012','PAGE_ID_1145'),
('NEWS_CAT_012','PAGE_ID_581'),
('NEWS_CAT_012','PAGE_ID_144'),
('NEWS_CAT_013','PAGE_ID_690'),
('NEWS_CAT_013','PAGE_ID_1122'),
('NEWS_CAT_013','PAGE_ID_71'),
('NEWS_CAT_013','PAGE_ID_1038'),
('NEWS_CAT_013','PAGE_ID_872'),
('NEWS_CAT_013','PAGE_ID_364'),
('NEWS_CAT_013','PAGE_ID_604'),
('NEWS_CAT_013','PAGE_ID_186'),
('NEWS_CAT_014','PAGE_ID_691'),
('NEWS_CAT_014','PAGE_ID_72'),
('NEWS_CAT_014','PAGE_ID_1039'),
('NEWS_CAT_014','PAGE_ID_873'),
('NEWS_CAT_014','PAGE_ID_504'),
('NEWS_CAT_014','PAGE_ID_577'),
('NEWS_CAT_014','PAGE_ID_185'),
('NEWS_CAT_015','PAGE_ID_1155'),
('NEWS_CAT_015','PAGE_ID_1138'),
('NEWS_CAT_015','PAGE_ID_638'),
('NEWS_CAT_015','PAGE_ID_1087'),
('NEWS_CAT_015','PAGE_ID_256'),
('NEWS_CAT_015','PAGE_ID_960'),
('NEWS_CAT_015','PAGE_ID_776'),
('NEWS_CAT_015','PAGE_ID_780'),
('NEWS_CAT_015','PAGE_ID_366'),
('NEWS_CAT_015','PAGE_ID_368'),
('NEWS_CAT_015','PAGE_ID_2'),
('NEWS_CAT_015','PAGE_ID_737'),
('NEWS_CAT_015','PAGE_ID_739'),
('NEWS_CAT_015','PAGE_ID_998'),
('NEWS_CAT_015','PAGE_ID_810'),
('NEWS_CAT_015','PAGE_ID_814'),
('NEWS_CAT_015','PAGE_ID_233'),
('NEWS_CAT_016','PAGE_ID_43'),
('NEWS_CAT_016','PAGE_ID_1021'),
('NEWS_CAT_016','PAGE_ID_846'),
('NEWS_CAT_016','PAGE_ID_274'),
('NEWS_CAT_016','PAGE_ID_272'),
('NEWS_CAT_016','PAGE_ID_382'),
('NEWS_CAT_016','PAGE_ID_380'),
('NEWS_CAT_016','PAGE_ID_381'),
('NEWS_CAT_016','PAGE_ID_211'),
('NEWS_CAT_016','PAGE_ID_213'),
('NEWS_CAT_016','PAGE_ID_212'),
('NEWS_CAT_017','PAGE_ID_44'),
('NEWS_CAT_017','PAGE_ID_1022'),
('NEWS_CAT_017','PAGE_ID_847'),
('NEWS_CAT_017','PAGE_ID_494'),
('NEWS_CAT_017','PAGE_ID_759'),
('NEWS_CAT_017','PAGE_ID_756'),
('NEWS_CAT_018','PAGE_ID_1154'),
('NEWS_CAT_018','PAGE_ID_255'),
('NEWS_CAT_018','PAGE_ID_790'),
('NEWS_CAT_018','PAGE_ID_376'),
('NEWS_CAT_018','PAGE_ID_30'),
('NEWS_CAT_018','PAGE_ID_23'),
('NEWS_CAT_018','PAGE_ID_738'),
('NEWS_CAT_018','PAGE_ID_1014'),
('NEWS_CAT_018','PAGE_ID_1008'),
('NEWS_CAT_018','PAGE_ID_812'),
('NEWS_CAT_019','PAGE_ID_796'),
('NEWS_CAT_019','PAGE_ID_785'),
('NEWS_CAT_019','PAGE_ID_779'),
('NEWS_CAT_019','PAGE_ID_375'),
('NEWS_CAT_019','PAGE_ID_3'),
('NEWS_CAT_019','PAGE_ID_20'),
('NEWS_CAT_019','PAGE_ID_983'),
('NEWS_CAT_019','PAGE_ID_751'),
('NEWS_CAT_019','PAGE_ID_740'),
('NEWS_CAT_019','PAGE_ID_999'),
('NEWS_CAT_019','PAGE_ID_813'),
('NEWS_CAT_019','PAGE_ID_821'),
('NEWS_CAT_019','PAGE_ID_214'),
('NEWS_CAT_019','PAGE_ID_758'),
('NEWS_CAT_020','PAGE_ID_79'),
('NEWS_CAT_020','PAGE_ID_1046'),
('NEWS_CAT_020','PAGE_ID_884'),
('NEWS_CAT_020','PAGE_ID_531'),
('NEWS_CAT_021','PAGE_ID_78'),
('NEWS_CAT_021','PAGE_ID_1047'),
('NEWS_CAT_021','PAGE_ID_882'),
('NEWS_CAT_021','PAGE_ID_530'),
('NEWS_CAT_022','PAGE_ID_77'),
('NEWS_CAT_022','PAGE_ID_1048'),
('NEWS_CAT_022','PAGE_ID_881'),
('NEWS_CAT_023','PAGE_ID_42'),
('NEWS_CAT_023','PAGE_ID_1020'),
('NEWS_CAT_023','PAGE_ID_851'),
('NEWS_CAT_023','PAGE_ID_495'),
('NEWS_CAT_023','PAGE_ID_589'),
('NEWS_CAT_023','PAGE_ID_140'),
('NEWS_CAT_024','PAGE_ID_98'),
('NEWS_CAT_024','PAGE_ID_41'),
('NEWS_CAT_024','PAGE_ID_1019'),
('NEWS_CAT_024','PAGE_ID_850'),
('NEWS_CAT_024','PAGE_ID_493'),
('NEWS_CAT_024','PAGE_ID_590'),
('NEWS_CAT_024','PAGE_ID_153'),
('NEWS_CAT_025','PAGE_ID_1106'),
('NEWS_CAT_025','PAGE_ID_45'),
('NEWS_CAT_025','PAGE_ID_1023'),
('NEWS_CAT_025','PAGE_ID_848'),
('NEWS_CAT_025','PAGE_ID_155'),
('NEWS_CAT_025','PAGE_ID_156'),
('NEWS_CAT_026','PAGE_ID_677'),
('NEWS_CAT_026','PAGE_ID_1123'),
('NEWS_CAT_026','PAGE_ID_46'),
('NEWS_CAT_026','PAGE_ID_1024'),
('NEWS_CAT_026','PAGE_ID_849'),
('NEWS_CAT_026','PAGE_ID_496'),
('NEWS_CAT_026','PAGE_ID_152'),
('NEWS_CAT_027','PAGE_ID_1109'),
('NEWS_CAT_027','PAGE_ID_13'),
('NEWS_CAT_028','PAGE_ID_798'),
('NEWS_CAT_028','PAGE_ID_108'),
('NEWS_CAT_028','PAGE_ID_890'),
('NEWS_CAT_028','PAGE_ID_583'),
('NEWS_CAT_028','PAGE_ID_149'),
('NEWS_CAT_029','PAGE_ID_115'),
('NEWS_CAT_029','PAGE_ID_1058'),
('NEWS_CAT_029','PAGE_ID_893'),
('NEWS_CAT_029','PAGE_ID_553'),
('NEWS_CAT_029','PAGE_ID_201'),
('NEWS_CAT_029','PAGE_ID_202'),
('NEWS_CAT_030','PAGE_ID_1144'),
('NEWS_CAT_030','PAGE_ID_715'),
('NEWS_CAT_030','PAGE_ID_258'),
('NEWS_CAT_030','PAGE_ID_263'),
('NEWS_CAT_030','PAGE_ID_264'),
('NEWS_CAT_030','PAGE_ID_805'),
('NEWS_CAT_030','PAGE_ID_807'),
('NEWS_CAT_030','PAGE_ID_793'),
('NEWS_CAT_030','PAGE_ID_782'),
('NEWS_CAT_030','PAGE_ID_377'),
('NEWS_CAT_030','PAGE_ID_374'),
('NEWS_CAT_030','PAGE_ID_16'),
('NEWS_CAT_030','PAGE_ID_17'),
('NEWS_CAT_030','PAGE_ID_828'),
('NEWS_CAT_030','PAGE_ID_830'),
('NEWS_CAT_030','PAGE_ID_147'),
('NEWS_CAT_032','PAGE_ID_1150'),
('NEWS_CAT_032','PAGE_ID_1147'),
('NEWS_CAT_032','PAGE_ID_643'),
('NEWS_CAT_032','PAGE_ID_1137'),
('NEWS_CAT_032','PAGE_ID_254'),
('NEWS_CAT_032','PAGE_ID_253'),
('NEWS_CAT_032','PAGE_ID_981'),
('NEWS_CAT_032','PAGE_ID_784'),
('NEWS_CAT_032','PAGE_ID_788'),
('NEWS_CAT_032','PAGE_ID_69'),
('NEWS_CAT_032','PAGE_ID_8'),
('NEWS_CAT_032','PAGE_ID_748'),
('NEWS_CAT_032','PAGE_ID_1005'),
('NEWS_CAT_032','PAGE_ID_766'),
('NEWS_CAT_032','PAGE_ID_184'),
('NEWS_CAT_032','PAGE_ID_145'),
('NEWS_CAT_033','PAGE_ID_1149'),
('NEWS_CAT_033','PAGE_ID_1152'),
('NEWS_CAT_033','PAGE_ID_656'),
('NEWS_CAT_033','PAGE_ID_269'),
('NEWS_CAT_033','PAGE_ID_778'),
('NEWS_CAT_033','PAGE_ID_378'),
('NEWS_CAT_033','PAGE_ID_26'),
('NEWS_CAT_033','PAGE_ID_1011'),
('NEWS_CAT_033','PAGE_ID_836'),
('NEWS_CAT_033','PAGE_ID_586'),
('NEWS_CAT_034','PAGE_ID_150'),
('NEWS_CAT_035','PAGE_ID_1099'),
('NEWS_CAT_035','PAGE_ID_11'),
('NEWS_CAT_035','PAGE_ID_833'),
('NEWS_CAT_036','PAGE_ID_646'),
('NEWS_CAT_036','PAGE_ID_801'),
('NEWS_CAT_036','PAGE_ID_804'),
('NEWS_CAT_036','PAGE_ID_786'),
('NEWS_CAT_036','PAGE_ID_800'),
('NEWS_CAT_036','PAGE_ID_803'),
('NEWS_CAT_036','PAGE_ID_795'),
('NEWS_CAT_036','PAGE_ID_789'),
('NEWS_CAT_036','PAGE_ID_22'),
('NEWS_CAT_036','PAGE_ID_745'),
('NEWS_CAT_036','PAGE_ID_746'),
('NEWS_CAT_036','PAGE_ID_749'),
('NEWS_CAT_036','PAGE_ID_750'),
('NEWS_CAT_036','PAGE_ID_747'),
('NEWS_CAT_036','PAGE_ID_753'),
('NEWS_CAT_036','PAGE_ID_826'),
('NEWS_CAT_036','PAGE_ID_822'),
('NEWS_CAT_036','PAGE_ID_832'),
('NEWS_CAT_036','PAGE_ID_824'),
('NEWS_CAT_036','PAGE_ID_820'),
('NEWS_CAT_036','PAGE_ID_829'),
('NEWS_CAT_036','PAGE_ID_827'),
('NEWS_CAT_036','PAGE_ID_221'),
('NEWS_CAT_036','PAGE_ID_587');

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