package dao;

import java.util.List;

public interface DAO {

    boolean insertText(int id, String text);    // Create
    String getText(int id);                     // Read
    List<String> getAll();                      // Read Spezial
    boolean updateText(int id, String text);    // Update
    void deleteText(int id);                    // Delete
    int getLastID();

    boolean insertKategorie(int id, String bezeichnung);
    String getKategorie(int id);
    List<String> getAllKategorien();
    boolean updateKategorie(int id, String bezeichnung);
    void deleteKategorie(int id);
    int getLastKategorieID();
}
