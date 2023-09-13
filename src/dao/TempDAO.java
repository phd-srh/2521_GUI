package dao;

import model.Table;

import java.util.ArrayList;
import java.util.List;

public class TempDAO implements DAO {
    // interner Speicher
    private List<Table> tableList;

    public TempDAO() {
        tableList = new ArrayList<>();
        // einige Datensätze einfügen
        insertText( getLastID()+1, "Test1" );
        insertText( getLastID()+1, "Apfel" );
        insertText( getLastID()+1, "Keksdose" );
        insertText( getLastID()+1, "Bleistift" );
    }

    @Override
    public boolean insertText(int id, String text) {
        if (getText(id) == null) {
            tableList.add(new Table(id, text));
            return true;
        }
        else
            return false;
    }

    @Override
    public String getText(int id) {
        for (Table table : tableList) {
            if (table.getId() == id)
                return table.getText();
        }
        return null;
    }

    @Override
    public List<String> getAll() {
        List<String> stringListe = new ArrayList<>();
        for (Table table : tableList) {
            stringListe.add( table.getText() );
        }
        return stringListe;
    }

    @Override
    public boolean updateText(int id, String text) {
        deleteText(id);
        return insertText(id, text);
    }

    @Override
    public void deleteText(int id) {
        for (int i=0; i<tableList.size(); i++) {
            if (tableList.get(i).getId() == id) {
                tableList.remove(i);
                break;
            }
        }
    }

    @Override
    public int getLastID() {
        int lastID = 0;
        for (Table table : tableList) {
            if (table.getId() > lastID)
                lastID = table.getId();
        }
        return lastID;
    }

    @Override
    public boolean insertKategorie(int id, String bezeichnung) {
        return false;
    }

    @Override
    public String getKategorie(int id) {
        return null;
    }

    @Override
    public List<String> getAllKategorien() {
        return null;
    }

    @Override
    public boolean updateKategorie(int id, String bezeichnung) {
        return false;
    }

    @Override
    public void deleteKategorie(int id) {

    }

    @Override
    public int getLastKategorieID() {
        return 0;
    }
}
