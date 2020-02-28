drop table if exists tbl_game_scores;
drop table if exists tbl_game_participants;
drop table if exists tbl_game_questions;
drop index if exists idx_games_organizers;
drop table if exists tbl_game;
drop table if exists tbl_question_matching_pairs;
drop table if exists tbl_question_listing_values;
drop table if exists tbl_question_clues;
drop table if exists tbl_question_choices;
drop index if exists idx_questions_asked_by;
drop table if exists tbl_question;
drop
type if exists category_type;
drop table if exists tbl_scrum_buzz;
drop table if exists tbl_scrum_cards;
drop table if exists tbl_scrummage;
drop table if exists tbl_card_deck;
drop table if exists tbl_users_network;
drop
type if exists follow_status;
drop table if exists tbl_team_members;
drop
type if exists member_status;
drop table if exists tbl_team;
drop table if exists tbl_user;
drop table if exists tbl_location;
drop table if exists tbl_address;
drop table if exists tbl_account;
drop index if exists idx_account_emails;
drop index if exists idx_account_users;
drop
type if exists account_type;
drop table if exists tbl_node_links;
drop table if exists tbl_link_names;
drop table if exists tbl_events_log;

create table if not exists tbl_events_log
(
    id           varchar(128) not null,
    name         varchar(128) not null,
    sender       varchar(128) not null,
    version      char(8)      not null,
    body         text         not null,
    date_created timestamp default current_timestamp
);

create table if not exists tbl_link_names
(
    name         varchar(64) not null,
    description  varchar(64),
    date_created timestamp default current_timestamp,
    primary key (name)
);

create table if not exists tbl_node_links
(
    table_from  varchar(64) not null,
    column_from varchar(64) not null,
    value_from  int         not null,
    table_to    varchar(64) not null,
    column_to   varchar(64) not null,
    value_to    int         not null,
    link_name   varchar(64) not null,
    foreign key (link_name) references tbl_link_names (name),
    unique (table_from, column_from, value_from, table_to, column_to, value_to, link_name)
);

create type account_type AS ENUM ('admin', 'user', 'guest');

create table if not exists tbl_account
(
    id            serial,
    user_name     varchar(50)  not null,
    pass_code     varchar(256) not null,
    email_address varchar(100) not null,
    account_type  account_type default 'user',
    date_created  timestamp    default current_timestamp,
    primary key (id),
    unique (user_name),
    unique (email_address)
);

create index idx_account_users on tbl_account (user_name);

create index idx_account_emails on tbl_account (email_address);

create table if not exists tbl_address
(
    id           serial,
    street       varchar(50),
    unit         varchar(50),
    city         varchar(50) not null,
    state        varchar(3)  not null,
    date_created timestamp default current_timestamp,
    primary key (id),
    unique (city, state)
);

create table if not exists tbl_location
(
    id           serial,
    address      int         not null,
    title        varchar(64) not null,
    date_created timestamp default current_timestamp,
    foreign key (address) references tbl_address (id),
    primary key (id),
    unique (address, title)
);

create table if not exists tbl_user
(
    id           serial,
    first_name   varchar(50),
    last_name    varchar(50),
    nick_name    varchar(50),
    user_account int not null,
    date_created timestamp default current_timestamp,
    primary key (id),
    foreign key (user_account) references tbl_account (id),
    unique (nick_name)
);

create table if not exists tbl_team
(
    id           serial,
    title        varchar(64) not null,
    captain      int         not null,
    location     int         not null,
    date_created timestamp default current_timestamp,
    primary key (id),
    foreign key (captain) references tbl_user (id),
    foreign key (location) references tbl_location (id)
);

create type member_status AS ENUM ('requested', 'accepted', 'rejected', 'blocked', 'suspended', 'revoked');

create table if not exists tbl_team_members
(
    team          int           not null,
    member        int           not null,
    member_status member_status not null default 'requested',
    date_joined   timestamp              default current_timestamp,
    foreign key (team) references tbl_team (id),
    foreign key (member) references tbl_user (id),
    unique (team, member)
);

create type follow_status AS ENUM ('requested', 'accepted', 'rejected', 'blocked', 'muted', 'unfollow');

create table if not exists tbl_users_network
(
    followee       int           not null,
    follower       int           not null,
    follow_status  follow_status not null default 'requested',
    date_requested timestamp              default current_timestamp,
    foreign key (followee) references tbl_user (id),
    foreign key (follower) references tbl_user (id)
);

create table if not exists tbl_card_deck
(
    id           serial,
    name         varchar(32) not null,
    values       varchar(16)[] not null,
    date_created timestamp default current_timestamp,
    primary key (id),
    unique (name)
);

--clean up every 'x' amount of time (minutes, hours etc)
create table if not exists tbl_scrummage
(
    team         int       not null,
    deck         int       not null,
    start_time   timestamp not null,
    date_created timestamp default current_timestamp,
    foreign key (team) references tbl_team (id),
    foreign key (deck) references tbl_card_deck (id),
    primary key (team, deck)
);

--always update existing row (player, team, deck) on new values
--all rows deleted on cascade when (team, deck) is removed from tbl_scrummage
create table if not exists tbl_scrum_cards
(
    team           int         not null,
    deck           int         not null,
    player         int         not null,
    value          varchar(8) not null,
    time_submitted timestamp   not null,
    foreign key (team, deck) references tbl_scrummage (team, deck) on delete cascade,
    foreign key (player) references tbl_user (id),
    unique (team, deck, player)
);

--always insert new row (player, team, from, to) on new values
--all rows deleted on cascade when (team, deck) is removed from tbl_scrummage
create table if not exists tbl_scrum_buzz
(
    team        int          not null,
    deck        int          not null,
    player_from int          not null,
    player_to   int          not null,
    content     varchar(256) not null,
    send_time   timestamp    not null,
    foreign key (team, deck) references tbl_scrummage (team, deck) on delete cascade,
    foreign key (player_from) references tbl_user (id),
    foreign key (player_to) references tbl_user (id)
);

create type category_type AS ENUM ('math', 'science', 'geography');

create table if not exists tbl_question
(
    id             serial,
    content        varchar(128)  not null,
    category       category_type not null,
    asked_by       int           not null,
    correct_answer varchar(128)  not null,
    answer_reason  varchar(256)  not null,
    date_created   timestamp default current_timestamp,
    primary key (id),
    foreign key (asked_by) references tbl_user (id)
);

create index idx_questions_asked_by on tbl_question (content, asked_by);

create table if not exists tbl_question_choices
(
    question      int         not null,
    choice_value  varchar(50) not null,
    choice_reason varchar(128),
    foreign key (question) references tbl_question (id),
    unique (question, choice_value)
);

create table if not exists tbl_question_clues
(
    question    int         not null,
    clue_value  varchar(50) not null,
    clue_reason varchar(128),
    foreign key (question) references tbl_question (id),
    unique (question, clue_value)
);

create table if not exists tbl_question_listing_values
(
    question         int          not null,
    list_item_value  varchar(50)  not null,
    list_item_reason varchar(128) not null,
    foreign key (question) references tbl_question (id),
    unique (question, list_item_value)
);

create table if not exists tbl_question_matching_pairs
(
    question             int          not null,
    pair_item_value      varchar(50)  not null,
    match_item_value     varchar(50)  not null,
    matching_pair_reason varchar(128) not null,
    foreign key (question) references tbl_question (id),
    unique (question, pair_item_value, match_item_value)
);

create table if not exists tbl_game
(
    id             serial,
    title          varchar(128) not null,
    organizer      int          not null,
    scheduled_date timestamp,
    date_created   timestamp default current_timestamp,
    primary key (id),
    foreign key (organizer) references tbl_user (id)
);

create index idx_games_organizers on tbl_game (title, organizer);

create table if not exists tbl_game_questions
(
    game     int not null,
    question int not null,
    points   int default 0,
    duration int default 0,
    foreign key (game) references tbl_game (id),
    foreign key (question) references tbl_question (id),
    unique (game, question)
);

create table if not exists tbl_game_participants
(
    game   int not null,
    player int not null,
    foreign key (game) references tbl_game (id),
    foreign key (player) references tbl_user (id),
    primary key (game, player)
);

create table if not exists tbl_game_scores
(
    game        int not null,
    player      int not null,
    question    int not null,
    score       int       default 0,
    time_left   int       default 0,
    submit_date timestamp default current_timestamp,
    foreign key (game, player) references tbl_game_participants (game, player),
    foreign key (question) references tbl_question (id),
    primary key (game, player)
);
