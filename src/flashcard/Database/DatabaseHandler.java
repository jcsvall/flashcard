package flashcard.Database;

import flashcard.model.Categoria;
import flashcard.model.FlashCard;
import flashcard.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        //main root = /firstApp/
        //Cambiar ruta cuando se genere el jar a:
        //dbConnection = DriverManager.getConnection("jdbc:sqlite:Data/flashCardDB.db");
        //Para ganar espacio al generar el jar eliminar flashCardDB.db y el sqlite3.exe y depues regresarlo
        //Si no
       dbConnection = DriverManager.getConnection("jdbc:sqlite:src/flashcard/Database/flashCardDB.db");

        return dbConnection;
    }

    public List<FlashCard> getAllFlasCards() {
        List<FlashCard> data = new ArrayList<>();
        try {
            ResultSet rs = getDbConnection().createStatement().executeQuery("select * from flashcard order by id desc");
            while(rs.next()){
                FlashCard flashCard = new FlashCard();
                flashCard.setId(rs.getInt("id"));
                flashCard.setAdelante(rs.getString("adelante"));
                flashCard.setAtras(rs.getString("atras"));
                flashCard.setColorFondo(rs.getString("color_fondo"));
                flashCard.setColorLetra(rs.getString("color_letra"));
                flashCard.setCatgoria(rs.getString("categoria"));
                flashCard.setSelected(rs.getBoolean("is_selected"));
                data.add(flashCard);
            }
        } catch (Exception e) {
           e.printStackTrace();
        }

        return data;
    }

    public void insertFlashCard(FlashCard flashCard){
        String query = "insert into flashcard (Adelante,Atras,color_fondo,color_letra,categoria,adelante_atras,date_created,is_selected) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, flashCard.getAdelante());
            preparedStatement.setString(2, flashCard.getAtras());
            preparedStatement.setString(3, flashCard.getColorFondo());
            preparedStatement.setString(4, flashCard.getColorLetra());
            preparedStatement.setString(5, flashCard.getCatgoria());
            preparedStatement.setString(6, flashCard.getAdelanteAtras());
            preparedStatement.setTimestamp(7, flashCard.getDatecreated());
            preparedStatement.setBoolean(8, flashCard.isSelected());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAllFlasCards(){
        String query = "delete from flashcard";
        try {
            getDbConnection().createStatement().executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Categoria> getCategorias() {
        List<Categoria> data = new ArrayList<>();
        try {
            ResultSet rs = getDbConnection().createStatement().executeQuery("select * from categoria");
            while(rs.next()){
                Categoria categoria = new Categoria(rs.getString("catgoria"));
                data.add(categoria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public void insertCategoria(Categoria categoria){
        String query = "insert into categoria (catgoria) values (?)";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, categoria.getCatgoria());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateIsSelected(FlashCard flashCard){
        String query = "update flashcard set is_selected = ? where id = ?";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
            preparedStatement.setBoolean(1, flashCard.isSelected());
            preparedStatement.setInt(2, flashCard.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FlashCard> getFlashCardsByIsSelected(boolean isSelected) {
        List<FlashCard> data = new ArrayList<>();
        try {
            ResultSet rs = getDbConnection().createStatement().executeQuery("select * from flashcard where is_selected = "+isSelected);
            while(rs.next()){
                FlashCard flashCard = new FlashCard();
                flashCard.setId(rs.getInt("id"));
                flashCard.setAdelante(rs.getString("adelante"));
                flashCard.setAtras(rs.getString("atras"));
                flashCard.setColorFondo(rs.getString("color_fondo"));
                flashCard.setColorLetra(rs.getString("color_letra"));
                flashCard.setCatgoria(rs.getString("categoria"));
                flashCard.setSelected(rs.getBoolean("is_selected"));
                data.add(flashCard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public void deleteFlashCard(FlashCard flashCard){
        String query = "delete from flashcard where id = ?";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
            preparedStatement.setInt(1, flashCard.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FlashCard> findFlashCardByText(String text) {
        List<FlashCard> data = new ArrayList<>();
        try {
            ResultSet rs = getDbConnection().createStatement().executeQuery("select * from flashcard where adelante_atras like '%"+text+"%'");
            while(rs.next()){
                FlashCard flashCard = new FlashCard();
                flashCard.setId(rs.getInt("id"));
                flashCard.setAdelante(rs.getString("adelante"));
                flashCard.setAtras(rs.getString("atras"));
                flashCard.setColorFondo(rs.getString("color_fondo"));
                flashCard.setColorLetra(rs.getString("color_letra"));
                flashCard.setCatgoria(rs.getString("categoria"));
                flashCard.setSelected(rs.getBoolean("is_selected"));
                data.add(flashCard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public void updateFlashCard(FlashCard flashCard){
        String query = "update flashcard set Adelante = ?,Atras = ?,color_fondo = ?,color_letra = ?,categoria = ?,adelante_atras = ? where id = ?";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, flashCard.getAdelante());
            preparedStatement.setString(2, flashCard.getAtras());
            preparedStatement.setString(3, flashCard.getColorFondo());
            preparedStatement.setString(4, flashCard.getColorLetra());
            preparedStatement.setString(5, flashCard.getCatgoria());
            preparedStatement.setString(6, flashCard.getAdelanteAtras());
            preparedStatement.setInt(7, flashCard.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteFlashCardsByCategoria(Categoria categoria){
        String query = "delete from flashcard where categoria = ?";
        boolean response;
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, categoria.getCatgoria());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            response =  true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
    public void deleteCategoria(Categoria categoria){
        boolean isDeleted = deleteFlashCardsByCategoria(categoria);
        if(isDeleted){
            String query = "delete from categoria where catgoria = ?";
            try {
                PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
                preparedStatement.setString(1, categoria.getCatgoria());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<FlashCard> getByFilter(String comboCategoria, String buscar) {
        List<FlashCard> data = new ArrayList<>();
        try {
            StringBuilder query = new StringBuilder();
            query.append("select * from flashcard where ");
            if((comboCategoria != null  && !comboCategoria.trim().isEmpty()) && !buscar.isEmpty()){
                query.append("categoria = '").append(comboCategoria).append("' and ");
                query.append("adelante_atras like '%").append(buscar).append("%' ");
            } else if (comboCategoria != null && !comboCategoria.trim().isEmpty()) {
                query.append("categoria = '").append(comboCategoria).append("' ");
            } else if (!buscar.isEmpty()) {
                query.append("adelante_atras like '%").append(buscar).append("%' ");
            }else{
                query = new StringBuilder();
                query.append("select * from flashcard ");
            }
            query.append(" order by id desc");

            ResultSet rs = getDbConnection().createStatement().executeQuery(query.toString());
            while(rs.next()){
                FlashCard flashCard = new FlashCard();
                flashCard.setId(rs.getInt("id"));
                flashCard.setAdelante(rs.getString("adelante"));
                flashCard.setAtras(rs.getString("atras"));
                flashCard.setColorFondo(rs.getString("color_fondo"));
                flashCard.setColorLetra(rs.getString("color_letra"));
                flashCard.setCatgoria(rs.getString("categoria"));
                flashCard.setSelected(rs.getBoolean("is_selected"));
                data.add(flashCard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}
