package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AlleAnzeigenView extends JFrame {

    private JList<String> alleDatensätzeListe;
    private JButton exportButton;

    public AlleAnzeigenView() {
        setSize(400, 100);
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
