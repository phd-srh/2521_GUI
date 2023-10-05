package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class AlleAnzeigenView extends JFrame {

    private JList<String> alleDatensätzeListe;
    private JButton exportButton;

    public AlleAnzeigenView() {
        setSize(300, 600);
        setTitle("Alle Datensätze");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addComponents();
        setVisible(true);
        // pack(); -- später nach dem Fix wieder rein
    }

    private void addComponents() {
        setLayout( new BorderLayout() );
        JScrollPane scrollPane = new JScrollPane();
        alleDatensätzeListe = new JList<>();
        scrollPane.setViewportView(alleDatensätzeListe);
        add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        exportButton = new JButton("Export");
        bottomPanel.add(exportButton);
        add(bottomPanel, BorderLayout.SOUTH);
        JPanel topPanel = new JPanel( new FlowLayout(FlowLayout.LEFT) );
        topPanel.setBorder( new EmptyBorder(1,1,1,1) );
        add(topPanel, BorderLayout.NORTH);
        JTextField textField = new JTextField();
        textField.setColumns(12);
        //textField.setBorder(new LineBorder(1, 1, 1, 1));
        topPanel.add( textField );
        //topPanel.add( new JLabel() );
        //topPanel.add( new JLabel() );
    }

    public void setExportButtonListener(ActionListener listener) {
        exportButton.addActionListener(listener);
    }

    public void setAlleDatensätzeListeDefaultModel(DefaultListModel<String> defaultModel) {
        alleDatensätzeListe.setModel(defaultModel);
    }

    public static void main(String[] args) {
        new AlleAnzeigenView();
    }
}
