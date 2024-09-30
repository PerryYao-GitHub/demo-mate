create table r_join
(
    id          int auto_increment
        primary key,
    user_id     int               not null,
    team_id     int               not null,
    create_time datetime          not null,
    deleted     tinyint default 0 not null
);

create table t_team
(
    id          int auto_increment
        primary key,
    name        varchar(36)       not null,
    description varchar(128)      null,
    member_cnt  int               not null,
    leader_id   int               not null,
    create_time datetime          not null,
    update_time datetime          not null,
    deleted     tinyint default 0 not null
);

create table t_user
(
    id              int auto_increment
        primary key,
    account         varchar(36)       null,
    password        varchar(36)       not null,
    name            varchar(18)       null,
    avatar          varchar(320)      null,
    phone           varchar(20)       null,
    email           varchar(36)       null,
    tags            varchar(512)      null,
    create_team_cnt int     default 0 not null,
    join_team_cnt   int     default 0 null,
    status          tinyint           not null,
    role            tinyint           not null,
    login_time      datetime          null,
    create_time     datetime          null,
    update_time     datetime          null,
    deleted         tinyint default 0 not null
);


