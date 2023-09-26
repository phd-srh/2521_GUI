package dao;

import model.Kategorie;
import model.Table;

import java.util.List;

public interface DAO {

    boolean insertTable(int id, Table table);    // Create
    Table getTable(int id);                      // Read
    List<Table> getAllTables();                  // Read Spezial
    boolean updateTable(int id, Table table);    // Update
    void deleteTable(int id);                    // Delete
    int getLastTableID();

    boolean insertKategorie(int id, Kategorie kategorie);
    Kategorie getKategorieByID(int id);
    Kategorie getKategorieByBezeichnung(String bezeichnung);
    List<Kategorie> getAllKategorien();
    boolean updateKategorie(int id, Kategorie kategorie);
    void deleteKategorie(int id);
    int getLastKategorieID();
}
