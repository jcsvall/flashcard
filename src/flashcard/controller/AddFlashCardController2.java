package flashcard.controller;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import flashcard.Database.DatabaseHandler;
import flashcard.animations.FaderV2;
import flashcard.animations.Shaker;
import flashcard.model.Categoria;
import flashcard.model.FlashCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class AddFlashCardController2 {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private Label textoLabel;

    @FXML
    private JFXTextArea desccriptionTextArea;

    @FXML
    private ImageView girarBtn;

    @FXML
    private JFXColorPicker colorFondo;

    @FXML
    private JFXColorPicker colorLetra;

    @FXML
    private JFXComboBox<String> comboCate;

    @FXML
    private ImageView saveBtnImg;

    @FXML
    private Label mensajeLabel;

    DatabaseHandler db;
    FlashCard initialFlasCard;

    private boolean isFrontBack = true;

    private boolean isEdition = false;

    @FXML
    void initialize() {
        instances();
        populateComboCategories();
        initialFlashCard();
        eventosBtn();
    }

    private void eventosBtn(){
        desccriptionTextArea.setOnKeyReleased(event ->{
            textoLabel.setText(desccriptionTextArea.getText());
            if(isFrontBack){
                initialFlasCard.setAdelante(desccriptionTextArea.getText());
            }else{
                initialFlasCard.setAtras(desccriptionTextArea.getText());
            }
        });

        colorFondo.setOnAction(event ->{
            String colorDefondoCss = "#"+colorFondo.getValue().toString().substring(2,8);
            initialFlasCard.setColorFondo(colorDefondoCss);
            textoLabel.setStyle("-fx-background-color:"+initialFlasCard.getColorFondo()
                    +"; -fx-text-fill:"+initialFlasCard.getColorLetra());
        });

        colorLetra.setOnAction(event ->{
            String colorLetraCss = "#"+colorLetra.getValue().toString().substring(2,8);
            initialFlasCard.setColorLetra(colorLetraCss);
            textoLabel.setStyle("-fx-background-color:"+initialFlasCard.getColorFondo()
                    +"; -fx-text-fill:"+initialFlasCard.getColorLetra());
        });

        girarBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            girarBtn();
        });

        saveBtnImg.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(isEdition){
                update();
            }else{
                save();
            }

        });
    }

    private void instances(){
        db = new DatabaseHandler();
        initialFlasCard = new FlashCard();
    }
    public void populateComboCategories(){
        ObservableList<String> categorias = FXCollections.observableArrayList();
        List<Categoria> catList = db.getCategorias();
        categorias.add(" ");
        for(Categoria cat: catList){
            categorias.add(cat.getCatgoria());
        }
        comboCate.setItems(categorias);
    }
    public void initialFlashCard(){
        colorFondo.setValue(Color.BLACK);
        colorLetra.setValue(Color.WHITE);
        initialFlasCard.setColorFondo("#000000");
        initialFlasCard.setColorLetra("#ffffff");
        initialFlasCard.setAdelante("");
        initialFlasCard.setAtras("");

        textoLabel.setText("");
        textoLabel.setStyle("-fx-background-color:"+initialFlasCard.getColorFondo()
                         +"; -fx-text-fill:"+initialFlasCard.getColorLetra());
        desccriptionTextArea.setPromptText("Ingrese Texto Frontal");
        mensajeLabel.setText("");
    }

    private void girarBtn(){
        Shaker buttonShaker = new Shaker(girarBtn);
        buttonShaker.shake();

        FaderV2 labelFader = new FaderV2(textoLabel,1500, false);
        labelFader.fade();

        desccriptionTextArea.setText("");
        isFrontBack = !isFrontBack;
        if(isFrontBack){
            textoLabel.setText(initialFlasCard.getAdelante());
            desccriptionTextArea.setText(initialFlasCard.getAdelante());
            desccriptionTextArea.setPromptText("Ingrese Texto Frontal");
        }else{
            textoLabel.setText(initialFlasCard.getAtras());
            desccriptionTextArea.setText(initialFlasCard.getAtras());
            desccriptionTextArea.setPromptText("Ingrese Texto Posterior");
        }
    }

    private void save(){
        Shaker buttonShaker = new Shaker(saveBtnImg);
        buttonShaker.shake();
        boolean guardar = true;
        String errorMsg = "";
        String ingrese = "";
        if(initialFlasCard.getAdelante().isEmpty()){
            guardar = false;
            ingrese = "Error ingrese texto en la parte ";
            errorMsg = ingrese+"Frontal";
        }
        if(initialFlasCard.getAtras().isEmpty()){
            guardar = false;
            if(ingrese.isEmpty()){
                errorMsg = "Error ingrese texto en la parte Posterior";
            }else{
                errorMsg = errorMsg +" y parte Posterior";
            }

        }

        if(guardar){
            String cat = "DEFAULT";
            if(comboCate.getValue() != null){
                cat = comboCate.getValue().trim().isEmpty() ? "DEFAULT": comboCate.getValue().trim();
            }
            initialFlasCard.setCatgoria(cat);
            initialFlasCard.setAdelanteAtras(initialFlasCard.getAdelante()+" "+initialFlasCard.getAtras());

            Calendar calendar = Calendar.getInstance();
            java.sql.Timestamp timestamp =
                    new java.sql.Timestamp(calendar.getTimeInMillis());
            initialFlasCard.setDatecreated(timestamp);

            db.insertFlashCard(initialFlasCard);
            FlashCardController.flashCardController.refresh();
            mensajeLabel.setText("Flashcard creada satisfactoriamente");
            mensajeLabel.setTextFill(Color.GREENYELLOW);

            desccriptionTextArea.setText("");
            textoLabel.setText("");

            isFrontBack = true;
            desccriptionTextArea.setPromptText("Ingrese Texto Frontal");
            //initialFlasCard = new FlashCard();
            initialFlasCard.setAdelante("");
            initialFlasCard.setAtras("");
        }else{
            mensajeLabel.setText(errorMsg);
            mensajeLabel.setTextFill(Color.ORANGE);
        }
    }

    public void setInitialFlasCardEdition(FlashCard flashCardToEdit){
        initialFlasCard = flashCardToEdit;
        initialEdition();
    }
    public void initialEdition(){
        colorFondo.setValue(Color.valueOf(initialFlasCard.getColorFondo()));
        colorLetra.setValue(Color.valueOf(initialFlasCard.getColorLetra()));
        initialFlasCard.setColorFondo(initialFlasCard.getColorFondo());
        initialFlasCard.setColorLetra(initialFlasCard.getColorLetra());
        initialFlasCard.setAdelante(initialFlasCard.getAdelante());
        initialFlasCard.setAtras(initialFlasCard.getAtras());

        textoLabel.setText(initialFlasCard.getAdelante());
        desccriptionTextArea.setText(initialFlasCard.getAdelante());
        textoLabel.setStyle("-fx-background-color:"+initialFlasCard.getColorFondo()
                +"; -fx-text-fill:"+initialFlasCard.getColorLetra());
        desccriptionTextArea.setPromptText("Ingrese Texto Frontal");
        mensajeLabel.setText("");
        comboCate.setValue(initialFlasCard.getCatgoria());
        isEdition = true;
    }

    public void update(){
        Shaker buttonShaker = new Shaker(saveBtnImg);
        buttonShaker.shake();
        boolean guardar = true;
        String errorMsg = "";
        String ingrese = "";
        if(initialFlasCard.getAdelante().isEmpty()){
            guardar = false;
            ingrese = "Error ingrese texto en la parte ";
            errorMsg = ingrese+"Frontal";
        }
        if(initialFlasCard.getAtras().isEmpty()){
            guardar = false;
            if(ingrese.isEmpty()){
                errorMsg = "Error ingrese texto en la parte Posterior";
            }else{
                errorMsg = errorMsg +" y parte Posterior";
            }

        }
        if(guardar){
            String cat = "DEFAULT";
            if(comboCate.getValue() != null){
                cat = comboCate.getValue().trim().isEmpty() ? "DEFAULT": comboCate.getValue().trim();
            }
            initialFlasCard.setCatgoria(cat);
            initialFlasCard.setAdelanteAtras(initialFlasCard.getAdelante()+" "+initialFlasCard.getAtras());
            db.updateFlashCard(initialFlasCard);
            FlashCardController.flashCardController.refresh();
            setInitialFlasCardEdition(initialFlasCard);
            mensajeLabel.setText("Flashcard actualizada satisfactoriamente");
            mensajeLabel.setTextFill(Color.GREENYELLOW);
            mensajeLabel.getScene().getWindow().hide();

        }else{
            mensajeLabel.setText(errorMsg);
            mensajeLabel.setTextFill(Color.ORANGE);
        }
    }

    public Label getTextoLabel(){
        return textoLabel;
    }
}
