import utils.Constants;
import preview.ImagePanel;
import preview.PreviewView;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import static utils.Constants.APP_NAME;
import static utils.Constants.DIRECTORY_LABEL;
import static utils.Constants.EDIT_LABEL;
import static utils.Constants.FILE_LABEL;
import static utils.Constants.MODIFIED_LABEL;
import static utils.Constants.OPEN_LABEL;
import static utils.Constants.PATH_LABEL;
import static utils.Constants.PRINT_LABEL;
import static utils.Constants.SIZE_LABEL;
import static utils.Constants.SOMETHING_WRONG_LABEL;
import static utils.Constants.TYPE_LABEL;

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

    private JLabel fileName;
    private JTextField path;
    private JLabel date;
    private JLabel size;
    private JRadioButton isDirectory;
    private JRadioButton isFile;

    private JButton openFile;
    private JButton printFile;
    private JButton editFile;

    private JLabel fileStatus;

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

    private JPanel createFileProperties() {
        JPanel fileProperties = new JPanel(new BorderLayout(4,2));
        fileProperties.setBorder(new EmptyBorder(0,6,0,6));

        JPanel labels = new JPanel(new GridLayout(0,1,2,2));
        fileProperties.add(labels, BorderLayout.WEST);

        JPanel values = new JPanel(new GridLayout(0,1,2,2));
        fileProperties.add(values, BorderLayout.CENTER);

        labels.add(new JLabel(FILE_LABEL, JLabel.TRAILING));
        fileName = new JLabel();
        values.add(fileName);

        labels.add(new JLabel(PATH_LABEL, JLabel.TRAILING));
        path = new JTextField(5);
        path.setEditable(false);
        values.add(path);

        labels.add(new JLabel(MODIFIED_LABEL, JLabel.TRAILING));
        date = new JLabel();
        values.add(date);

        labels.add(new JLabel(SIZE_LABEL, JLabel.TRAILING));
        size = new JLabel();
        values.add(size);

        labels.add(new JLabel(TYPE_LABEL, JLabel.TRAILING));
        JPanel flags = new JPanel(new FlowLayout(FlowLayout.LEADING,4,0));
        isDirectory = new JRadioButton(DIRECTORY_LABEL);
        isDirectory.setEnabled(false);
        flags.add(isDirectory);
        isFile = new JRadioButton(FILE_LABEL);
        isFile.setEnabled(false);
        flags.add(isFile);
        values.add(flags);

        return fileProperties;
    }

    private JToolBar createFileOperations() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        openFile = new JButton(OPEN_LABEL);
        openFile.setMnemonic('o');
        toolBar.add(openFile);

        editFile = new JButton(EDIT_LABEL);
        editFile.setMnemonic('e');
        toolBar.add(editFile);

        printFile = new JButton(PRINT_LABEL);
        printFile.setMnemonic('p');
        toolBar.add(printFile);

        fileStatus = new JLabel(SOMETHING_WRONG_LABEL);
        fileStatus.setForeground(Color.RED);
        fileStatus.setVisible(false);
        toolBar.add(fileStatus);

        return toolBar;
    }

    public void disableFileOperations() {
        openFile.setEnabled(false);
        editFile.setEnabled(false);
        printFile.setEnabled(false);
    }

    private JPanel createFileDetailsView() {
        JPanel detailView = new JPanel(new BorderLayout(3,3));
        JPanel fileView = new JPanel(new BorderLayout(3,3));

        detailView.add(createFileTable(), BorderLayout.CENTER);

        fileView.add(createFileOperations(), BorderLayout.NORTH);
        fileView.add(createFileProperties(), BorderLayout.CENTER);
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

        preview.add(noPreview, Constants.NO_PREVIEW_LABEL);
        preview.add(textPreviewScroll, Constants.TEXT_PREVIEW_LABEL);
        preview.add(imagePreview, Constants.IMAGE_PREVIEW_LABEL);

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

    public JLabel getFileName() {
        return fileName;
    }

    public JTextField getPath() {
        return path;
    }

    public JLabel getDate() {
        return date;
    }

    public JLabel getSize() {
        return size;
    }

    public JRadioButton getIsDirectory() {
        return isDirectory;
    }

    public JRadioButton getIsFile() {
        return isFile;
    }

    public JButton getOpenFile() {
        return openFile;
    }

    public JButton getPrintFile() {
        return printFile;
    }

    public JButton getEditFile() {
        return editFile;
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

    public JLabel getFileStatus() {
        return fileStatus;
    }
}
