import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

public class FileManagerView {

    private static final String APP_NAME = "File Manager";
    private static final String FILE_LABEL = "File";
    private static final String PATH_LABEL = "Path";
    private static final String MODIFIED_LABEL = "Modified";
    private static final String SIZE_LABEL = "Size";

    private JFrame frame;
    private JPanel guiPanel;

    private JTree tree;
    private DefaultTreeModel treeModel;

    private JTable table;
    private JProgressBar progressBar;

    private JLabel fileName;
    private JTextField path;
    private JLabel date;
    private JLabel size;

    private FileSystemView fileSystemView;

    public FileManagerView() {
        fileSystemView = FileSystemView.getFileSystemView();
        buildFrame();
    }

    private void buildFrame() {
        frame = new JFrame(APP_NAME);
        frame.setLayout(new BorderLayout(3, 3));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(frame.getSize());
        frame.setContentPane(getGUI());
        //frame.setLocationByPlatform(true);
    }

    private Container getGUI() {
        guiPanel = new JPanel(new BorderLayout(3,3));
        guiPanel.setBorder(new EmptyBorder(5,5,5,5));

        guiPanel.add(createFileTreeScroll(), BorderLayout.CENTER);

        return guiPanel;
    }

    private JTree createFileTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        treeModel = new DefaultTreeModel(root);

        File[] roots = fileSystemView.getRoots();
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            root.add(node);

            File[] files = fileSystemView.getFiles(fileSystemRoot, true);
            for (File file : files) {
                if (file.isDirectory()) {
                    node.add(new DefaultMutableTreeNode(file));
                }
            }
        }

        tree = new JTree(treeModel);
        tree.setRootVisible(false);
        tree.expandRow(0);
        tree.setVisibleRowCount(15);
        tree.getSelectionModel()
                .setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        return tree;
    }

    private JScrollPane createFileTreeScroll() {
        tree = createFileTree();
        JScrollPane treeScroll = new JScrollPane(tree);
        Dimension d = treeScroll.getPreferredSize();
        treeScroll.setPreferredSize(
                new Dimension(200, (int)d.getHeight()));

        return treeScroll;
    }

    private JTable createTable() {
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setShowVerticalLines(false);

        return table;
    }

    private JScrollPane createTableScroll() {
        table = createTable();
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

        return fileProperties;
    }

    private JPanel createFileDetailsView() {
        JPanel detailView = new JPanel(new BorderLayout(3,3));

        detailView.add(createTableScroll(), BorderLayout.CENTER);

        return  detailView;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JTree getTree() {
        return tree;
    }

    public JPanel getGui() {
        return guiPanel;
    }

    public JTable getTable() {
        return table;
    }
}
