package ru.temnik.databaseManager.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.temnik.databaseManager.Annotation.Id;
import ru.temnik.databaseManager.Annotation.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "readers")
public class Reader {
    @Id
    private int id_reader;
    private String last_name;
    private String first_name;
    private String patronymic;
    private int id_degree;
    private String phone;

    @Override
    public String toString() {
        return "Reader{" +
                "id_reader=" + id_reader +
                ", last_name='" + last_name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", id_degree=" + id_degree +
                ", phone='" + phone + '\'' +
                '}';
    }
}
