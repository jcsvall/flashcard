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

public class AddFlashCardController3Delete {


    @FXML
    private Label textoLabel;

    @FXML
    void initialize() {

    }

    public Label getTextoLabel(){
        return textoLabel;
    }
}
