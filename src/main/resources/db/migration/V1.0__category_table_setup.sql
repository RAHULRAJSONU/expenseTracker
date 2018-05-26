	create table expense_tracker.account (
       id VARCHAR(36) not null,
        active bit not null,
        balance double precision not null,
        code varchar(255),
        created_dttm timestamp,
        description varchar(255),
        last_modified_dttm timestamp,
        last_modified_user varchar(255),
        version bigint,
        customer_id VARCHAR(36),
        parent_account_id VARCHAR(36),
        primary key (id)
    );


    create table expense_tracker.address (
       id VARCHAR(36) not null,
        address_type varchar(255),
        city varchar(255),
        country varchar(255),
        created_dttm timestamp,
        last_modified_dttm timestamp,
        last_modified_user varchar(255),
        state varchar(255),
        street_address varchar(255),
        version bigint,
        zip varchar(255),
        customer_id VARCHAR(36),
        primary key (id)
    );


    create table expense_tracker.category (
       id VARCHAR(36) not null,
        active bit not null,
        created_dttm timestamp,
        description varchar(255),
        last_modified_dttm timestamp,
        last_modified_user varchar(255),
        name varchar(255),
        version bigint,
        parent_id VARCHAR(36),
        primary key (id)
    );


    create table expense_tracker.customer (
       id VARCHAR(36) not null,
        created_dttm timestamp,
        dob bigint not null,
        first_name varchar(255),
        last_modified_dttm timestamp,
        last_modified_user varchar(255),
        last_name varchar(255),
        middle_name varchar(255),
        version bigint,
        gender_id VARCHAR(36),
        primary key (id)
    );


    create table expense_tracker.email (
       id VARCHAR(36) not null,
        created_dttm timestamp,
        email_address varchar(255),
        last_modified_dttm timestamp,
        last_modified_user varchar(255),
        is_primary bit not null,
        version bigint,
        customer_id VARCHAR(36),
        primary key (id)
    );


    create table expense_tracker.gender (
       id VARCHAR(36) not null,
        created_dttm timestamp,
        last_modified_dttm timestamp,
        last_modified_user varchar(255),
        sex varchar(255),
        version bigint,
        primary key (id)
    );


    create table expense_tracker.mobile (
       id VARCHAR(36) not null,
        country_code varchar(255),
        created_dttm timestamp,
        last_modified_dttm timestamp,
        last_modified_user varchar(255),
        mobile_number varchar(255),
        is_primary bit not null,
        version bigint,
        customer_id VARCHAR(36),
        primary key (id)
    );


    create table expense_tracker.transaction (
       id VARCHAR(36) not null,
        ammount double precision not null,
        created_dttm timestamp,
        date datetime,
        description varchar(255),
        last_modified_dttm timestamp,
        last_modified_user varchar(255),
        transaction_type varchar(255),
        version bigint,
        account_id VARCHAR(36),
        category_id VARCHAR(36),
        primary key (id)
    );


    alter table expense_tracker.account
       add constraint FKnnwpo0lfq4xai1rs6887sx02k
       foreign key (customer_id)
       references customer (id);


    alter table expense_tracker.account
       add constraint FKnv7l2hnidyppjeputfeo3jupq
       foreign key (parent_account_id)
       references account (id);


    alter table expense_tracker.address
       add constraint FK93c3js0e22ll1xlu21nvrhqgg
       foreign key (customer_id)
       references customer (id);


    alter table expense_tracker.category
       add constraint FK2y94svpmqttx80mshyny85wqr
       foreign key (parent_id)
       references category (id);


    alter table expense_tracker.customer
       add constraint FK1lt23ie2trgs1r42hqyhliedv
       foreign key (gender_id)
       references gender (id);


    alter table expense_tracker.email
       add constraint FKgcnceoklbx8vce63l05y0o44c
       foreign key (customer_id)
       references customer (id);


    alter table expense_tracker.mobile
       add constraint FK8plhylub3nmk45010r8y01l5m
       foreign key (customer_id)
       references customer (id);


    alter table expense_tracker.transaction
       add constraint FK6g20fcr3bhr6bihgy24rq1r1b
       foreign key (account_id)
       references account (id);


    alter table expense_tracker.transaction
       add constraint FKgik7ruym8r1n4xngrclc6kiih
       foreign key (category_id)
       references category (id);