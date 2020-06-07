package ru.temnik.databaseManager.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.temnik.databaseManager.Annotation.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name="documents")
public class Document {

    private int id_document;
    private String document_name;
    private int id_degrees;
    private int id_status;
    private String registration_number;
    private String date_of_creation;

}
