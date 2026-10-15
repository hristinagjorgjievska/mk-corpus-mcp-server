create table tool_invocation_logs (
    id bigserial primary key,
    created_at timestamp not null,
    updated_at timestamp not null,
    tool_name varchar(255) not null,
    arguments text,
    result_summary text,
    successful boolean not null,
    occurred_at timestamp not null,
    search_run_id bigint references search_runs(id) on delete cascade
);

create index idx_tool_invocation_logs_search_run on tool_invocation_logs(search_run_id);
