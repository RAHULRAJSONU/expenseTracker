create table expense_tracker.transaction_log (
        id VARCHAR(36) not null,
        date bigint not null,
        amount double precision not null,
        beneficiary_account_id VARCHAR(36) not null,
        debit_account_id VARCHAR(36) not null,
        category_id VARCHAR(36) not null,
        description VARCHAR(255) not null,
        created_dttm timestamp,
        last_modified_dttm timestamp,
        last_modified_user varchar(255),
        version bigint,
        primary key (id)
    )ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;