package controller;

import dao.DAO;
import dao.SQLDAO;
import model.Kategorie;
import model.Table;
import view.AlleAnzeigenView;
import view.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {
    private DAO db;
    private MainView mainView;
    private Logger logger = Logger.getLogger("MainControl");

    public MainController(DAO db, MainView mainView) {
        this.db = db;
        this.mainView = mainView;

        logger.setLevel(Level.OFF);
        logger.info("Logger beginnt mit seiner Arbeit");

        DefaultComboBoxModel<String> kategorieModel = new DefaultComboBoxModel<>();
        for (Kategorie kategorie : db.getAllKategorien()) {
            kategorieModel.addElement( kategorie.getBezeichnung() );
        }
        mainView.setKategorieKomboBoxModel(kategorieModel);

        mainView.setAbfrageButtonListener( this::performAbfragenButton );
        mainView.setHinzufügenButtonListener( this::performHinzufügenButton );
        mainView.setLöschenButtonListener( this::performLöschenButton );
        mainView.setAlleAnzeigenButtonListener( this::performAlleAnzeigenButton );
    }

    private void performAlleAnzeigenButton(ActionEvent e) {
        // TODO
        // + neues Fenster zur Anzeige einer Liste
        // - entweder nur einmal eine Liste anzeigen, oder das Hauptfenster sperren
        // - Doppelklick in der List zeigt Datensatz im Hauptfenster an
        // + das neue Fenster soll auch einfach geschlossen werden können
        // - im Fenster soll ein Export-Button angezeigt werden
        AlleAnzeigenView alleAnzeigenView = new AlleAnzeigenView();
        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        alleAnzeigenView.setAlleDatensätzeListeDefaultModel(defaultListModel);
        for (Table table : db.getAllTables()) {
            defaultListModel.addElement( table.toString() );
        }
        alleAnzeigenView.setExportButtonListener( this::performExportButton );
    }

    private void performExportButton(ActionEvent e) {
        System.out.println("Da hat einer den Export Button geklickt");
        // TODO: hier soll natürlich mehr passieren
        // z.B. eine Datei öffnen (evtl. ein DateiÖffnenDialog anzeigen),
        // Inhalt des Listenfensters in Datei schreiben, Datei wieder schließen
    }

    private void performHinzufügenButton(ActionEvent e) {
        int id = mainView.getID();
        String text = mainView.getText();
        String kategorieBezeichnung = mainView.getKategorie();

        if (text.isBlank() || kategorieBezeichnung.isBlank()) {
            mainView.showWarning("Bitte sinnvolle Daten eingeben!");
            return;
        }

        if (id == 0) {
            id = db.getLastTableID() + 1;
            mainView.setID(id);
        }

        Kategorie kategorie = db.getKategorieByBezeichnung(kategorieBezeichnung);
        if (kategorie == null) {
            int kategorieID = db.getLastKategorieID() + 1;
            kategorie = new Kategorie(kategorieID, kategorieBezeichnung);
            db.insertKategorie(kategorieID, kategorie);
        }
        Table neuerDatensatz = new Table(id, text, kategorie);
        if ( db.insertTable(id, neuerDatensatz) )
            mainView.showMessage("Datensatz erfolgreich hinzugefügt!");
        else
            mainView.showWarning("Fehler beim Einfügen des neuen Datensatzes");
    }

    private void performLöschenButton(ActionEvent e) {
        int id = mainView.getID();
        Table table = db.getTable(id);
        if (table != null &&
                mainView.showConfirmation("Diesen Datensatz wirklich löschen")) {
            db.deleteTable(id);
            zeigeTable(null); // Eingabefelder löschen
        }
    }

    private void zeigeTable(Table t) {
        if (t == null) {
            mainView.clearID();
            mainView.setText("");
            mainView.setKategorie("");
        }
        else {
            mainView.setID(t.getId());
            mainView.setText(t.getText());
            mainView.setKategorie(t.getKategorie().getBezeichnung());
        }
    }

    private void performAbfragenButton(ActionEvent e) {
        int id = mainView.getID();
        logger.info("Bis hier läufts");
        Table table = db.getTable(id);
        if (table != null) {
            logger.info("Das sehe ich schon nicht mehr!");
            zeigeTable(table);
        }
        else
            mainView.showWarning("Datensatz nicht vorhanden!");
    }

    public static void main(String[] args) {
        new MainController(new SQLDAO(), new MainView() );
    }
}
