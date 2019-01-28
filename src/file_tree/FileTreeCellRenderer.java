package file_tree;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;
import java.io.File;


public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

    private JLabel label;

    public FileTreeCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
    }

    @Override
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        File file = (File) node.getUserObject();

        String filename = file.getName().isEmpty() ? file.getAbsolutePath() : file.getName();

        JLabel label = (JLabel)super.getTreeCellRendererComponent(
                tree, filename, sel, expanded, leaf, row, hasFocus);
        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
        label.setIcon(icon);

        return label;
    }
}
