package controller;

import dao.DAO;
import dao.SQLDAO;
import dao.TempDAO;
import model.Kategorie;
import model.Table;
import view.AlleAnzeigenView;
import view.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {
    private DAO db;
    private MainView mainView;
    private Logger logger = Logger.getLogger("MainControl");
    private AlleAnzeigenView alleAnzeigenView;
    private Table letzterDatensatz;

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
        mainView.setRückwärtsButtonListener( this::performRückwärtsButton );
        mainView.setVorwärtsButtonListener( this::performVorwärtsButton );

        mainView.setKategorieKomboBoxListener( this::performKategorieKomboBox );
        mainView.setTextTextfeldKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                performTextfeldChanging();
            }
        });
    }

    private void performTextfeldChanging() {
        if (letzterDatensatz == null) return;

        String text = mainView.getText();
        if (letzterDatensatz.getText().equals(text)) {
            mainView.setFarbeTextTextfeld(Color.WHITE);
            if (mainView.getKategorie().equals(letzterDatensatz.getKategorie().getBezeichnung())) {
                mainView.setSpeichernButtonEnabled(false);
            }
        }
        else {
            mainView.setFarbeTextTextfeld(Color.RED);
            mainView.setSpeichernButtonEnabled(true);
        }
    }

    private void performKategorieKomboBox(ActionEvent e) {
        if (letzterDatensatz == null) return;

        String kategorieText = mainView.getKategorie();
        if (letzterDatensatz.getKategorie().getBezeichnung().equals(kategorieText)) {
            mainView.setFarbeKategorieKomboBox(Color.WHITE);
            if (mainView.getText().equals(letzterDatensatz.getText())) {
                mainView.setSpeichernButtonEnabled(false);
            }
        }
        else {
            mainView.setFarbeKategorieKomboBox(Color.RED);
            mainView.setSpeichernButtonEnabled(true);
        }
    }

    private void zeigeTextInConsole(ActionEvent e) {
        System.out.println("Holla, da ist was passiert!");
    }


    private void performVorwärtsButton(ActionEvent e) {
        int id = mainView.getID();
        Table table = null;
        do {
            id++;
            if (id > db.getLastTableID()) break;
            table = db.getTable(id);
        } while (table == null);
        if (table != null)
            zeigeTable(table);
    }

    private void performRückwärtsButton(ActionEvent e) {
        int id = mainView.getID();
        Table table = null;
        do {
            id--;
            if (id < 1) break;
            table = db.getTable(id);
        } while (table == null);
        if (table != null)
            zeigeTable(table);
    }

    private void performAlleAnzeigenButton(ActionEvent e) {
        // TODO
        // + neues Fenster zur Anzeige einer Liste
        // - entweder nur einmal eine Liste anzeigen, oder das Hauptfenster sperren
        // - Doppelklick in der List zeigt Datensatz im Hauptfenster an
        // + das neue Fenster soll auch einfach geschlossen werden können
        // + im Fenster soll ein Export-Button angezeigt werden
        mainView.setEnabled(false);

        alleAnzeigenView = new AlleAnzeigenView();
        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        alleAnzeigenView.setAlleDatensätzeListeDefaultModel(defaultListModel);
        for (Table table : db.getAllTables()) {
            defaultListModel.addElement( table.toString() );
        }
        alleAnzeigenView.setExportButtonListener( this::performExportButton );
        alleAnzeigenView.setTextFieldListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //System.out.println("keyTyped event!");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println("keyPressed event!");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String filter = alleAnzeigenView.getTextFieldText().toLowerCase();
                DefaultListModel<String> defaultListModel = new DefaultListModel<>();
                for (Table table : db.getAllTables()) {
                    String displayText = table.toString();
                    if ( displayText.toLowerCase().contains(filter) )
                        defaultListModel.addElement( displayText );
                }
                alleAnzeigenView.setAlleDatensätzeListeDefaultModel(defaultListModel);
            }
        });
        alleAnzeigenView.setAlleAnzeigenViewWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {}

            @Override
            public void windowClosed(WindowEvent e) {
                mainView.setEnabled(true);
                mainView.requestFocus();
            }

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        alleAnzeigenView.setAlleDatensätzeListeMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String element = alleAnzeigenView.getSelectedItem();
                    int nr = extractNumber(element);
                    Table table = db.getTable(nr);
                    zeigeTable(table);
                    alleAnzeigenView.dispose();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    private int extractNumber(String element) {
        int positionDesDoppelpunkts = element.indexOf(':');
        String number = element.substring(0, positionDesDoppelpunkts);
        return Integer.parseInt(number);
    }

    private void performExportButton(ActionEvent e) {
        // eine Datei öffnen (evtl. ein DateiÖffnenDialog anzeigen),
        // Inhalt des Listenfensters in Datei schreiben, Datei wieder schließen
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileHidingEnabled(false);
        fileChooser.setVisible(true);
        if (fileChooser.showSaveDialog(mainView) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filename = file.getAbsoluteFile().toString();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                // zweiter Filterdurchgang, weil Zugriff auf die Liste nicht möglich ist
                String filter = alleAnzeigenView.getTextFieldText().toLowerCase();
                for (Table table : db.getAllTables()) {
                    String displayText = table.toString();
                    if ( displayText.toLowerCase().contains(filter) )
                        writer.write( displayText + "\n");
                }
                writer.close();
            }
            catch (IOException exception) {
                System.out.println("Oh, nein, das ging nicht gut!");
                exception.printStackTrace();
            }
        }
        fileChooser.setVisible(false);
    }

    private void performHinzufügenButton(ActionEvent e) {
        int id = mainView.getID();
        String text = mainView.getText();
        String kategorieBezeichnung = mainView.getKategorie();
        boolean lecker = mainView.isLecker();

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
        Table neuerDatensatz = new Table(id, text, kategorie, lecker);
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
            mainView.setLecker(false);
        }
        else {
            letzterDatensatz = t.clone();
            mainView.setID(t.getId());
            mainView.setText(t.getText());
            mainView.setKategorie(t.getKategorie().getBezeichnung());
            mainView.setLecker(t.isLecker());
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
