package ru.temnik.databaseManager.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.temnik.databaseManager.Annotation.Id;
import ru.temnik.databaseManager.Annotation.Table;

@Table(name="document_statuses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DocumentStatus {
    @Id
    private int id_status;
    private String status_Name;

    public DocumentStatus(String status_Name) {
        this.status_Name = status_Name;
    }

    @Override
    public String toString() {
        return "DocumentStatus{" +
                "id_status=" + id_status +
                ", status_Name='" + status_Name + '\'' +
                '}';
    }
}
