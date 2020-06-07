package ru.temnik.databaseManager.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.temnik.databaseManager.Annotation.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name="passports")
public class Passport {
    private int id_reader;
    private int series;
    private int number;
}
