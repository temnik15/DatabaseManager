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
@Table(name="degrees_of_secrecy")
public class DegreeOfSecrecy {
    @Id
    private int id_degree;
    private String degree_name;
}
