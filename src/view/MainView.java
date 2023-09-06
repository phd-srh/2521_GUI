package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainView extends JFrame {

    private JTextField idTextfeld;
    private JTextField textTextfeld;
    private JButton abfrageButton;
    private JButton löschenButton;

    public MainView() {
        setSize(400, 200);
        setTitle("GUI Datenbank");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addComponents();
        setVisible(true);
        pack();
    }

    private void addComponents() {
        // Hauptfenster
        setLayout( new BorderLayout() );
        JPanel topPanel = new JPanel( new FlowLayout() );
        JPanel centerPanel = new JPanel( new GridLayout(2, 2) );
        JPanel bottomPanel = new JPanel( new FlowLayout() );
        add( topPanel, BorderLayout.NORTH );
        add( centerPanel, BorderLayout.CENTER );
        add( bottomPanel, BorderLayout.SOUTH );

        // topPanel
        topPanel.add( new JLabel("Zur Verwaltung von einfachen Texten") );

        // centerPanel
        centerPanel.setBorder( new EmptyBorder(5,5,5,5) );
        centerPanel.add( new JLabel("ID:") );
        idTextfeld = new JTextField();
        centerPanel.add( idTextfeld );
        centerPanel.add( new JLabel("Text:") );
        textTextfeld = new JTextField();
        centerPanel.add( textTextfeld );

        // bottomPanel
        abfrageButton = new JButton("Abfrage");
        löschenButton = new JButton("Löschen");
        bottomPanel.add( abfrageButton );
        bottomPanel.add( löschenButton );
    }

    public int getID() {
        return Integer.parseInt( idTextfeld.getText() );
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

    public void setAbfrageButtonListener(ActionListener listener) {
        abfrageButton.addActionListener(listener);
    }
}
