package view;

import javax.swing.*;

public class AlleAnzeigenView extends JFrame {

    private JList<String> alleDatensätzeListe;

    public AlleAnzeigenView() {
        setSize(400, 100);
        setTitle("Alle Datensätze");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addComponents();
        setVisible(true);
        // pack(); -- später nach dem Fix wieder rein
    }

    private void addComponents() {
        JScrollPane scrollPane = new JScrollPane();
        alleDatensätzeListe = new JList<>();
        scrollPane.setViewportView(alleDatensätzeListe);
        add(scrollPane);
        JButton exportButton = new JButton("Export");
        add(exportButton);
    }

    public void setAlleDatensätzeListeDefaultModel(DefaultListModel<String> defaultModel) {
        alleDatensätzeListe.setModel(defaultModel);
    }

    public static void main(String[] args) {
        new AlleAnzeigenView();
    }
}
