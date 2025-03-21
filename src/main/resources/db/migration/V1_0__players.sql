CREATE TABLE PLAYERS (
    id                          UUID                        NOT NULL,
    name                        TEXT                        NOT NULL,
    country                     CHAR(3)                     NOT NULL,
    tour_card                   BOOLEAN                     NOT NULL DEFAULT FALSE,
    average                     DECIMAL(5,2)                NOT NULL,
    first_nine_average          DECIMAL(5,2)                NOT NULL,
    accuracy_starting_double    DECIMAL(5,2)                NOT NULL,
    accuracy_bull               DECIMAL(5,2)                NOT NULL,
    accuracy_double             DECIMAL(5,2)                NOT NULL,
    accuracy_double_third       DECIMAL(5,2)                NOT NULL,
    accuracy_treble             DECIMAL(5,2)                NOT NULL,


    CONSTRAINT PK_players PRIMARY key (id)
);