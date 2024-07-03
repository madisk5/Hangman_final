package models;

import models.datastructures.DataScore;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * See klass tegeleb andmebaasi ühenduse ja "igasuguste" päringutega tabelitest.
 * Alguses on ainult ühenduse jaoks funktsionaalsus
 */
public class Database {
    /**
     * Algselt ühendust pole
     */
    private Connection connection = null;
    /**
     * Andmebaasi ühenduse string
     */
    private String databaseUrl;
    /**
     * Loodud mudel
     */
    private Model model;

    /**
     * Klassi andmebaas konstruktor
     * @param model loodud mudel
     */
    public Database(Model model) {
        this.model = model;
        this.databaseUrl = "jdbc:sqlite:" + model.getDatabaseFile();
        this.selectUniqueCategories();
    }

    /**
     * Loob andmebaasiga ühenduse
     * @return andmebaasi ühenduse
     */
    private Connection dbConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        connection = DriverManager.getConnection(databaseUrl);
        return connection;
    }

    private void selectUniqueCategories() {
        String sql = "SELECT DISTINCT(category) as category FROM words ORDER BY category;";
        List<String> categories = new ArrayList<>();
        try {
            Connection connection = this.dbConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String category = rs.getString("category");
                categories.add(category); // Lisa kategooria listi kategooriad (categories)
            }
            categories.add(0, model.getChooseCategory()); // Esimeseks kõik kategooriad on olemas
            String[] result = categories.toArray(new String[0]);
            model.setCmbCategories(result);  // Seadista kategooriad mudelisse
            connection.close(); // Andmebaasi ühendus sulgeda
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectScores() {
        String sql = "SELECT * FROM scores ORDER BY gametime, playertime DESC, playername;";
        List<DataScore> data = new ArrayList<>();
        try {
            Connection connection = this.dbConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            model.getDataScores().clear();

            while (rs.next()) {
                String datetime = rs.getString("playertime");
                LocalDateTime playerTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String playerName = rs.getString("playername");
                String guessWord = rs.getString("guessword");
                String wrongChar = rs.getString("wrongcharacters");
                int timeSeconds = rs.getInt("gametime");
                data.add(new DataScore(playerTime, playerName, guessWord, wrongChar, timeSeconds));
            }
            model.setDataScores(data); // Muuda andmed mudelis
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRandomWord(String category) {
        String sql;
        if (category.equals(model.getChooseCategory())) {
            sql = "SELECT word FROM words ORDER BY RANDOM() LIMIT 1;";
        } else {
            sql = "SELECT word FROM words WHERE category = ? ORDER BY RANDOM() LIMIT 1;";
        }
        try {
            Connection connection = this.dbConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            if (!category.equals(model.getChooseCategory())) {
                pstmt.setString(1, category);
            }
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("word");
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void addScore(String playerName, int gameTime) {
        String sql = "INSERT INTO scores (playername, gametime, playertime, guessword, wrongcharacters) VALUES (?, ?, ?, ?, ?);";
        try {
            Connection connection = this.dbConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, playerName);
            pstmt.setInt(2, gameTime);
            pstmt.setString(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            pstmt.setString(4, model.getCurrentWord());
            pstmt.setString(5, model.getIncorrectGuesses().trim());
            pstmt.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
