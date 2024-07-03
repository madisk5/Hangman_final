package listeners;

import models.Model;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ComboboxChange implements ItemListener {
    private final Model model;

    public ComboboxChange(Model model) {
        this.model = model;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            model.setSelectedCategory(e.getItem().toString());
            JOptionPane.showMessageDialog(null, e.getItem().toString());
        }
    }
}
