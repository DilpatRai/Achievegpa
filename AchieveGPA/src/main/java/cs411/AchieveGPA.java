package cs411;

import cs411.ui.LoginForm;

import javax.swing.*;

public class AchieveGPA {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        new LoginForm().setVisible(true);
    }
}