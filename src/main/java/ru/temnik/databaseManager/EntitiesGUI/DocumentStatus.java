package ru.temnik.databaseManager.EntitiesGUI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class DocumentStatus{


    private SimpleIntegerProperty id_status;
    private SimpleStringProperty status_Name;

    public DocumentStatus(int id, String statusName) {
        this.id_status = new SimpleIntegerProperty(id);
        this.status_Name = new SimpleStringProperty(statusName);
    }

    public int getId() {
        return id_status.get();
    }
    public String getStatusName() {
        return status_Name.get();
    }

}
