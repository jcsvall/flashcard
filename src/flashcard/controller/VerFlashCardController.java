package flashcard.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import flashcard.Database.DatabaseHandler;
import flashcard.animations.Fader;
import flashcard.animations.FaderV2;
import flashcard.animations.Shaker;
import flashcard.model.FlashCard;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class VerFlashCardController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private Label textoLabel;

    @FXML
    private ImageView siguienteBtn;

    @FXML
    private ImageView atrasBtn;

    @FXML
    private ImageView girarBtn;


    @FXML
    private Label conteoHeaderLabel;

    @FXML
    private AnchorPane respuestaEliminarPanel;

    @FXML
    private JFXButton eliminarBtnSi;

    @FXML
    private JFXButton eliminarBtnNo;

    @FXML
    private JFXCheckBox selected;

    private JFXListView<FlashCard> listView;

    int position = 0;
    boolean frenteAtras = true;

    @FXML
    void initialize() {
        siguienteBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            setSiguiente();
        });
        atrasBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            setAtras();
        });
        girarBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            setGirar();
        });

        eliminarBtnSi.setOnAction(actionEvent -> {
            eliminar();
        });
        eliminarBtnNo.setOnAction(actionEvent -> {
            eliminarBtnNo.getScene().getWindow().hide();
        });
        selected.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            updateIsSelected();
        });
    }

    public void setListView(JFXListView<FlashCard> listViewElements){
        this.listView = listViewElements;
        if(!listView.getItems().isEmpty()){
            FlashCard firstElement = listView.getItems().get(position);
            textoLabel.setText(String.valueOf(firstElement.getAdelante()));
            textoLabel.setStyle("-fx-background-color: "+firstElement.getColorFondo()+
                    "; -fx-text-fill:"+firstElement.getColorLetra());
            conteoHeaderLabel.setText(position+1+"/"+listView.getItems().size());
        }else {
            textoLabel.setText(String.valueOf(""));
            conteoHeaderLabel.setText(position+"/"+listView.getItems().size());
            siguienteBtn.setVisible(false);
            atrasBtn.setVisible(false);
            girarBtn.setVisible(false);
        }

    }

    public void verOneFlashCard(JFXListView<FlashCard> listViewElements){
        this.listView = listViewElements;

            FlashCard firstElement = listView.getItems().get(position);
            textoLabel.setText(String.valueOf(firstElement.getAdelante()));
            textoLabel.setStyle("-fx-background-color: "+firstElement.getColorFondo()+
                    "; -fx-text-fill:"+firstElement.getColorLetra());
            conteoHeaderLabel.setText("");
            siguienteBtn.setVisible(false);
            atrasBtn.setVisible(false);
    }

    private void setSiguiente(){
        Shaker buttonShaker = new Shaker(siguienteBtn);
        buttonShaker.shake();

        position++;
        if(position >= listView.getItems().size()){
            //position =  listView.getItems().size() - 1;
            position = 0;
        }
        FlashCard fc = listView.getItems().get(position);
        textoLabel.setText(String.valueOf(fc.getAdelante()));
        textoLabel.setStyle("-fx-background-color: "+fc.getColorFondo()+
                "; -fx-text-fill:"+fc.getColorLetra());

        conteoHeaderLabel.setText(position+1+"/"+listView.getItems().size());

        frenteAtras = true;

    }

    private void setAtras(){
        Shaker buttonShaker = new Shaker(atrasBtn);
        buttonShaker.shake();

        position--;
        if(position <= 0){
            position =  0;
        }

        FlashCard fc = listView.getItems().get(position);
        textoLabel.setText(String.valueOf(fc.getAdelante()));
        textoLabel.setStyle("-fx-background-color: "+fc.getColorFondo()+
                "; -fx-text-fill:"+fc.getColorLetra());

        conteoHeaderLabel.setText(position+1+"/"+listView.getItems().size());

        frenteAtras = true;

    }

    private void setGirar(){
        Shaker buttonShaker = new Shaker(girarBtn);
        buttonShaker.shake();

        FaderV2 labelFader = new FaderV2(textoLabel,1500, false);
        labelFader.fade();

        FlashCard flashCard = listView.getItems().get(position);
        frenteAtras = !frenteAtras;
        if(frenteAtras){
            textoLabel.setText(String.valueOf(flashCard.getAdelante()));
        }else{
            textoLabel.setText(String.valueOf(flashCard.getAtras()));
        }
        System.out.println(frenteAtras);
    }

    public void verEliminarFlashCard(JFXListView<FlashCard> listViewElements){
        this.listView = listViewElements;

        FlashCard firstElement = listView.getItems().get(position);
        textoLabel.setText(String.valueOf(firstElement.getAdelante()));
        textoLabel.setStyle("-fx-background-color: "+firstElement.getColorFondo()+
                "; -fx-text-fill:"+firstElement.getColorLetra());
        conteoHeaderLabel.setText("");
        siguienteBtn.setVisible(false);
        atrasBtn.setVisible(false);

        respuestaEliminarPanel.setVisible(true);
    }

    public void eliminar(){
        FlashCard flashCard = listView.getItems().get(0);
        DatabaseHandler dbh = new DatabaseHandler();
        dbh.deleteFlashCard(flashCard);
        FlashCardController.flashCardController.refresh();
        eliminarBtnSi.getScene().getWindow().hide();
    }

    public void setListViewSelected(JFXListView<FlashCard> listViewElements){
        this.listView = listViewElements;
        if(!listView.getItems().isEmpty()){
            FlashCard firstElement = listView.getItems().get(position);
            textoLabel.setText(String.valueOf(firstElement.getAdelante()));
            textoLabel.setStyle("-fx-background-color: "+firstElement.getColorFondo()+
                    "; -fx-text-fill:"+firstElement.getColorLetra());
            conteoHeaderLabel.setText(position+1+"/"+listView.getItems().size());
            selected.setSelected(firstElement.isSelected());
            selected.setVisible(true);
        }else {
            textoLabel.setText(String.valueOf(""));
            conteoHeaderLabel.setText(position+"/"+listView.getItems().size());
            siguienteBtn.setVisible(false);
            atrasBtn.setVisible(false);
            girarBtn.setVisible(false);
            selected.setVisible(false);
        }

    }

    private void updateIsSelected() {
        FlashCard myFlashCard = listView.getItems().get(position);
        myFlashCard.setSelected(selected.isSelected());
        DatabaseHandler dbh = new DatabaseHandler();
        dbh.updateIsSelected(myFlashCard);
        FlashCardController.flashCardController.addToSelect();
        FlashCardController.flashCardController.refresh();
        listView.getItems().remove(myFlashCard);
        position = 0;
        setListViewSelected(listView);
    }


}
