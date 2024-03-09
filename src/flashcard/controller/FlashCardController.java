package flashcard.controller;

import com.jfoenix.controls.*;
import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;
import flashcard.Database.DatabaseHandler;
import flashcard.animations.Fader;
import flashcard.animations.Shaker;
import flashcard.model.Categoria;
import flashcard.model.FlashCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FlashCardController {
    public static FlashCardController flashCardController;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXListView<FlashCard> listView;

    @FXML
    private AnchorPane paneMenu;

    @FXML
    private ImageView agregar;

    @FXML
    private ImageView menu;

    @FXML
    private JFXTextField buscar;

    @FXML
    private ImageView buscarLupa;

    @FXML
    private ImageView categoriaBtn;

    @FXML
    private ImageView donateBtn;

    @FXML
    private JFXComboBox<String> comboCate;

    @FXML
    private JFXButton aplicarBtn;

    @FXML
    private JFXButton seleccionadosBtn;

    @FXML
    private JFXButton practicarBtn;

    @FXML
    private ImageView agregar2;

    @FXML
    private JFXButton editarBtn;

    @FXML
    private ImageView aboutImgBtn;

    private boolean menuClick = false;

    private ObservableList<FlashCard> flashCards;

    private boolean activeOrInactiveEditarbtn = false;

    DatabaseHandler db;

    @FXML
    void initialize() {
        db = new DatabaseHandler();
        flashCards = FXCollections.observableArrayList();
        List<FlashCard> flasCardList = db.getAllFlasCards();
        flashCards.addAll(flasCardList);
        listView.setItems(flashCards);
        listView.setCellFactory(CellFlashCardController -> new CellFlashCardController());

        setVisible();
        menu.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
           Shaker buttonShaker = new Shaker(menu);
           buttonShaker.shake();
            menuClick = !menuClick;
            setVisible();
            System.out.println("Evento clicked");
        });

        agregar.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            Shaker buttonShaker = new Shaker(agregar);
            buttonShaker.shake();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/flashcard/view/addFlashCard.fxml"));
                                                           //flashcard/view/flascard.fxml
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

        });

        agregar2.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            Shaker buttonShaker = new Shaker(agregar2);
            buttonShaker.shake();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/flashcard/view/addFlashCard2.fxml"));
            //loader.setLocation(getClass().getResource("/flashcard/view/addFlashCardv3.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
//            AddFlashCardController3Delete addFlashCardController2 = loader.getController();
//            Label textoLabel = addFlashCardController2.getTextoLabel();
//            textoLabel.setMaxWidth(40000);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        });

        buscarLupa.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Shaker buttonShaker = new Shaker(buscarLupa);
            buttonShaker.shake();
        });

        categoriaBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Shaker buttonShaker = new Shaker(categoriaBtn);
            buttonShaker.shake();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/flashcard/view/addCategoria.fxml"));
            //flashcard/view/flascard.fxml
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        });

        donateBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Shaker buttonShaker = new Shaker(donateBtn);
            buttonShaker.shake();
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.paypal.com/donate/?hosted_button_id=YEUPQBSMVR7LU"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        });

        aplicarBtn.setOnAction(actionEvent -> {
            filtrar();
            //seleccionarDeListView();
        });

        comboCate.setOnAction(actionEvent -> {
            filtrar();
        });

        pupulateComboCategories();

        practicarBtn.setOnAction(actionEvent -> {
            practicarWindow();
        });

        seleccionadosBtn.setOnAction(actionEvent -> {
            practicarWindowSelection();
        });

        buscarLupa.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            buscar();
        });

        buscar.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER){
                buscar();
            }
        });

        editarBtn.setOnAction(actionEvent -> {
            editar();
        });

        addToSelect();

        flashCardController = this;

        setActiveOrInactiveEditarbtn();
        listView.setOnMouseClicked(mouseEvent -> {
            FlashCard itemSelected = null;

            if(listView !=null && listView.getSelectionModel() != null && listView.getSelectionModel().getSelectedItem() != null){
                itemSelected = listView.getSelectionModel().getSelectedItem();
            }

            if(itemSelected != null){
                activeOrInactiveEditarbtn = true;
                setActiveOrInactiveEditarbtn();
            }
        });

        aboutImgBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            about();
        });


    }

    public void setVisible(){
        paneMenu.setVisible(menuClick);
    }

    public void refresh(){
        flashCards = FXCollections.observableArrayList();
        List<FlashCard> flasCardList = null;
        if((comboCate.getValue() !=null && !comboCate.getValue().trim().isEmpty()) || !buscar.getText().isEmpty()){
            flasCardList = db.getByFilter(comboCate.getValue(), buscar.getText());
        }else{
            flasCardList = db.getAllFlasCards();
        }
        flashCards.addAll(flasCardList);
        listView.setItems(flashCards);
        listView.setCellFactory(CellFlashCardController -> new CellFlashCardController());
        activeOrInactiveEditarbtn = false;
        setActiveOrInactiveEditarbtn();

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

    //Metodo de referencia para selecionar valore de listView
    public void seleccionarDeListView(){
        FlashCard itemSelected = listView.getSelectionModel().getSelectedItem();
        System.out.println("Anchor paint id "+itemSelected.getAnchorPane().getId());
        ObservableList<Node> childrens = itemSelected.getAnchorPane().getChildren();
        Node findChild = null;
        String labelId = "check-"+itemSelected.getAnchorPane().getId();
        for(Node child:childrens){
            if(labelId.equalsIgnoreCase(child.getId())){
                findChild =child;
                break;
            }
        }
        if(findChild != null){
            JFXCheckBox checkBox = (JFXCheckBox)findChild;
            checkBox.setSelected(true);
            itemSelected.setSelected(true);
        }
    }

    public void filtrar(){
        //List<FlashCard> flasCardList = db.getAllFlasCards();
        List<FlashCard> flasCardList = db.getByFilter(comboCate.getValue(), buscar.getText());
        ObservableList<FlashCard> orgiginalList = FXCollections.observableArrayList();
        orgiginalList.addAll(flasCardList);

        ObservableList<FlashCard> itemInListView = listView.getItems();
        String cat = comboCate.getValue();
        List<FlashCard> itemFiltered = orgiginalList.stream().filter(item -> item.getCatgoria().equalsIgnoreCase(cat)).collect(Collectors.toList());

        ObservableList<FlashCard> filteredList = FXCollections.observableArrayList();
        filteredList.addAll(itemFiltered);
        if(cat == null){
            listView.setItems(orgiginalList);
        } else if (cat.trim().isEmpty()) {
            listView.setItems(orgiginalList);
        }else{
            listView.setItems(filteredList);
        }
        activeOrInactiveEditarbtn = false;
        setActiveOrInactiveEditarbtn();
    }

//    List<FlashCard> flasCardFromCellFlash = new ArrayList<>();
//    public void addToSelection(FlashCard flashCard){
//        flasCardFromCellFlash.add(flashCard);
//        seleccionadosBtn.setText("Seleccionados("+flasCardFromCellFlash.size()+")");
//    }
//    public void removeToSelection(FlashCard flashCard){
//        flasCardFromCellFlash.remove(flashCard);
//        seleccionadosBtn.setText("Seleccionados("+flasCardFromCellFlash.size()+")");
//    }

    List<FlashCard> flashCardsSelected = new ArrayList<>();
    public void addToSelect(){
        flashCardsSelected = db.getFlashCardsByIsSelected(true);
        seleccionadosBtn.setText("Seleccionados("+flashCardsSelected.size()+")");
    }

    public void practicarWindow(){
        Shaker buttonShaker = new Shaker(practicarBtn);
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
        JFXListView<FlashCard> listViewPractice = new JFXListView<>();
        //listView.getItems()
        listViewPractice.getItems().addAll(listView.getItems());
        Collections.shuffle(listViewPractice.getItems());//para desordenar al azar
        verFlashCardController.setListView(listViewPractice);
    }

    public void practicarWindowSelection(){
        Shaker buttonShaker = new Shaker(practicarBtn);
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
        FlashCard f = new FlashCard();
        f.setId(2568);

        ObservableList<FlashCard> flashCardsSelect = FXCollections.observableArrayList();
        //List<FlashCard> flasCardList = db.getAllFlasCards();
        flashCardsSelect.addAll(flashCardsSelected);
        JFXListView<FlashCard> listViewSelectoion = new JFXListView<>();
        listViewSelectoion.setItems(flashCardsSelect);
        listViewSelectoion.setCellFactory(CellFlashCardController -> new CellFlashCardController());
        verFlashCardController.setListViewSelected(listViewSelectoion);
    }

    public void buscar(){
        String text = buscar.getText();
        if(!text.isEmpty()){
            //List<FlashCard> flasCardList = db.findFlashCardByText(buscar.getText());
            List<FlashCard> flasCardList = db.getByFilter(comboCate.getValue(), buscar.getText());
            listView.getItems().clear();
            listView.getItems().addAll(flasCardList);
        }else{
            refresh();
        }
        activeOrInactiveEditarbtn = false;
        setActiveOrInactiveEditarbtn();
    }

    private void editar(){
        Shaker buttonShaker = new Shaker(editarBtn);
        buttonShaker.shake();

        FlashCard itemSelected = listView.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/flashcard/view/addFlashCard2.fxml"));
        //loader.setLocation(getClass().getResource("/flashcard/view/addFlashCardv3.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddFlashCardController2 addFlashCardController2 = loader.getController();
        addFlashCardController2.setInitialFlasCardEdition(itemSelected);
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
    }

    private void setActiveOrInactiveEditarbtn(){
        if(activeOrInactiveEditarbtn){
            editarBtn.setVisible(true);
        }else{
            editarBtn.setVisible(false);
        }
    }

    private void about(){
        Shaker buttonShaker = new Shaker(aboutImgBtn);
        buttonShaker.shake();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/flashcard/view/about.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Información");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);//Para no mostrar el - y el [] en la barra solo la X
        //stage.initStyle(StageStyle.UNDECORATED); //para quitar la barra.
        stage.showAndWait();
    }


}
