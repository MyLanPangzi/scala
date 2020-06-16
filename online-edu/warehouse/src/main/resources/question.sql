create external table `dwd`.`dwd_qz_chapter`
(
    chapterid     int,
    chapterlistid int,
    chaptername   string,
    sequence      string,
    showstatus    string,
    creator       string,
    createtime    timestamp,
    courseid      int,
    chapternum    int,
    outchapterid  int
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');

create external table `dwd`.`dwd_qz_chapter_list`
(
    chapterlistid   int,
    chapterlistname string,
    courseid        int,
    chapterallnum   int,
    sequence        string,
    status          string,
    creator         string,
    createtime      timestamp
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');

create external table `dwd`.`dwd_qz_point`
(
    pointid       int,
    courseid      int,
    pointname     string,
    pointyear     string,
    chapter       string,
    creator       string,
    createtme     timestamp,
    status        string,
    modifystatus  string,
    excisenum     int,
    pointlistid   int,
    chapterid     int,
    sequece       string,
    pointdescribe string,
    pointlevel    string,
    typelist      string,
    score         decimal(4, 1),
    thought       string,
    remid         string,
    pointnamelist string,
    typelistids   string,
    pointlist     string
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');

create external table `dwd`.`dwd_qz_point_question`
(
    pointid    int,
    questionid int,
    questype   int,
    creator    string,
    createtime string
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');

create external table `dwd`.`dwd_qz_site_course`
(
    sitecourseid    int,
    siteid          int,
    courseid        int,
    sitecoursename  string,
    coursechapter   string,
    sequence        string,
    status          string,
    creator         string,
    createtime      timestamp,
    helppaperstatus string,
    servertype      string,
    boardid         int,
    showstatus      string
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');



create external table `dwd`.`dwd_qz_course`
(
    courseid      int,
    majorid       int,
    coursename    string,
    coursechapter string,
    sequence      string,
    isadvc        string,
    creator       string,
    createtime    timestamp,
    status        string,
    chapterlistid int,
    pointlistid   int
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `dwd`.`dwd_qz_course_edusubject`
(
    courseeduid  int,
    edusubjectid int,
    courseid     int,
    creator      string,
    createtime   timestamp,
    majorid      int
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');

create external table `dwd`.`dwd_qz_website`
(
    siteid           int,
    sitename         string,
    domain           string,
    sequence         string,
    multicastserver  string,
    templateserver   string,
    status           string,
    creator          string,
    createtime       timestamp,
    multicastgateway string,
    multicastport    string
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `dwd`.`dwd_qz_major`
(
    majorid         int,
    businessid      int,
    siteid          int,
    majorname       string,
    shortname       string,
    status          string,
    sequence        string,
    creator         string,
    createtime      timestamp,
    column_sitetype string
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `dwd`.`dwd_qz_business`
(
    businessid   int,
    businessname string,
    sequence     string,
    status       string,
    creator      string,
    createtime   timestamp,
    siteid       int
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `dwd`.`dwd_qz_paper_view`
(
    paperviewid      int,
    paperid          int,
    paperviewname    string,
    paperparam       string,
    openstatus       string,
    explainurl       string,
    iscontest        string,
    contesttime      timestamp,
    conteststarttime timestamp,
    contestendtime   timestamp,
    contesttimelimit string,
    dayiid           int,
    status           string,
    creator          string,
    createtime       timestamp,
    paperviewcatid   int,
    modifystatus     string,
    description      string,
    papertype        string,
    downurl          string,
    paperuse         string,
    paperdifficult   string,
    testreport       string,
    paperuseshow     string
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `dwd`.`dwd_qz_center_paper`
(
    paperviewid int,
    centerid    int,
    openstatus  string,
    sequence    string,
    creator     string,
    createtime  timestamp
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');

create external table `dwd`.`dwd_qz_paper`
(
    paperid       int,
    papercatid    int,
    courseid      int,
    paperyear     string,
    chapter       string,
    suitnum       string,
    papername     string,
    status        string,
    creator       string,
    createtime    timestamp,
    totalscore    decimal(4, 1),
    chapterid     int,
    chapterlistid int
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');

create external table `dwd`.`dwd_qz_center`
(
    centerid       int,
    centername     string,
    centeryear     string,
    centertype     string,
    openstatus     string,
    centerparam    string,
    description    string,
    creator        string,
    createtime     timestamp,
    sequence       string,
    provideuser    string,
    centerviewtype string,
    stage          string
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `dwd`.`dwd_qz_question`
(
    questionid    int,
    parentid      int,
    questypeid    int,
    quesviewtype  int,
    content       string,
    answer        string,
    analysis      string,
    limitminute   string,
    score         decimal(4, 1),
    splitscore    decimal(4, 1),
    status        string,
    optnum        int,
    lecture       string,
    creator       string,
    createtime    string,
    modifystatus  string,
    attanswer     string,
    questag       string,
    vanalysisaddr string,
    difficulty    string,
    quesskill     string,
    vdeoaddr      string
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');

create external table `dwd`.`dwd_qz_question_type`
(
    quesviewtype   int,
    viewtypename   string,
    questypeid     int,
    description    string,
    status         string,
    creator        string,
    createtime     timestamp,
    papertypename  string,
    sequence       string,
    remark         string,
    splitscoretype string
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `dwd`.`dwd_qz_member_paper_question`
(
    userid          int,
    paperviewid     int,
    chapterid       int,
    sitecourseid    int,
    questionid      int,
    majorid         int,
    useranswer      string,
    istrue          string,
    lasttime        timestamp,
    opertype        string,
    paperid         int,
    spendtime       int,
    score           decimal(4, 1),
    question_answer int
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');



create external table `dws`.`dws_qz_chapter`
(
    chapterid          int,
    chapterlistid      int,
    chaptername        string,
    sequence           string,
    showstatus         string,
    status             string,
    chapter_creator    string,
    chapter_createtime string,
    chapter_courseid   int,
    chapternum         int,
    chapterallnum      int,
    outchapterid       int,
    chapterlistname    string,
    pointid            int,
    questionid         int,
    questype           int,
    pointname          string,
    pointyear          string,
    chapter            string,
    excisenum          int,
    pointlistid        int,
    pointdescribe      string,
    pointlevel         string,
    typelist           string,
    point_score        decimal(4, 1),
    thought            string,
    remid              string,
    pointnamelist      string,
    typelistids        string,
    pointlist          string
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `dws`.`dws_qz_course`
(
    sitecourseid          int,
    siteid                int,
    courseid              int,
    sitecoursename        string,
    coursechapter         string,
    sequence              string,
    status                string,
    sitecourse_creator    string,
    sitecourse_createtime string,
    helppaperstatus       string,
    servertype            string,
    boardid               int,
    showstatus            string,
    majorid               int,
    coursename            string,
    isadvc                string,
    chapterlistid         int,
    pointlistid           int,
    courseeduid           int,
    edusubjectid          int
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `dws`.`dws_qz_major`
(
    majorid          int,
    businessid       int,
    siteid           int,
    majorname        string,
    shortname        string,
    status           string,
    sequence         string,
    major_creator    string,
    major_createtime timestamp,
    businessname     string,
    sitename         string,
    domain           string,
    multicastserver  string,
    templateserver   string,
    multicastgateway string,
    multicastport    string
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `dws`.`dws_qz_paper`
(
    paperviewid           int,
    paperid               int,
    paperviewname         string,
    paperparam            string,
    openstatus            string,
    explainurl            string,
    iscontest             string,
    contesttime           timestamp,
    conteststarttime      timestamp,
    contestendtime        timestamp,
    contesttimelimit      string,
    dayiid                int,
    status                string,
    paper_view_creator    string,
    paper_view_createtime timestamp,
    paperviewcatid        int,
    modifystatus          string,
    description           string,
    paperuse              string,
    paperdifficult        string,
    testreport            string,
    paperuseshow          string,
    centerid              int,
    sequence              string,
    centername            string,
    centeryear            string,
    centertype            string,
    provideuser           string,
    centerviewtype        string,
    stage                 string,
    papercatid            int,
    courseid              int,
    paperyear             string,
    suitnum               string,
    papername             string,
    totalscore            decimal(4, 1),
    chapterid             int,
    chapterlistid         int
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create external table `dws`.`dws_qz_question`
(
    questionid     int,
    parentid       int,
    questypeid     int,
    quesviewtype   int,
    content        string,
    answer         string,
    analysis       string,
    limitminute    string,
    score          decimal(4, 1),
    splitscore     decimal(4, 1),
    status         string,
    optnum         int,
    lecture        string,
    creator        string,
    createtime     timestamp,
    modifystatus   string,
    attanswer      string,
    questag        string,
    vanalysisaddr  string,
    difficulty     string,
    quesskill      string,
    vdeoaddr       string,
    viewtypename   string,
    description    string,
    papertypename  string,
    splitscoretype string
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');


create table `dws`.`dws_user_paper_detail`
(
    `userid`                  int,
    `courseid`                int,
    `questionid`              int,
    `useranswer`              string,
    `istrue`                  string,
    `lasttime`                string,
    `opertype`                string,
    `paperid`                 int,
    `spendtime`               int,
    `chapterid`               int,
    `chaptername`             string,
    `chapternum`              int,
    `chapterallnum`           int,
    `outchapterid`            int,
    `chapterlistname`         string,
    `pointid`                 int,
    `questype`                int,
    `pointyear`               string,
    `chapter`                 string,
    `pointname`               string,
    `excisenum`               int,
    `pointdescribe`           string,
    `pointlevel`              string,
    `typelist`                string,
    `point_score`             decimal(4, 1),
    `thought`                 string,
    `remid`                   string,
    `pointnamelist`           string,
    `typelistids`             string,
    `pointlist`               string,
    `sitecourseid`            int,
    `siteid`                  int,
    `sitecoursename`          string,
    `coursechapter`           string,
    `course_sequence`         string,
    `course_stauts`           string,
    `course_creator`          string,
    `course_createtime`       timestamp,
    `servertype`              string,
    `helppaperstatus`         string,
    `boardid`                 int,
    `showstatus`              string,
    `majorid`                 int,
    `coursename`              string,
    `isadvc`                  string,
    `chapterlistid`           int,
    `pointlistid`             int,
    `courseeduid`             int,
    `edusubjectid`            int,
    `businessid`              int,
    `majorname`               string,
    `shortname`               string,
    `major_status`            string,
    `major_sequence`          string,
    `major_creator`           string,
    `major_createtime`        timestamp,
    `businessname`            string,
    `sitename`                string,
    `domain`                  string,
    `multicastserver`         string,
    `templateserver`          string,
    `multicastgatway`         string,
    `multicastport`           string,
    `paperviewid`             int,
    `paperviewname`           string,
    `paperparam`              string,
    `openstatus`              string,
    `explainurl`              string,
    `iscontest`               string,
    `contesttime`             timestamp,
    `conteststarttime`        timestamp,
    `contestendtime`          timestamp,
    `contesttimelimit`        string,
    `dayiid`                  int,
    `paper_status`            string,
    `paper_view_creator`      string,
    `paper_view_createtime`   timestamp,
    `paperviewcatid`          int,
    `modifystatus`            string,
    `description`             string,
    `paperuse`                string,
    `testreport`              string,
    `centerid`                int,
    `paper_sequence`          string,
    `centername`              string,
    `centeryear`              string,
    `centertype`              string,
    `provideuser`             string,
    `centerviewtype`          string,
    `paper_stage`             string,
    `papercatid`              int,
    `paperyear`               string,
    `suitnum`                 string,
    `papername`               string,
    `totalscore`              decimal(4, 1),
    `question_parentid`       int,
    `questypeid`              int,
    `quesviewtype`            int,
    `question_content`        string,
    `question_answer`         string,
    `question_analysis`       string,
    `question_limitminute`    string,
    `score`                   decimal(4, 1),
    `splitscore`              decimal(4, 1),
    `lecture`                 string,
    `question_creator`        string,
    `question_createtime`     timestamp,
    `question_modifystatus`   string,
    `question_attanswer`      string,
    `question_questag`        string,
    `question_vanalysisaddr`  string,
    `question_difficulty`     string,
    `quesskill`               string,
    `vdeoaddr`                string,
    `question_description`    string,
    `question_splitscoretype` string,
    `user_question_answer`    int
)
    partitioned by (
        dt string,
        dn string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS PARQUET TBLPROPERTIES ('parquet.compression' = 'SNAPPY');



create external table ads.ads_paper_avgtimeandscore
(
    paperviewid   int,
    paperviewname string,
    avgscore      decimal(4, 1),
    avgspendtime  decimal(10, 1)
)
    partitioned by (
        dt string,
        dn string)
    row format delimited fields terminated by '\t';


create external table ads.ads_paper_maxdetail
(
    paperviewid   int,
    paperviewname string,
    maxscore      decimal(4, 1),
    minscore      decimal(4, 1)
)
    partitioned by (
        dt string,
        dn string)
    row format delimited fields terminated by '\t';


create external table ads.ads_top3_userdetail
(
    userid         int,
    paperviewid    int,
    paperviewname  string,
    chaptername    string,
    pointname      string,
    sitecoursename string,
    coursename     string,
    majorname      string,
    shortname      string,
    papername      string,
    score          decimal(4, 1),
    rk             int
)
    partitioned by (
        dt string,
        dn string)
    row format delimited fields terminated by '\t';


create external table ads.ads_low3_userdetail
(
    userid         int,
    paperviewid    int,
    paperviewname  string,
    chaptername    string,
    pointname      string,
    sitecoursename string,
    coursename     string,
    majorname      string,
    shortname      string,
    papername      string,
    score          decimal(4, 1),
    rk             int
)
    partitioned by (
        dt string,
        dn string)
    row format delimited fields terminated by '\t';



create external table ads.ads_paper_scoresegment_user
(
    paperviewid   int,
    paperviewname string,
    score_segment string,
    userids       string
)
    partitioned by (
        dt string,
        dn string)
    row format delimited fields terminated by '\t';



create external table ads.ads_user_paper_detail
(
    paperviewid   int,
    paperviewname string,
    unpasscount   int,
    passcount     int,
    rate          decimal(4, 2)
)
    partitioned by (
        dt string,
        dn string)
    row format delimited fields terminated by '\t';


create external table ads.ads_user_question_detail
(
    questionid int,
    errcount   int,
    rightcount int,
    rate       decimal(4, 2)
)
    partitioned by (
        dt string,
        dn string)
    row format delimited fields terminated by '\t';







