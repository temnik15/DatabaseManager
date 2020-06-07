package ru.temnik.databaseManager.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.temnik.databaseManager.Annotation.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name="positions_employees")
public class PositionEmployee {
    private int id_position;
    private String position_name;
}
