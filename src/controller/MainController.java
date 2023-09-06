package controller;

import dao.DAO;
import dao.SQLDAO;
import view.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainController {
    private DAO db;
    private MainView mainView;

    public MainController(DAO db, MainView mainView) {
        this.db = db;
        this.mainView = mainView;

        mainView.setAbfrageButtonListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int id = mainView.getID();
                        String text = db.getText(id);
                        mainView.setText(text);
                    }
                }
        );
    }

    public static void main(String[] args) {
        new MainController(new SQLDAO(), new MainView() );
    }
}
