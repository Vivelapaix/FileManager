package filemanager.models;

import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.File;
import java.util.Arrays;

public class FileTreeModel extends DefaultTreeModel {

    public FileTreeModel(TreeNode root) {
        super(root);

        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File[] roots = fileSystemView.getRoots();

        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            ((DefaultMutableTreeNode)root).add(node);

            Arrays.stream(fileSystemView.getFiles(fileSystemRoot, true))
                    .sorted((o1, o2) -> o1.getName()
                            .compareToIgnoreCase(o2.getName()))
                    .map(DefaultMutableTreeNode::new)
                    .forEach(node::add);
        }
    }
}
