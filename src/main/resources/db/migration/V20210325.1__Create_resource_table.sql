CREATE TABLE resource
(
    id          UUID PRIMARY KEY,
    name        TEXT                     NOT NULL,
    description TEXT                     NOT NULL,
    timezone    TEXT                     NOT NULL,
    capacity    INTEGER                  NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL
);