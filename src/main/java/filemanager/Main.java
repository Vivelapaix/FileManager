package filemanager;

import filemanager.controller.FileManagerController;
import org.apache.log4j.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * This program implements an File Manager application.
 *
 * The operations include opening, editing, printing, previewing and searching
 * for files.
 *
 * The preview is available only for text files and images.
 */
class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        systemLookAndFeel();

        SwingUtilities.invokeLater(() -> {

            FileManagerController manager = new FileManagerController();
            JFrame frame = manager.createGUI();

            frame.pack();
            frame.setVisible(true);
        });
    }

    private static void systemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            logger.info(e.getMessage(), e);
        }
    }
}
