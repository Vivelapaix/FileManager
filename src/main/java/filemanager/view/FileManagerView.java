package filemanager.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import static filemanager.utils.Constants.APP_NAME;

public class FileManagerView {

    private JFrame frame;
    private JPanel guiPanel;

    private JTable table;

    private FileOperationsView fileOperations;

    private FilePropertiesView fileProperties;

    private FilePreviewView filePreview;

    private FileTreeView fileTree;

    public FileManagerView() {
        buildFrame();
    }

    private void buildFrame() {
        frame = new JFrame(APP_NAME);
        frame.setLayout(new BorderLayout(3, 3));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1, 1));
        frame.setContentPane(getGUI());
    }

    private Container getGUI() {
        guiPanel = new JPanel(new BorderLayout(3,3));
        guiPanel.setBorder(new EmptyBorder(5,5,5,5));

        filePreview = new FilePreviewView();
        fileTree = new FileTreeView();

        JSplitPane sp = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                fileTree, createFileDetailsView());
        sp.setResizeWeight(0.4);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                sp,
                filePreview);
        splitPane.setResizeWeight(0.6);

        guiPanel.add(splitPane, BorderLayout.CENTER);

        return guiPanel;
    }

    private JScrollPane createFileTable() {
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setShowVerticalLines(false);

        JScrollPane tableScroll = new JScrollPane(table);
        Dimension d = tableScroll.getPreferredSize();
        tableScroll.setPreferredSize(
                new Dimension((int)d.getWidth(), (int)d.getHeight()/2));

        return tableScroll;
    }

    private JPanel createFileDetailsView() {
        JPanel detailView = new JPanel(new BorderLayout(3,3));
        JPanel fileView = new JPanel(new BorderLayout(3,3));
        fileOperations = new FileOperationsView();
        fileProperties = new FilePropertiesView();

        detailView.add(createFileTable(), BorderLayout.CENTER);

        fileView.add(fileOperations, BorderLayout.NORTH);
        fileView.add(fileProperties, BorderLayout.CENTER);
        detailView.add(fileView, BorderLayout.SOUTH);

        return  detailView;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getGuiPanel() {
        return guiPanel;
    }

    public JTable getTable() {
        return table;
    }

    public FileTreeView getFileTree() {
        return fileTree;
    }

    public FilePreviewView getFilePreview() {
        return filePreview;
    }

    public FileOperationsView getFileOperations() {
        return fileOperations;
    }

    public FilePropertiesView getFileProperties() {
        return fileProperties;
    }
}
