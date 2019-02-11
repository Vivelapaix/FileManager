package filemanager.models;

import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.Arrays;

/**
 * FileTreeModel is the class that defines the hierarchical file tree.
 */
public class FileTreeModel extends DefaultTreeModel {

    /**
     * Constructor initializing system roots as children of fictive tree root.
     *
     * @param root not visible parent node for combining system roots
     */
    public FileTreeModel(TreeNode root) {
        super(root);

        FileSystemView fileSystemView = FileSystemView.getFileSystemView();

        Arrays.stream(fileSystemView.getRoots())
                .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
                .map(DefaultMutableTreeNode::new)
                .forEach(((DefaultMutableTreeNode)root)::add);
    }
}
