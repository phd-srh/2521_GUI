package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {

    private MainView mainView;
    private JTextField idTextfeld;
    private JTextField textTextfeld;
    private JButton abfrageButton, hinzufügenButton, löschenButton, alleAnzeigenButton;
    private JButton vorwärtsButton, rückwärtsButton;
    private JComboBox<String> kategorieKomboBox;


    private class ExitButtonDispose implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Eigene Klasse schließt MainView");
            mainView.dispose();
        }
    }


    public MainView() {
        this.mainView = this;
        setSize(400, 200);
        setTitle("GUI Datenbank");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addComponents();
        setVisible(true);
        pack();
    }

    private void addComponents() {
        // Hauptfenster
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel centerPanel = new JPanel(new GridLayout(3, 2));
        JPanel bottomPanel = new JPanel(new FlowLayout());
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // topPanel
        topPanel.add(new JLabel("Zur Verwaltung von einfachen Texten"));

        // centerPanel
        centerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        centerPanel.add(new JLabel("ID:"));
        idTextfeld = new JTextField();
        idTextfeld.setToolTipText("bei 0 wird beim Hinzufügen die nächste freie ID benutzt");
        centerPanel.add(idTextfeld);
        centerPanel.add(new JLabel("Text:"));
        textTextfeld = new JTextField();
        centerPanel.add(textTextfeld);
        centerPanel.add(new JLabel("Kategorie:"));
        kategorieKomboBox = new JComboBox<>();
        kategorieKomboBox.setEditable(true);
        centerPanel.add(kategorieKomboBox);

        // bottomPanel
        abfrageButton = new JButton("Abfrage");
        hinzufügenButton = new JButton("Hinzufügen");
        löschenButton = new JButton("Löschen");
        alleAnzeigenButton = new JButton("Alle anzeigen");
        JButton exitButton = new JButton("Beenden");
        vorwärtsButton = new JButton("->");
        rückwärtsButton = new JButton("<-");
        bottomPanel.add(rückwärtsButton);
        bottomPanel.add(abfrageButton);
        bottomPanel.add(hinzufügenButton);
        bottomPanel.add(löschenButton);
        bottomPanel.add(alleAnzeigenButton);
        bottomPanel.add(exitButton);
        bottomPanel.add(vorwärtsButton);

        // ExitButton mit Leben füllen:
        // Methode (a) - eigene Klasse
//        exitButton.addActionListener( new ExitButtonDispose() );

        // Methode (b) - anonyme Klasse
//        exitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Anonyme Klasse schließt MainView");
//                mainView.dispose();
//            }
//        });

        // Methode (c) - Lambda Ausdruck
        exitButton.addActionListener(
                (ActionEvent e) -> {
                    //System.out.println("Lambda Ausdruck schließt MainView");
                    //showMessage("Lambda Ausdruck schließt MainView");
// - das nervt -   if ( showConfirmation("Wirklich beenden?") )
                        this.dispose();
                }
        );

        // Methode (d) - Funktionales Interface
//        exitButton.addActionListener( this::machWasWennManDenExitButtonKlickt );
    }

    private void machWasWennManDenExitButtonKlickt(ActionEvent e) {
        System.out.println("Funktionales Interface schließt MainView");
        this.dispose();
    }

    public int getID() {
        int id = 0;
        try {
            id = Integer.parseInt(idTextfeld.getText());
        }
        catch (NumberFormatException e) {
            showWarning("Fehlerhafte Eingabe der ID");
        }
        return id;
    }

    public void clearID() {
        idTextfeld.setText("");
    }

    public void setID(int id) {
        idTextfeld.setText( String.valueOf(id) );
    }

    public String getText() {
        return textTextfeld.getText();
    }

    public void setText(String text) {
        textTextfeld.setText(text);
    }

    public String getKategorie() {
        return (String)kategorieKomboBox.getSelectedItem();
    }

    public void setKategorie(String kategorie) {
        if (kategorie == null || kategorie.isBlank()) {
            kategorieKomboBox.setSelectedIndex(0);
            return;
        }

        for (int i=0; i < kategorieKomboBox.getItemCount(); i++) {
            if (kategorieKomboBox.getItemAt(i).equals(kategorie)) {
                kategorieKomboBox.setSelectedIndex(i);
                return;
            }
        }
        // nicht gefunden, Kategorie muss ergänzt werden
        kategorieKomboBox.addItem(kategorie);
        kategorieKomboBox.setSelectedIndex( kategorieKomboBox.getItemCount()-1 );
    }

    public void showWarning(String message) {
         JOptionPane.showMessageDialog(this, message, "Warnung",
                 JOptionPane.WARNING_MESSAGE);
        //System.out.println("Feddisch mit Message!?");
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean showConfirmation(String question) {
        return JOptionPane.showConfirmDialog(this, question, "Nachfrage",
                JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION;
    }

    public void setAbfrageButtonListener(ActionListener listener) {
        abfrageButton.addActionListener(listener);
    }

    public void setHinzufügenButtonListener(ActionListener listener) {
        hinzufügenButton.addActionListener(listener);
    }

    public void setLöschenButtonListener(ActionListener listener) {
        löschenButton.addActionListener(listener);
    }

    public void setAlleAnzeigenButtonListener(ActionListener listener) {
        alleAnzeigenButton.addActionListener(listener);
    }

    public void setVorwärtsButtonListener(ActionListener listener) {
        vorwärtsButton.addActionListener(listener);
    }

    public void setRückwärtsButtonListener(ActionListener listener) {
        rückwärtsButton.addActionListener(listener);
    }

    public void setKategorieKomboBoxModel(DefaultComboBoxModel<String> kategorieModel) {
        kategorieKomboBox.setModel(kategorieModel);
    }

    public static void main(String[] args) {
        // reiner Mockup, ganz ohne Funktionen
        new MainView();
    }
}
