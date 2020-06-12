package ru.temnik.databaseManager.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.temnik.databaseManager.Annotation.Table;

@Table(name="acts_admission")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ActAdm {
    private int id_acts;
    private int id_document;
    private int id_employee;
    private int id_reader;
    private String date;
}
