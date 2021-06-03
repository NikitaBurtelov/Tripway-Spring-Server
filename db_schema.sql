CREATE TABLE Point
(
    "id"               uuid             NOT NULL,
    name               text             NOT NULL,
    description        text             NULL,
    photos             text[]           NOT NULL,
    lat                double precision NOT NULL,
    lng                double precision NOT NULL,
    address            text             NOT NULL,
    address_components text             NOT NULL,
    trip_id            uuid             NOT NULL,
    CONSTRAINT PK_point PRIMARY KEY ("id"),
    CONSTRAINT FK_26 FOREIGN KEY (trip_id) REFERENCES Trip ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX fkIdx_27 ON Point
    (
     trip_id
        );

CREATE TABLE Trip
(
    "id"             uuid    NOT NULL,
    tripname         text    NOT NULL,
    is_completed     boolean NOT NULL,
    first_point_name text    NOT NULL,
    last_point_name  text    NOT NULL,
    photo            text    NOT NULL,
    user_id            text    NOT NULL,
    CONSTRAINT PK_trip PRIMARY KEY ("id"),
    CONSTRAINT FK_18 FOREIGN KEY (user_id) REFERENCES "User" (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX fkIdx_19 ON Trip (user_id);

CREATE TABLE "User"
(
    uid      text NOT NULL,
    email    text NOT NULL,
    nickname text NOT NULL,
    password text NOT NULL,
    photo    text NULL,
    CONSTRAINT PK_user PRIMARY KEY (uid)
);

CREATE TABLE Subscription
(
    from_user text NOT NULL,
    to_user   text NOT NULL,
    CONSTRAINT PK_subscription PRIMARY KEY (from_user, to_user),
    CONSTRAINT FK_34 FOREIGN KEY (from_user) REFERENCES "User" (uid) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_37 FOREIGN KEY (to_user) REFERENCES "User" (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX fkIdx_35 ON Subscription (from_user);

CREATE INDEX fkIdx_38 ON Subscription
    (
     to_user
        );
