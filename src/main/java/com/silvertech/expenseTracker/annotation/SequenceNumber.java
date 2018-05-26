package com.silvertech.expenseTracker.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "SequenceNumber", uniqueConstraints = {@UniqueConstraint(columnNames = {"className"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SequenceNumber {
    @Id
    @Column(name = "className", updatable = false)
    private String className;

    @Column(name = "nextValue")
    private Integer nextValue = 1;

    @Column(name = "incrementValue")
    private Integer incrementValue = 1;
}