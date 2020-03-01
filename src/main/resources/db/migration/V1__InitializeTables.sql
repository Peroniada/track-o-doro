CREATE TYPE role AS ENUM ('USER', 'ADMIN');

CREATE TABLE user_goals
(
    pk_user_goals_id UUID UNIQUE PRIMARY KEY NOT NULL,
    daily_goal       INT,
    weekly_goal      INT
);

CREATE TABLE user_account
(
    pk_user_id       UUID UNIQUE PRIMARY KEY NOT NULL,
    email            varchar(255),
    password         varchar(255),
    salt             bytea,
    role             role,
    fk_user_goals_id UUID REFERENCES user_goals (pk_user_goals_id)
);

CREATE TABLE pomodoro_session
(
    pk_pomodoro_session_id UUID UNIQUE PRIMARY KEY NOT NULL,
    activity_name          varchar(255),
    category               varchar(255),
    duration               INT,
    session_created        timestamptz,
    fk_owner_id            UUID REFERENCES user_account (pk_user_id)
);

CREATE TABLE pomodoro_category
(
    pk_pomodoro_category_id UUID UNIQUE PRIMARY KEY NOT NULL,
    category_name           varchar(255),
    dailyGoal               INT,
    weeklyGoal              INT,
    fk_owner_id             UUID REFERENCES user_account (pk_user_id)
);