package filemanager;

import filemanager.controller.FileManagerController;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(Exception e) {
            }

            FileManagerController manager = new FileManagerController();
            JFrame frame = manager.createGUI();

            frame.pack();
            frame.setVisible(true);
        });
    }
}
