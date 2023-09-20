package dao;

import model.Kategorie;
import model.Table;

import java.util.ArrayList;
import java.util.List;

public class TempDAO implements DAO {
    // interner Speicher
    private List<Table> tableList;
    private List<Kategorie> kategorieList;

    public TempDAO() {
        tableList = new ArrayList<>();
        kategorieList = new ArrayList<>();

        insertKategorie( 1, new Kategorie(1, "Tisch") );
        insertKategorie( 2, new Kategorie(2, "Schrank") );
        insertKategorie( 3, new Kategorie(3, "Stuhl") );

        // einige Datensätze einfügen
        insertTable( 1, new Table(1, "Test1", getKategorie(1)) );
        insertTable( 2, new Table(2, "Apfel", getKategorie(2)) );
        insertTable( 3, new Table(3, "Keksdose", getKategorie(3)) );
        insertTable( 4, new Table(4, "Bleistift", getKategorie(2)) );
    }

    @Override
    public boolean insertTable(int id, Table table) {
        if (getTable(id) == null) {
            tableList.add( table.clone() );
            return true;
        }
        else
            return false;
    }

    @Override
    public Table getTable(int id) {
        for (Table table : tableList) {
            if (table.getId() == id)
                return table.clone();
        }
        return null;
    }

    @Override
    public List<Table> getAllTables() {
        List<Table> copyListe = new ArrayList<>();
        for (Table table : this.tableList) {
            copyListe.add( table.clone() );
        }
        return copyListe;
    }

    @Override
    public boolean updateTable(int id, Table table) {
        deleteTable(id);
        return insertTable(id, table);
    }

    @Override
    public void deleteTable(int id) {
        for (int i=0; i<tableList.size(); i++) {
            if (tableList.get(i).getId() == id) {
                tableList.remove(i);
                break;
            }
        }
    }

    @Override
    public int getLastTableID() {
        int lastID = 0;
        for (Table table : tableList) {
            if (table.getId() > lastID)
                lastID = table.getId();
        }
        return lastID;
    }

    @Override
    public boolean insertKategorie(int id, Kategorie kategorie) {
        return false;
    }

    @Override
    public Kategorie getKategorie(int id) {
        return null;
    }

    @Override
    public List<Kategorie> getAllKategorien() {
        return null;
    }

    @Override
    public boolean updateKategorie(int id, Kategorie kategorie) {
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
