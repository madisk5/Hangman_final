package models;

import models.datastructures.DataScore;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Model {
    private final String chooseCategory = "Kõik kategooriad";
    private String databaseFile = "hangman_words_ee_test.db";
    private String selectedCategory;
    private String[] cmbCategories;
    private String currentWord;
    private StringBuilder guessedWord;
    private int mistakes;
    private static final int MAX_MISTAKES = 10;
    private Database database;
    private List<String> imageFiles = new ArrayList<>();
    private DefaultTableModel dtm;
    private List<DataScore> dataScores = new ArrayList<>();
    private StringBuilder incorrectGuesses;

    public Model(String dbName) {
        if (dbName != null) {
            this.databaseFile = dbName;
        }
        this.database = new Database(this);
        readImagesFolder();
        selectedCategory = chooseCategory;
        resetGame();
    }

    private void readImagesFolder() {
        File folder = new File("images");
        File[] files = folder.listFiles();
        for (File file : Objects.requireNonNull(files)) {
            imageFiles.add(file.getAbsolutePath());
        }
        Collections.sort(imageFiles);
    }

    public void resetGame() {
        this.currentWord = database.getRandomWord(selectedCategory);
        if (currentWord == null) {
            throw new IllegalArgumentException("No words found for the given category: " + selectedCategory);
        }
        this.guessedWord = new StringBuilder("_".repeat(currentWord.length()));
        this.mistakes = 0;
        this.incorrectGuesses = new StringBuilder();
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void incrementMistakes() {
        mistakes++;
    }

    public int getMistakeCount() {
        return mistakes;
    }

    public boolean isGameOver() {
        return mistakes >= MAX_MISTAKES || guessedWord.indexOf("_") == -1;
    }

    public void updateGuessedWord(char guess) {
        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.charAt(i) == guess) {
                guessedWord.setCharAt(i, guess);
            }
        }
    }

    public String getGuessedWord() {
        return guessedWord.toString();
    }

    public boolean isWordGuessed() {
        return guessedWord.indexOf("_") == -1;
    }

    public void addScore(String playerName, int gameTime) {
        database.addScore(playerName, gameTime);
    }

    public String getChooseCategory() {
        return chooseCategory;
    }

    public String getDatabaseFile() {
        return databaseFile;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public String[] getCmbCategories() {
        return cmbCategories;
    }

    public void setCmbCategories(String[] cmbCategories) {
        this.cmbCategories = cmbCategories;
    }

    public List<String> getImageFiles() {
        return imageFiles;
    }

    public List<DataScore> getDataScores() {
        return dataScores;
    }

    public void setDataScores(List<DataScore> dataScores) {
        this.dataScores = dataScores;
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }

    public void setDtm(DefaultTableModel dtm) {
        this.dtm = dtm;
    }

    public String getIncorrectGuesses() {
        return incorrectGuesses.toString();
    }

    public void addIncorrectGuess(char guess) {
        if (incorrectGuesses.indexOf(String.valueOf(guess)) == -1) {
            incorrectGuesses.append(guess).append(" ");
        }
    }
}
