create schema if not exists app;

create table if not exists app.bank_accounts
(
    id            bigserial    not null unique check (id > 0),
    user_id       bigserial    not null unique check (user_id > 0),
    error_message varchar(2000),
    created_at    timestamp(3) not null,
    updated_at    timestamp(3) not null,
    version       bigserial    not null,
    primary key (id)
);

create table if not exists app.currency_wallets
(
    id              bigserial      not null unique check (id > 0),
    bank_account_id bigserial      not null check (id > 0),
    currency        varchar(5)     not null check (length(currency) > 0),
    balance         decimal(19, 6) not null,
    error_message   varchar(2000),
    created_at      timestamp(3)   not null,
    updated_at      timestamp(3)   not null,
    version         bigserial      not null,
    primary key (id)
);

create table if not exists app.payments
(
    id             bigserial      not null unique check (id > 0),
    type           varchar(50)    not null check (length(type) > 0),
    currency       varchar(5)     not null check (length(currency) > 0),
    amount         decimal(19, 2) not null check (amount >= 0),
    source_id      bigserial,
    destination_id bigint,
    comment        varchar(2000),
    error_message  varchar(2000),
    status         varchar(50)    not null check (length(status) > 0),
    created_at     timestamp(3)   not null,
    updated_at     timestamp(3)   not null,
    version        bigserial      not null,
    primary key (id)
);

create table if not exists app.refunds
(
    id            bigserial      not null unique check (id > 0),
    payment_id    bigserial      not null check (payment_id > 0),
    currency      varchar(5)     not null check (length(currency) > 0),
    amount        decimal(19, 2) not null check (amount >= 0),
    reason        varchar(2000),
    error_message varchar(2000),
    status        varchar(50)    not null check (length(status) > 0),
    created_at    timestamp(3)   not null,
    updated_at    timestamp(3)   not null,
    version       bigserial      not null,
    primary key (id)
);

ALTER TABLE app.currency_wallets
    ADD CONSTRAINT fk__currency_wallet__bank_account FOREIGN KEY (bank_account_id)
        REFERENCES app.bank_accounts (id);

ALTER TABLE app.payments
    ADD CONSTRAINT fk__payment_sender__currency_wallet FOREIGN KEY (source_id)
        REFERENCES app.currency_wallets (id);

ALTER TABLE app.payments
    ADD CONSTRAINT fk__payment_receiver__currency_wallet FOREIGN KEY (destination_id)
        REFERENCES app.currency_wallets (id);

ALTER TABLE app.refunds
    ADD CONSTRAINT fk__refund__transaction FOREIGN KEY (payment_id)
        REFERENCES app.payments (id);