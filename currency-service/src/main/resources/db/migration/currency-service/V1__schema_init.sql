create schema if not exists app;

create table if not exists app.currency_rates
(
    id         bigserial      not null,
    title      varchar(3)     not null unique,
    rate       decimal(19, 6) not null,
    created_at timestamp(3)   not null,
    updated_at timestamp(3),
    version    bigserial,
    primary key (id)
);