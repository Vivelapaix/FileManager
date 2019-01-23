import file_table.FileTableModel;
import file_tree.FileTreeCellRenderer;
import file_tree.FileTreeModel;

import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.util.List;

public class FileManagerController {

    private FileManagerView view;

    public FileManagerController() {
        initView();
    }

    private void initView() {
        view = new FileManagerView();
        view.getTree().setModel(new FileTreeModel(new DefaultMutableTreeNode()));
        view.getTable().setModel(new FileTableModel());
    }

    public JFrame createGUI() {

        TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse){
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                showChildren(node);
                //setFileDetails((File)node.getUserObject());
            }
        };

        view.getTree().addTreeSelectionListener(treeSelectionListener);
        view.getTree().setCellRenderer(new FileTreeCellRenderer());

        return view.getFrame();
    }

    private void showChildren(final DefaultMutableTreeNode node) {
        view.getTree().setEnabled(false);

        SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
            @Override
            public Void doInBackground() {
                File file = (File) node.getUserObject();
                if (file.isDirectory()) {
                    File[] files = FileSystemView.getFileSystemView().getFiles(file, true);
                    if (node.isLeaf()) {
                        for (File child : files) {
                            if (child.isDirectory()) {
                                publish(child);
                            }
                        }
                    }
                    //setTableData(files);
                }
                return null;
            }

            @Override
            protected void process(List<File> chunks) {
                for (File child : chunks) {
                    node.add(new DefaultMutableTreeNode(child));
                }
            }

            @Override
            protected void done() {
                view.getTree().setEnabled(true);
            }
        };
        worker.execute();
    }
}
