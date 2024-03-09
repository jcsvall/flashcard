package flashcard.controller;

import com.jfoenix.controls.*;
import flashcard.Database.DatabaseHandler;
import flashcard.animations.Shaker;
import flashcard.model.Categoria;
import flashcard.model.FlashCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AddFlashCardController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextArea desccriptionTextArea;

    @FXML
    private JFXButton ingresarBtn;

    @FXML
    private ImageView girarBtn;

    @FXML
    private Label frontBackLabel;

    @FXML
    private JFXColorPicker colorFondo;

    @FXML
    private JFXColorPicker colorLetra;

    @FXML
    private JFXComboBox<String> comboCate;

    @FXML
    private ImageView verBtnImage;

    public Map<String, String> flasCardData;

    boolean isFront = true;

    DatabaseHandler db;

    @FXML
    void initialize() {
        flasCardData = new HashMap<>();
        db = new DatabaseHandler();

        Color cFondoDefault = Color.web("#000102");
        colorFondo.setValue(cFondoDefault);

        Color cLetraDefault = Color.web("#FFFFFF");
        colorLetra.setValue(cLetraDefault);

        ingresarBtn.setVisible(false);

        girarBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            girarBtn();
        });

        ingresarBtn.setOnAction(event -> {
            Shaker buttonShaker = new Shaker(ingresarBtn);
            buttonShaker.shake();
            
            flasCardData.put("back",desccriptionTextArea.getText());

            FlashCard flashCard = new FlashCard();
            flashCard.setAdelante(flasCardData.get("front"));
            flashCard.setAtras(flasCardData.get("back"));
            //flashCard.setColorFondo(colorFondo.getValue().toString().substring(2));
            //flashCard.setColorLetra(colorLetra.getValue().toString().substring(2));
            String colorDefondoCss = "#"+colorFondo.getValue().toString().substring(2,8);
            String colorLetraCss = "#"+colorLetra.getValue().toString().substring(2,8);
            flashCard.setColorFondo(colorDefondoCss);
            flashCard.setColorLetra(colorLetraCss);
            String cat = "DEFAULT";
            if(comboCate.getValue() != null){
                cat = comboCate.getValue().trim().isEmpty() ? "DEFAULT": comboCate.getValue().trim();
            }
            flashCard.setCatgoria(cat);
            flashCard.setAdelanteAtras(flashCard.getAdelante()+" "+flashCard.getAtras());

            Calendar calendar = Calendar.getInstance();
            java.sql.Timestamp timestamp =
                    new java.sql.Timestamp(calendar.getTimeInMillis());

            flashCard.setDatecreated(timestamp);

            db.insertFlashCard(flashCard);
            desccriptionTextArea.setText("");
            flasCardData = new HashMap<>();

            FlashCardController.flashCardController.refresh();
            isFront = false;
            girarBtn();

        });

        pupulateComboCategories();

        verBtnImage.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            preview();
        });

    }

    public void girarBtn(){
        Shaker buttonShaker = new Shaker(girarBtn);
        buttonShaker.shake();

        String textAreaValue = desccriptionTextArea.getText();
        if(isFront){
            flasCardData.put("front",textAreaValue);
            desccriptionTextArea.setText(flasCardData.get("back"));
            frontBackLabel.setText("Atras");
            ingresarBtn.setVisible(true);
        }else{
            flasCardData.put("back",textAreaValue);
            desccriptionTextArea.setText(flasCardData.get("front"));
            frontBackLabel.setText("Adelante");
            ingresarBtn.setVisible(false);
        }
        isFront = !isFront;
    }

    public void pupulateComboCategories(){
        ObservableList<String> categorias = FXCollections.observableArrayList();
        List<Categoria> catList = db.getCategorias();
        categorias.add(" ");
        for(Categoria cat: catList){
            categorias.add(cat.getCatgoria());
        }
        comboCate.setItems(categorias);
    }

    public void preview(){
        Shaker buttonShaker = new Shaker(verBtnImage);
        buttonShaker.shake();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/flashcard/view/verFlashCard.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

        VerFlashCardController verFlashCardController = loader.getController();

        ObservableList<FlashCard> flashCardsSelect = FXCollections.observableArrayList();
        FlashCard flashCard = new FlashCard();
        flashCard.setAdelante(flasCardData.get("front"));
        flashCard.setAtras(flasCardData.get("back"));
        String colorDefondoCss = "#"+colorFondo.getValue().toString().substring(2,8);
        String colorLetraCss = "#"+colorLetra.getValue().toString().substring(2,8);
        flashCard.setColorLetra(colorLetraCss);
        flashCard.setColorFondo(colorDefondoCss);
        flashCardsSelect.addAll(flashCard);
        JFXListView<FlashCard> listViewSelectoion = new JFXListView<>();
        listViewSelectoion.setItems(flashCardsSelect);
        listViewSelectoion.setCellFactory(CellFlashCardController -> new CellFlashCardController());
        verFlashCardController.verOneFlashCard(listViewSelectoion);

    }
}
