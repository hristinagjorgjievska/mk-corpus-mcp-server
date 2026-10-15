create table search_runs (
    id bigserial primary key,
    created_at timestamp not null,
    updated_at timestamp not null,
    query varchar(255) not null,
    status varchar(255) not null,
    started_at timestamp,
    finished_at timestamp,
    result_count integer
);
