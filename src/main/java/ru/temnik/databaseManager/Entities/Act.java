package ru.temnik.databaseManager.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Acts {
    private int id_acts;
    private int id_document;
    private int id_employee;
    private int id_reader;
    private String date;
}
