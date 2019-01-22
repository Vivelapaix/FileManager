import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch(Exception e) {
                }

                FileManager manager = new FileManager();
                JFrame frame = manager.createGUI();

                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}