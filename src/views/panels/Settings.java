package views.panels;

import models.Model;
import views.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * See on vaheleht Seaded paneel ehk avaleht. Siit saab valida mängu jaoks sõna kategooria ja käivitada mängu. See on
 * üks kolmest vahelehest (esimene). JPanel vaikimisi (default) aknahaldur (Layout Manager) on FlowLayout.
 */
public class Settings extends JPanel {
    private Model model;
    private GridBagConstraints gbc = new GridBagConstraints();
    private JLabel lblRealTime;
    private JLabel lblCategory;
    private JComboBox<String> cmbCategory;
    private JButton btnNewGame;
    private JButton btnLeaderboard;
    private JLabel lblDatabase;

    public Settings(Model model, View view) {
        this.model = model;
        setBackground(new Color(255,250,200));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2,2,2,2);
        JPanel components = new JPanel(new GridBagLayout());
        components.setBackground(new Color(140,185,250));

        setupUIComponents(components);
        add(components);

        btnLeaderboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showLeaderboard();
            }
        });
    }

    private void setupUIComponents(JPanel components) {
        lblRealTime = new JLabel("Siia tuleb reaalne aeg", JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        components.add(lblRealTime, gbc);

        lblCategory = new JLabel("Sõna kategooria");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        components.add(lblCategory, gbc);

        cmbCategory = new JComboBox<>(model.getCmbCategories());
        gbc.gridx = 1;
        gbc.gridy = 1;
        components.add(cmbCategory, gbc);

        lblDatabase = new JLabel(model.getDatabaseFile(), JLabel.CENTER);
        lblDatabase.setForeground(Color.RED);
        lblDatabase.setFont(new Font("Verdana", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        components.add(lblDatabase, gbc);

        btnNewGame = new JButton("Uus mäng");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        components.add(btnNewGame, gbc);

        btnLeaderboard = new JButton("Edetabel");
        gbc.gridx = 1;
        gbc.gridy = 3;
        components.add(btnLeaderboard, gbc);
    }

    public JLabel getLblRealTime() {
        return lblRealTime;
    }

    public JComboBox<String> getCmbCategory() {
        return cmbCategory;
    }

    public JButton getBtnNewGame() {
        return btnNewGame;
    }

    public JButton getBtnLeaderboard() {
        return btnLeaderboard;
    }
}
