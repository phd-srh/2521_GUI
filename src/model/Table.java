package model;

// Value Class
public class Table /* extends Object */ {
    private int id;
    private String text;
    private Kategorie kategorie;
    private boolean lecker;

    public Table(int id, String text, Kategorie kategorie, boolean lecker) {
        this.id = id;
        this.text = text;
        this.kategorie = kategorie;
        this.lecker = lecker;
    }

    public boolean isLecker() {
        return lecker;
    }

    public void setLecker(boolean lecker) {
        this.lecker = lecker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Kategorie getKategorie() {
        return kategorie;
    }

    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    public Table clone() {
        return new Table(id, text, kategorie, lecker);
    }

    @Override
    public String toString() {
        return id + ": " + text + " (" + kategorie + ") [" + lecker + "]";
    }
}
