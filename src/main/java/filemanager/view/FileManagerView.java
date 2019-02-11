package filemanager.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import static filemanager.utils.Constants.APP_NAME;

/**
 * FileManagerView is the class which is the main GUI panel of all application.
 */
public class FileManagerView {

    private JFrame frame;
    private JPanel guiPanel;

    private FileTreeView fileTree;
    private FileDetailsView fileDetails;
    private FilePreviewView filePreview;

    /**
     * Class constructor initializing main frame.
     */
    public FileManagerView() {
        initFrame();
    }

    private void initFrame() {
        frame = new JFrame(APP_NAME);
        frame.setLayout(new BorderLayout(3, 3));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1, 1));
        frame.setContentPane(getGUI());
    }

    /**
     * The method initializes view of the file system tree, view of the contents
     * of the current directory, view of the contents of the selected file
     * (preview).
     *
     * @return guiPanel the main panel of this application
     */
    private Container getGUI() {
        guiPanel = new JPanel(new BorderLayout(3,3));
        guiPanel.setBorder(new EmptyBorder(5,5,5,5));

        fileTree = new FileTreeView();
        fileDetails = new FileDetailsView();
        filePreview = new FilePreviewView();

        JSplitPane sp = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                fileTree, fileDetails);
        sp.setResizeWeight(0.4);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                sp,
                filePreview);
        splitPane.setResizeWeight(0.6);

        guiPanel.add(splitPane, BorderLayout.CENTER);

        return guiPanel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getGuiPanel() {
        return guiPanel;
    }

    public JTable getTable() {
        return fileDetails.getTable();
    }

    public JTree getTree() {
        return fileTree.getTree();
    }

    public FilePreviewView getFilePreview() {
        return filePreview;
    }

    public FileOperationsView getFileOperations() {
        return fileDetails.getFileOperations();
    }

    public FilePropertiesView getFileProperties() {
        return fileDetails.getFileProperties();
    }
}
