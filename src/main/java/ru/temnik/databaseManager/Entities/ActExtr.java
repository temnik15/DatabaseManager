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
@Table(name="acts_extradition")
public class ActExtr {
    @Id
    private int id_acts;
    private int id_document;
    private int id_employee;
    private int id_reader;
    private String date;
}
