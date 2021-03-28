CREATE TABLE reservation
(
    id          UUID PRIMARY KEY,
    resource_id UUID                        NOT NULL,
    name        TEXT,
    start_time  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    state       TEXT                        NOT NULL,
    quantity    INTEGER                     NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE    NOT NULL
);

CREATE INDEX reservation_times_idx on reservation(start_time, end_time, state);