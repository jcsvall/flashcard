package flashcard.model;

import javafx.scene.layout.AnchorPane;

import java.sql.Timestamp;

public class FlashCard {

    private int id;
    private String adelante;
    private String atras;
    private String colorFondo;
    private String colorLetra;
    private String catgoria;
    private String adelanteAtras;
    private Timestamp datecreated;

    private boolean isSelected;

    //no database something like transients
    private AnchorPane anchorPane;

    public FlashCard() {
    }

    public FlashCard(int id, String adelante, String atras, String colorFondo, String colorLetra, String catgoria, Timestamp datecreated) {
        this.id = id;
        this.adelante = adelante;
        this.atras = atras;
        this.colorFondo = colorFondo;
        this.colorLetra = colorLetra;
        this.catgoria = catgoria;
        this.datecreated = datecreated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdelante() {
        return adelante;
    }

    public void setAdelante(String adelante) {
        this.adelante = adelante;
    }

    public String getAtras() {
        return atras;
    }

    public void setAtras(String atras) {
        this.atras = atras;
    }

    public String getColorFondo() {
        return colorFondo;
    }

    public void setColorFondo(String colorFondo) {
        this.colorFondo = colorFondo;
    }

    public String getColorLetra() {
        return colorLetra;
    }

    public void setColorLetra(String colorLetra) {
        this.colorLetra = colorLetra;
    }

    public String getCatgoria() {
        return catgoria;
    }

    public void setCatgoria(String catgoria) {
        this.catgoria = catgoria;
    }

    public Timestamp getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Timestamp datecreated) {
        this.datecreated = datecreated;
    }

    public String getAdelanteAtras() {
        return adelanteAtras;
    }

    public void setAdelanteAtras(String adelanteAtrar) {
        this.adelanteAtras = adelanteAtrar;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "FlashCard{" +
                "id=" + id +
                ", adelante='" + adelante + '\'' +
                ", atras='" + atras + '\'' +
                ", colorFondo='" + colorFondo + '\'' +
                ", colorLetra='" + colorLetra + '\'' +
                ", catgoria='" + catgoria + '\'' +
                ", datecreated=" + datecreated +
                '}';
    }
}
