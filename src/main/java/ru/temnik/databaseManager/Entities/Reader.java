package ru.temnik.databaseManager.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.temnik.databaseManager.Annotation.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name="readers")
public class Reader {
    private int id_reader;
    private String last_name;
    private String first_name;
    private String patronymic;
    private int id_degree;
    private String phone;
}
