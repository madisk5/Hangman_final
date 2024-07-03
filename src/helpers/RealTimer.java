package helpers;

import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RealTimer implements ActionListener {
    private View view;
    private Timer timer;

    public RealTimer(View view) {
        this.view = view;
        timer = new Timer(1000, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        show();
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void show() {
        String strTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        view.getSettings().getLblRealTime().setText(strTime);
    }
}
