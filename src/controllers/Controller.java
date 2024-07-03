package controllers;

import listeners.ButtonCancel;
import listeners.ButtonNew;
import listeners.ComboboxChange;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    public Controller(Model model, View view) {

        view.getSettings().getCmbCategory().addItemListener(new ComboboxChange(model));
        view.getSettings().getBtnNewGame().addActionListener(new ButtonNew(model, view));
        view.getGameBoard().getBtnCancel().addActionListener(new ButtonCancel(model, view));
        view.getSettings().getBtnLeaderboard().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showLeaderboard();
            }
        });

        view.getGameBoard().getBtnSend().addActionListener(new ActionListener() {
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
        });
    }
}
