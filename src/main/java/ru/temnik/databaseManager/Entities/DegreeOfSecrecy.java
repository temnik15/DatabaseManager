package ru.temnik.databaseManager.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.temnik.databaseManager.Annotation.Table;

@Table(name="degrees_of_secrecy")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DegreeOfSecrecy {
    private int id_degree;
    private String degree_name;
}
