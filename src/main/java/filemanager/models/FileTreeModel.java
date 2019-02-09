package filemanager.models;

import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.Arrays;

public class FileTreeModel extends DefaultTreeModel {

    public FileTreeModel(TreeNode root) {
        super(root);

        FileSystemView fileSystemView = FileSystemView.getFileSystemView();

        Arrays.stream(fileSystemView.getRoots())
                .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
                .map(DefaultMutableTreeNode::new)
                .forEach(((DefaultMutableTreeNode)root)::add);
    }
}
