package listeners;

import models.Model;
import views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonNew implements ActionListener {
    private Model model;
    private View view;

    public ButtonNew(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.startNewGame();
    }
}
