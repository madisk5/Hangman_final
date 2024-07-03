package views;

import helpers.GameTimer;
import helpers.RealTimer;
import models.Database;
import models.Model;
import models.datastructures.DataScore;
import views.panels.GameBoard;
import views.panels.LeaderBoard;
import views.panels.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class View extends JFrame {
    private Model model;
    private Settings settings;
    private GameBoard gameBoard;
    private LeaderBoard leaderBoard;
    private JTabbedPane tabbedPane;
    private GameTimer gameTimer;
    private RealTimer realTimer;

    public View(Model model) {
        this.model = model;

        setTitle("Poomismäng 2024 õpilased");
        setPreferredSize(new Dimension(500, 250));
        getContentPane().setBackground(new Color(250, 210, 205));

        settings = new Settings(model, this);
        gameBoard = new GameBoard(model);
        leaderBoard = new LeaderBoard(model, this);

        createTabbedPanel();

        add(tabbedPane, BorderLayout.CENTER);

        gameTimer = new GameTimer(this);
        realTimer = new RealTimer(this);
        realTimer.start();
    }

    private void createTabbedPanel() {
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Seaded", settings);
        tabbedPane.addTab("Mängulaud", gameBoard);
        tabbedPane.addTab("Edetabel", leaderBoard);

        tabbedPane.setEnabledAt(1, false);
    }

    public void hideButtons() {
        tabbedPane.setEnabledAt(0, false);
        tabbedPane.setEnabledAt(2, false);
        tabbedPane.setEnabledAt(1, true);
        tabbedPane.setSelectedIndex(1);

        gameBoard.getBtnSend().setEnabled(true);
        gameBoard.getBtnCancel().setEnabled(true);
        gameBoard.getTxtChar().setEnabled(true);
    }

    public void showButtons() {
        tabbedPane.setEnabledAt(0, true);
        tabbedPane.setEnabledAt(2, true);
        tabbedPane.setEnabledAt(1, false);

        gameBoard.getBtnSend().setEnabled(false);
        gameBoard.getBtnCancel().setEnabled(false);
        gameBoard.getTxtChar().setEnabled(false);
    }

    public void resetHangmanImage() {
        gameBoard.getLblImage().setIcon(new ImageIcon(model.getImageFiles().get(0)));
    }

    public void updateHangmanImage(int index) {
        if (index >= 0 && index < model.getImageFiles().size()) {
            gameBoard.getLblImage().setIcon(new ImageIcon(model.getImageFiles().get(index)));
        }
    }

    public void showWord(String word) {
        StringBuilder displayWord = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            displayWord.append("_ ");
        }
        gameBoard.getLblResult().setText(displayWord.toString());
    }

    public void updateDisplayedWord(String guessedWord) {
        gameBoard.getLblResult().setText(guessedWord.replace("", " ").trim());
    }

    public void showIncorrectGuess(char guess) {
        gameBoard.getLblError().setText(gameBoard.getLblError().getText() + " " + guess);
    }

    public void stopTimer() {
        gameTimer.stopTime();
    }

    public void updateScoresTable() {
        List<DataScore> scores = model.getDataScores();
        DefaultTableModel dtm = model.getDtm();
        dtm.setRowCount(0); // Tühjenda olemasolevad read
        for (DataScore ds : scores) {
            String gameTime = ds.gameTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            String name = ds.playerName();
            String word = ds.word();
            String chars = ds.missedChars();
            String humanTime = convertSecToMMSS(ds.timeSeconds());
            dtm.addRow(new Object[]{gameTime, name, word, chars, humanTime});
        }
    }

    public void updateLeaderBoard() {
        new Database(model).selectScores();
        updateScoresTable();
    }

    public void showLeaderboard() {
        updateLeaderBoard();
        tabbedPane.setSelectedIndex(2);
    }

    private String convertSecToMMSS(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    public Settings getSettings() {
        return settings;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public LeaderBoard getLeaderBoard() {
        return leaderBoard;
    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }

    public void resetGameBoard() {
        gameBoard.resetErrorLabel();
        gameBoard.getLblResult().setText("");
        gameBoard.getTxtChar().setText("");
        gameBoard.resetGameTimeLabel();
    }

    public void promptNewGame() {
        int response = JOptionPane.showConfirmDialog(this, "Kas soovid uut mängu alustada?", "Uus mäng", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            startNewGame();
        } else {
            showButtons();
        }
    }

    public void startNewGame() {
        hideButtons();
        resetGameBoard();
        model.resetGame();
        resetHangmanImage();
        showWord(model.getCurrentWord());

        gameTimer.stopTime();
        gameTimer.setSeconds(0);
        gameTimer.setMinutes(0);
        gameTimer.setRunning(true);
        gameTimer.startTime();
    }
}
