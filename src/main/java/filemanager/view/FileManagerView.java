package filemanager.view;

import filemanager.preview.ImagePanel;
import filemanager.preview.PreviewView;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import static filemanager.utils.Constants.APP_NAME;
import static filemanager.utils.Constants.IMAGE_PREVIEW_LABEL;
import static filemanager.utils.Constants.NO_PREVIEW_LABEL;
import static filemanager.utils.Constants.TEXT_PREVIEW_LABEL;

public class FileManagerView implements PreviewView {

    private JFrame frame;
    private JPanel guiPanel;

    private JTree tree;

    private JTable table;

    private JPanel preview;
    private JLabel noPreview;
    private JTextArea textPreview;
    private ImagePanel imagePreview;
    private JScrollPane textPreviewScroll;

    private FileOperationsView fileOperations;

    private FilePropertiesView fileProperties;

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

        JSplitPane sp = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createFileTreeView(), createFileDetailsView());
        sp.setResizeWeight(0.4);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                sp,
                createPreview());
        splitPane.setResizeWeight(0.6);

        guiPanel.add(splitPane, BorderLayout.CENTER);

        return guiPanel;
    }

    private JScrollPane createFileTree() {
        tree = new JTree();
        tree.setRootVisible(false);
        tree.expandRow(0);
        tree.setVisibleRowCount(15);
        tree.getSelectionModel()
                .setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        JScrollPane treeScroll = new JScrollPane(tree);
        Dimension d = treeScroll.getPreferredSize();
        treeScroll.setPreferredSize(new Dimension(200, (int)d.getHeight()));

        return treeScroll;
    }

    private JPanel createFileTreeView() {
        JPanel treeView = new JPanel(new BorderLayout(3,3));
        treeView.add(createFileTree(), BorderLayout.CENTER);

        return treeView;
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

    private JPanel createPreview() {
        preview = new JPanel(new CardLayout(3, 3));
        preview.setBorder(BorderFactory.createLineBorder(Color.gray));
        Dimension d = preview.getPreferredSize();
        preview.setPreferredSize(new Dimension(200, (int)d.getHeight()));

        noPreview = new JLabel();
        noPreview.setHorizontalAlignment(JLabel.CENTER);
        noPreview.setVisible(false);

        textPreview = new JTextArea();
        textPreview.setWrapStyleWord(true);
        textPreview.setLineWrap(true);
        textPreview.setEditable(false);
        textPreviewScroll = new JScrollPane(textPreview,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        textPreviewScroll.setVisible(false);

        imagePreview = new ImagePanel();
        imagePreview.setVisible(false);

        preview.add(noPreview, NO_PREVIEW_LABEL);
        preview.add(textPreviewScroll, TEXT_PREVIEW_LABEL);
        preview.add(imagePreview, IMAGE_PREVIEW_LABEL);

        return preview;
    }

    public void hidePreviews() {
        noPreview.setVisible(false);
        textPreviewScroll.setVisible(false);
        imagePreview.setVisible(false);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getGuiPanel() {
        return guiPanel;
    }

    public JTree getTree() {
        return tree;
    }

    public JTable getTable() {
        return table;
    }

    public JScrollPane getTextPreviewScroll() {
        return textPreviewScroll;
    }

    public JPanel getPreview() {
        return preview;
    }

    public JLabel getNoPreview() {
        return noPreview;
    }

    public JTextArea getTextPreview() {
        return textPreview;
    }

    public ImagePanel getImagePreview() {
        return imagePreview;
    }

    public FileOperationsView getFileOperations() {
        return fileOperations;
    }

    public FilePropertiesView getFileProperties() {
        return fileProperties;
    }
}
