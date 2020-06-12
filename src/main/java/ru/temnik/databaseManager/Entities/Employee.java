package ru.temnik.databaseManager.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.temnik.databaseManager.Annotation.Id;
import ru.temnik.databaseManager.Annotation.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name="employees")
public class Employee {
    @Id
    private int id_employee;
    private String last_name;
    private String first_name;
    private String patronymic;
    private int id_position;
}
