create table expense_tracker.sequence_number (
       class_name varchar(255) not null,
        increment_value integer,
        next_value integer,
        primary key (class_name)
    )