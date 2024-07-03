package listeners;

import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonSend implements ActionListener {
    private Model model;
    private View view;

    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = view.getGameBoard().getTxtChar().getText();
        if (text.length() == 1) {
            char guess = text.charAt(0);
            if (model.getCurrentWord().indexOf(guess) >= 0) {
                model.updateGuessedWord(guess);
                view.updateDisplayedWord(model.getGuessedWord());
            } else {
                model.incrementMistakes();
                view.showIncorrectGuess(guess);
                view.updateHangmanImage(model.getMistakeCount());
                model.addIncorrectGuess(guess); // Lisame vale tähe
            }

            if (model.isGameOver()) {
                view.stopTimer();
                if (model.isWordGuessed()) {
                    String playerName = JOptionPane.showInputDialog(view, "Sisesta oma nimi:", "Mäng lõppenud", JOptionPane.PLAIN_MESSAGE);
                    if (playerName != null && !playerName.trim().isEmpty()) {
                        model.addScore(playerName.trim(), view.getGameTimer().getPlayedTimeInSeconds());
                        view.updateLeaderBoard();
                    }
                }
                view.promptNewGame();
            } else {
                view.getGameBoard().getTxtChar().setText("");
            }
        }
    }
}
