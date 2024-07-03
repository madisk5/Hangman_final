package views.panels;

import helpers.TextFieldLimit;
import models.Model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameBoard extends JPanel {
    private Model model;
    private GridBagConstraints gbc = new GridBagConstraints();
    private JLabel lblGameTime;
    private JTextField txtChar;
    private JLabel lblError;
    private JButton btnSend;
    private JButton btnCancel;
    private JLabel lblImage;
    private JLabel lblResult;

    public GameBoard(Model model) {
        this.model = model;

        setLayout(new BorderLayout());
        setBackground(new Color(200,255,175));
        setBorder(new EmptyBorder(5,5,5,5));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2,2,2,2);

        JPanel components = new JPanel(new GridBagLayout());
        components.setBackground(new Color(140,185,250));

        JPanel pnlResult = new JPanel(new FlowLayout());
        pnlResult.setBackground(new Color(250,200,235));

        createUIComponents(components);
        createImagePlace(components);
        createResultPlace(pnlResult);

        add(components, BorderLayout.CENTER);
        add(pnlResult, BorderLayout.NORTH);
    }

    private void createUIComponents(JPanel components) {
        lblGameTime = new JLabel("Siia tuleb mänguaeg", JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        components.add(lblGameTime, gbc);

        JLabel lblChar = new JLabel("Sisesta täht: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        components.add(lblChar, gbc);

        txtChar = new JTextField("", 10) {
            @Override
            public void addNotify() {
                super.addNotify();
                requestFocus();
            }
        };
        txtChar.setEnabled(false);
        txtChar.setHorizontalAlignment(JTextField.CENTER);
        txtChar.setDocument(new TextFieldLimit(1));
        gbc.gridx = 1;
        gbc.gridy = 1;
        components.add(txtChar, gbc);

        lblError = new JLabel("Vigased tähed: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        components.add(lblError, gbc);

        btnSend = new JButton("Saada");
        btnSend.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        components.add(btnSend, gbc);

        btnCancel = new JButton("Katkesta");
        btnCancel.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridy = 3;
        components.add(btnCancel, gbc);
    }

    private void createImagePlace(JPanel components) {
        lblImage = new JLabel();
        ImageIcon imageIcon = new ImageIcon(model.getImageFiles().get(model.getImageFiles().size()-1));
        lblImage.setIcon(imageIcon);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        components.add(lblImage, gbc);
    }

    private BufferedImage temporaryImage() {
        BufferedImage image = new BufferedImage(125, 125, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(new Color(255, 0, 0));
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        return image;
    }

    private void createResultPlace(JPanel pnlResult) {
        lblResult = new JLabel("T _ _ E M _ S");
        lblResult.setFont(new Font("Courier New", Font.BOLD, 24));
        pnlResult.add(lblResult);
    }

    public JLabel getLblGameTime() {
        return lblGameTime;
    }

    public JTextField getTxtChar() {
        return txtChar;
    }

    public JLabel getLblError() {
        return lblError;
    }

    public JButton getBtnSend() {
        return btnSend;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public JLabel getLblImage() {
        return lblImage;
    }

    public JLabel getLblResult() {
        return lblResult;
    }

    public void resetErrorLabel() {
        lblError.setText("Vigased tähed: ");
    }

    public void resetGameTimeLabel() {
        lblGameTime.setText("00:00");
    }
}
