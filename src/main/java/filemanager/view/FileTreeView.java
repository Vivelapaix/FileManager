package filemanager.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * FileTreeView is the class that displays the hierarchical file system structure.
 */
class FileTreeView extends JPanel {

    private JTree tree;

    /**
     * Class constructor initializing JTree component.
     */
    FileTreeView() {
        super(new BorderLayout(3,3));
        initFileTree();
    }

    private void initFileTree() {
        tree = new JTree();
        tree.setRootVisible(false);
        tree.expandRow(0);
        tree.setVisibleRowCount(15);
        tree.getSelectionModel()
                .setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        JScrollPane treeScroll = new JScrollPane(tree);
        Dimension d = treeScroll.getPreferredSize();
        treeScroll.setPreferredSize(new Dimension(200, (int)d.getHeight()));

        add(treeScroll, BorderLayout.CENTER);
    }

    public JTree getTree() {
        return tree;
    }
}
