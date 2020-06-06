package ru.temnik.databaseManager.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Employee {
    private int id_employee;
    private String last_name;
    private String first_name;
    private String patronymic;
    private int id_position;
}
