package flashcard.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import flashcard.Database.DatabaseHandler;
import flashcard.model.Categoria;
import flashcard.model.FlashCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddCategoriaController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField textFieldCategoria;

    @FXML
    private JFXButton addCategoriaBtn;

    @FXML
    private JFXListView<String> listViewCategoria;

    @FXML
    private Label frontBackLabel;

    @FXML
    private JFXButton eliminarBtn;

    @FXML
    private Label msgLabel;

    @FXML
    private JFXButton eliminarBtnNo;

    @FXML
    private JFXButton eliminarBtnSi;


    private DatabaseHandler databaseHandler;
    List<Categoria> catList;

    private boolean verBtnEliminar = false;

    @FXML
    void initialize() {
        catList = new ArrayList<>();
        databaseHandler = new DatabaseHandler();
        populateCategorias();

        addCategoriaBtn.setOnAction(event -> {

            if(!textFieldCategoria.getText().isEmpty()){
                Categoria categoria = new Categoria(textFieldCategoria.getText().toUpperCase().trim());
                if(!checkIfCatExist(categoria.getCatgoria())){
                    databaseHandler.insertCategoria(categoria);
                    populateCategorias();
                    textFieldCategoria.setText("");
                    FlashCardController.flashCardController.pupulateComboCategories();
                }else{
                    System.out.println("Categoria ya existe");
                }
            }else{
                System.out.println("Categoria esta vacio");
            }
        });

        eliminarBtn.setOnAction(actionEvent -> {
            eliminarCat();
        });

        eliminarBtnSi.setOnAction(actionEvent -> {
            eliminarSi();
        });

        verBtneEliminar();

        listViewCategoria.setOnMouseClicked(mouseEvent -> {
            if(getCatSelected() != null){
                eliminarBtn.setVisible(true);
            }else{
                eliminarBtn.setVisible(false);
            }
            setVisibleFlaseConfirDialog();
        });

        setVisibleFlaseConfirDialog();

        eliminarBtnNo.setOnAction(actionEvent -> {
            setVisibleFlaseConfirDialog();
            if(getCatSelected() != null){
                eliminarBtn.setVisible(true);
            }
        });

    }

    public void populateCategorias(){
        ObservableList<String> categorias = FXCollections.observableArrayList();
        catList = databaseHandler.getCategorias();
        for(Categoria cat: catList){
            categorias.add(cat.getCatgoria());
        }
        listViewCategoria.setItems(categorias);
    }

    public boolean checkIfCatExist(String catStr){
        long has = catList.stream().filter(cat -> cat.getCatgoria().equalsIgnoreCase(catStr)).count();
        return has > 0;
    }

    public void eliminarCat(){
        msgLabel.setText("Al eliminar categoria tambien se eliminaran los elementos asociados a la categoria?");
        msgLabel.setVisible(true);
        eliminarBtnSi.setVisible(true);
        eliminarBtnNo.setVisible(true);
        eliminarBtn.setVisible(false);
    }

    public void setVisibleFlaseConfirDialog(){
        msgLabel.setVisible(false);
        eliminarBtnSi.setVisible(false);
        eliminarBtnNo.setVisible(false);
    }

    public void eliminarSi(){
        String catSelected = listViewCategoria.getSelectionModel().getSelectedItem();
        if(!catSelected.equalsIgnoreCase("DEFAULT")){
            Categoria cat = new Categoria(catSelected);
            databaseHandler.deleteCategoria(cat);
            populateCategorias();
            setVisibleFlaseConfirDialog();
            FlashCardController.flashCardController.pupulateComboCategories();
            FlashCardController.flashCardController.refresh();
        }else{
            msgLabel.setText("No esta permitido eliminar la categoria DEFAULT");
            eliminarBtnSi.setVisible(false);
            eliminarBtnNo.setVisible(false);
        }

    }

    private String getCatSelected(){
        String catSelected = null;
        if(listViewCategoria != null && listViewCategoria.getSelectionModel() != null && listViewCategoria.getSelectionModel().getSelectedItem() != null){
            catSelected = listViewCategoria.getSelectionModel().getSelectedItem();
        }
        return catSelected;
    }

    private void verBtneEliminar(){
        if(verBtnEliminar){
            eliminarBtn.setVisible(true);
        }else{
            eliminarBtn.setVisible(false);
        }
    }
}
