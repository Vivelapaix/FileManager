package filemanager.models;

import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.File;

public class FileTreeModel extends DefaultTreeModel {
    public FileTreeModel(TreeNode root) {
        super(root);

        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File[] roots = fileSystemView.getRoots();

        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            ((DefaultMutableTreeNode)root).add(node);

            File[] files = fileSystemView.getFiles(fileSystemRoot, true);
            for (File file : files) {
                if (file.isDirectory()) {
                    node.add(new DefaultMutableTreeNode(file));
                }
            }
        }
    }
}
