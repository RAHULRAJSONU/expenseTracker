ALTER TABLE expense_tracker.transaction MODIFY COLUMN date BIGINT;
ALTER TABLE expense_tracker.transaction CHANGE account_id beneficiary_account_id varchar(36);
ALTER TABLE expense_tracker.transaction ADD debit_account_id varchar(36);
ALTER TABLE expense_tracker.transaction
       ADD CONSTRAINT FKymgip1b572bwfjdelv9l6t9h
       foreign key (debit_account_id)
       references account (id);