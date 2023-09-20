package controller;

import dao.DAO;
import dao.SQLDAO;
import model.Kategorie;
import model.Table;
import view.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainController {
    private DAO db;
    private MainView mainView;

    public MainController(DAO db, MainView mainView) {
        this.db = db;
        this.mainView = mainView;

        DefaultComboBoxModel<String> kategorieModel = new DefaultComboBoxModel<>();
        for (Kategorie kategorie : db.getAllKategorien()) {
            kategorieModel.addElement( kategorie.getBezeichnung() );
        }
        mainView.setKategorieKomboBoxModel(kategorieModel);

        mainView.setAbfrageButtonListener( this::performAbfragenButton );
    }

    private void performAbfragenButton(ActionEvent e) {
        int id = mainView.getID();
        Table table = db.getTable(id);
        mainView.setText(table.getText());
        mainView.setKategorie(table.getKategorie().getBezeichnung());
    }

    public static void main(String[] args) {
        new MainController(new SQLDAO(), new MainView() );
    }
}
