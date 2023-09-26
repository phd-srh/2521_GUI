package controller;

import dao.DAO;
import dao.SQLDAO;
import model.Kategorie;
import model.Table;
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
    }

    private void performAbfragenButton(ActionEvent e) {
        int id = mainView.getID();
        logger.info("Bis hier läufts");
        Table table = db.getTable(id);
        if (table != null) {
            mainView.setText(table.getText());
            logger.info("Das sehe ich schon nicht mehr!");
            mainView.setKategorie(table.getKategorie().getBezeichnung());
        }
        else
            mainView.showMessage("Datensatz nicht vorhanden!");
    }

    public static void main(String[] args) {
        new MainController(new SQLDAO(), new MainView() );
    }
}
