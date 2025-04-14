CREATE TABLE order_of_merit (
     id                          TEXT                        NOT NULL,
     oom_type                    CHAR(3)                     NOT NULL,
     rank                        INT                         NOT NULL,
     prev_rank                   INT                         NOT NULL,
     player_id                   TEXT                        NOT NULL,
     player_name                 TEXT                        NOT NULL,
     money                       DECIMAL(10,2)               NOT NULL,

     CONSTRAINT PK_oom PRIMARY key (id),
     CONSTRAINT FK_players_oom FOREIGN KEY (player_id) REFERENCES PLAYERS(id)
);