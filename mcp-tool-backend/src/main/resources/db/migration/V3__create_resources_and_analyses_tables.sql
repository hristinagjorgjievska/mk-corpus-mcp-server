create table resources (
    id bigserial primary key,
    created_at timestamp not null,
    updated_at timestamp not null,
    search_run_id bigint not null references search_runs(id) on delete cascade,
    external_id varchar(255),
    title varchar(255),
    content text,
    source_url varchar(2048),
    language varchar(255),
    macedonian_confidence double precision,
    word_count integer,
    fetched_at timestamp,
    version bigint not null
);

create table resource_analyses (
    id bigserial primary key,
    created_at timestamp not null,
    updated_at timestamp not null,
    resource_id bigint not null unique references resources(id) on delete cascade,
    summary text,
    keywords text,
    sentence_count integer,
    macedonian_confidence double precision,
    analyzed_at timestamp
);

create index idx_resources_search_run on resources(search_run_id);
