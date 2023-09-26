package dao;

import model.Kategorie;
import model.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLDAO implements DAO {
    private static final String SQL_DRIVER   = "org.mariadb.jdbc.Driver";
    private static final String SQL_SERVER   = "127.0.0.1";
    private static final String SQL_PORT     = "3306";
    private static final String SQL_DATABASE = "dbtest";
    private static final String SQL_USER     = "dbtestadm";
    private static final String SQL_PASSWORD = "geheim123";

    private Connection sqlConnection = null;

    public SQLDAO() {
        try {
            Class.forName(SQL_DRIVER);
        }
        catch (ClassNotFoundException e) {
            System.err.println("MariaDB SQL Connector ist nicht installiert.");
            System.err.println( e.getMessage() );
            //e.printStackTrace();
            System.exit(-1);
        }
        openConnection();
    }

    private void openConnection() {
        try {
            sqlConnection =
            DriverManager.getConnection(
                    "jdbc:mariadb://" + SQL_SERVER + ":" + SQL_PORT + "/" + SQL_DATABASE,
                    SQL_USER, SQL_PASSWORD);
        }
        catch (SQLException e) {
            System.out.println("Probleme beim Aufbau der Verbindung");
            System.err.println( e.getMessage() );
            System.exit(-2);
        }
    }

    private void closeConnection() {
        try {
            if (sqlConnection != null)
                sqlConnection.close();
        }
        catch (SQLException ignored) {}
    }

    @Override
    public boolean insertTable(int id, Table table) {
        try {
            int kid = table.getKategorie().getId();
            if (getKategorieByID(kid) == null) {
                if (!insertKategorie(kid, table.getKategorie()))
                    return false;
            }

            PreparedStatement insertCommand = sqlConnection.prepareStatement(
                    "INSERT INTO `table` (id, text, katid) VALUE (?, ?, ?)"
            );
            insertCommand.setInt(1, id);
            insertCommand.setString(2, table.getText());
            insertCommand.setInt(3, kid);
            return (insertCommand.executeUpdate() == 1);
        }
        catch (SQLException e) {
            System.out.println("Probleme beim Einfügen eines Datensatzes");
            System.err.println( e.getMessage() );
        }
        return false;
    }

    @Override
    public Table getTable(int id) {
        try {
            PreparedStatement sqlCommand = sqlConnection.prepareStatement("""
                      SELECT t.id AS tid, text, k.id AS kid, bezeichnung
                      FROM `table` AS t, kategorie AS k
                      WHERE t.katid = k.id
                      AND t.id = ?
                            """);
            sqlCommand.setInt(1, id);
            ResultSet sqlResult = sqlCommand.executeQuery();
            if ( sqlResult.next() ) {
                return getTableResultEntry(sqlResult);
            }
        }
        catch (SQLException e) {
            System.out.println("Probleme beim Anfragen des Datensatzes");
            System.err.println( e.getMessage() );
        }
        return null;
    }

    private Table getTableResultEntry(ResultSet sqlResult) throws SQLException {
        int katid = sqlResult.getInt("kid");
        String bezeichnung = sqlResult.getString("bezeichnung");
        Kategorie kategorie = new Kategorie(katid, bezeichnung);
        int tid = sqlResult.getInt("tid");
        String text = sqlResult.getString("text");
        return new Table(tid, text, kategorie);
    }

    @Override
    public List<Table> getAllTables() {
        ArrayList<Table> ergebnisListe = new ArrayList<>();
        try {
            Statement sqlCommand = sqlConnection.createStatement();
            ResultSet sqlResult = sqlCommand.executeQuery("""
                    SELECT t.id AS tid, text, k.id AS kid, bezeichnung
                    FROM `table` AS t, kategorie AS k
                    WHERE t.katid = k.id
                    """);
            while ( sqlResult.next() ) {
                ergebnisListe.add( getTableResultEntry(sqlResult) );
            }
        }
        catch (SQLException e) {
            System.out.println("Probleme beim Anfragen der Datensätze");
            System.err.println( e.getMessage() );
        }
        return ergebnisListe;
    }

    @Override
    public boolean updateTable(int id, Table table) {
        deleteTable(id);
        return insertTable(id, table);
    }

    @Override
    public void deleteTable(int id) {
        try {
            PreparedStatement deleteCommand = sqlConnection.prepareStatement(
                    "DELETE FROM `table` WHERE id = ?");
            deleteCommand.setInt(1, id);
            deleteCommand.execute();
        }
        catch (SQLException ignored) {}
    }

    @Override
    public int getLastTableID() {
        try {
            Statement sqlCommand = sqlConnection.createStatement();
            ResultSet sqlResult = sqlCommand.executeQuery(
                "SELECT MAX(id) AS maxid FROM `table`"
            );
            if (sqlResult.next())
                return sqlResult.getInt("maxid");
        }
        catch (SQLException ignored) {}
        return 0;
    }

    @Override
    public boolean insertKategorie(int id, Kategorie kategorie) {
        try {
            PreparedStatement insertCommand = sqlConnection.prepareStatement(
                    "INSERT INTO `kategorie` (id, bezeichnung) VALUE (?, ?)"
            );
            insertCommand.setInt(1, id);
            insertCommand.setString(2, kategorie.getBezeichnung());
            return (insertCommand.executeUpdate() == 1);
        }
        catch (SQLException e) {
            System.out.println("Probleme beim Einfügen eines Datensatzes");
            System.err.println( e.getMessage() );
        }
        return false;
    }

    private Kategorie getKategorieResultEntry(ResultSet sqlResult) throws SQLException {
        return new Kategorie(sqlResult.getInt("id"),
                        sqlResult.getString("bezeichnung"));
    }

    @Override
    public Kategorie getKategorieByID(int id) {
        try {
            PreparedStatement sqlCommand = sqlConnection.prepareStatement(
                    "SELECT id, bezeichnung FROM `kategorie` WHERE id = ?");
            sqlCommand.setInt(1, id);
            ResultSet sqlResult = sqlCommand.executeQuery();
            if ( sqlResult.next() ) {
                return getKategorieResultEntry(sqlResult);
            }
        }
        catch (SQLException e) {
            System.out.println("Probleme beim Anfragen der Datensätze");
            System.err.println( e.getMessage() );
        }
        return null;
    }

    @Override
    public Kategorie getKategorieByBezeichnung(String bezeichnung) {
        try {
            PreparedStatement sqlCommand = sqlConnection.prepareStatement(
                    "SELECT id, bezeichnung FROM `kategorie` WHERE bezeichnung LIKE ?");
            sqlCommand.setString(1, bezeichnung);
            ResultSet sqlResult = sqlCommand.executeQuery();
            if ( sqlResult.next() ) {
                return getKategorieResultEntry(sqlResult);
            }
        }
        catch (SQLException e) {
            System.out.println("Probleme beim Anfragen der Datensätze");
            System.err.println( e.getMessage() );
        }
        return null;
    }

    @Override
    public List<Kategorie> getAllKategorien() {
        ArrayList<Kategorie> ergebnisListe = new ArrayList<>();
        try {
            Statement sqlCommand = sqlConnection.createStatement();
            ResultSet sqlResult = sqlCommand.executeQuery(
                    "SELECT id, bezeichnung FROM `kategorie`");
            while ( sqlResult.next() ) {
                ergebnisListe.add( getKategorieResultEntry(sqlResult) );
            }
        }
        catch (SQLException e) {
            System.out.println("Probleme beim Anfragen der Datensätze");
            System.err.println( e.getMessage() );
        }
        return ergebnisListe;
    }

    @Override
    public boolean updateKategorie(int id, Kategorie kategorie) {
        deleteKategorie(id);
        return insertKategorie(id, kategorie);
    }

    @Override
    public void deleteKategorie(int id) {
        try {
            PreparedStatement deleteCommand = sqlConnection.prepareStatement(
                    "DELETE FROM kategorie WHERE id = ?");
            deleteCommand.setInt(1, id);
            deleteCommand.execute();
        }
        catch (SQLException ignored) {}
    }

    @Override
    public int getLastKategorieID() {
        try {
            Statement sqlCommand = sqlConnection.createStatement();
            ResultSet sqlResult = sqlCommand.executeQuery(
                    "SELECT MAX(id) AS maxid FROM kategorie"
            );
            if (sqlResult.next())
                return sqlResult.getInt("maxid");
        }
        catch (SQLException ignored) {}
        return 0;
    }
}
