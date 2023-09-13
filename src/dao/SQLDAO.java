package dao;

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
    public boolean insertText(int id, String text) {
        try {
            PreparedStatement insertCommand = sqlConnection.prepareStatement(
                    "INSERT INTO `table` (id, text) VALUE (?, ?)"
            );
            insertCommand.setInt(1, id);
            insertCommand.setString(2, text);
            return (insertCommand.executeUpdate() == 1);
        }
        catch (SQLException e) {
            System.out.println("Probleme beim Einfügen eines Datensatzes");
            System.err.println( e.getMessage() );
        }
        return false;
    }

    @Override
    public String getText(int id) {
        try {
            PreparedStatement sqlCommand = sqlConnection.prepareStatement(
                    "SELECT text FROM `table` WHERE id = ?");
            sqlCommand.setInt(1, id);
            ResultSet sqlResult = sqlCommand.executeQuery();
            if ( sqlResult.next() ) {
                return sqlResult.getString("text");
            }
        }
        catch (SQLException e) {
            System.out.println("Probleme beim Anfragen des Datensatzes");
            System.err.println( e.getMessage() );
        }
        return null;
    }

    @Override
    public List<String> getAll() {
        ArrayList<String> ergebnisListe = new ArrayList<>();
        try {
            Statement sqlCommand = sqlConnection.createStatement();
            ResultSet sqlResult = sqlCommand.executeQuery("SELECT text FROM `table`");
            while ( sqlResult.next() ) {
                String text = sqlResult.getString("text");
                ergebnisListe.add( text );
            }
        }
        catch (SQLException e) {
            System.out.println("Probleme beim Anfragen der Datensätze");
            System.err.println( e.getMessage() );
        }
        return ergebnisListe;
    }

    @Override
    public boolean updateText(int id, String text) {
        deleteText(id);
        return insertText(id, text);
    }

    @Override
    public void deleteText(int id) {
        try {
            PreparedStatement deleteCommand = sqlConnection.prepareStatement(
                    "DELETE FROM `table` WHERE id = ?");
            deleteCommand.setInt(1, id);
            deleteCommand.execute();
        }
        catch (SQLException ignored) {}
    }

    @Override
    public int getLastID() {
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
    public boolean insertKategorie(int id, String bezeichnung) {
        try {
            PreparedStatement insertCommand = sqlConnection.prepareStatement(
                    "INSERT INTO `kategorie` (id, bezeichnung) VALUE (?, ?)"
            );
            insertCommand.setInt(1, id);
            insertCommand.setString(2, bezeichnung);
            return (insertCommand.executeUpdate() == 1);
        }
        catch (SQLException e) {
            System.out.println("Probleme beim Einfügen eines Datensatzes");
            System.err.println( e.getMessage() );
        }
        return false;
    }

    @Override
    public String getKategorie(int id) {
        return null;
    }

    @Override
    public List<String> getAllKategorien() {
        ArrayList<String> ergebnisListe = new ArrayList<>();
        try {
            Statement sqlCommand = sqlConnection.createStatement();
            ResultSet sqlResult = sqlCommand.executeQuery("SELECT bezeichnung FROM `kategorie`");
            while ( sqlResult.next() ) {
                String bezeichnung = sqlResult.getString("bezeichnung");
                ergebnisListe.add( bezeichnung );
            }
        }
        catch (SQLException e) {
            System.out.println("Probleme beim Anfragen der Datensätze");
            System.err.println( e.getMessage() );
        }
        return ergebnisListe;
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
