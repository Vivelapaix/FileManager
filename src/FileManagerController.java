import file_table.FileTableModel;
import file_tree.FileTreeCellRenderer;
import file_tree.FileTreeModel;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.Desktop;
import java.io.File;
import java.util.Date;
import java.util.List;

public class FileManagerController {

    private FileManagerView view;

    private FileSystemView fileSystemView;
    private ListSelectionListener listSelectionListener;
    private Desktop desktop;

    public FileManagerController() {
        fileSystemView = FileSystemView.getFileSystemView();
        desktop = Desktop.getDesktop();
        initView();
    }

    private void initView() {
        view = new FileManagerView();
        view.getTree().setModel(new FileTreeModel(new DefaultMutableTreeNode()));
        view.getTable().setModel(new FileTableModel());

        view.getOpenFile().setEnabled(desktop.isSupported(Desktop.Action.OPEN));
        view.getEditFile().setEnabled(desktop.isSupported(Desktop.Action.EDIT));
        view.getPrintFile().setEnabled(desktop.isSupported(Desktop.Action.PRINT));
    }

    public JFrame createGUI() {

        TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse){
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                showChildren(node);
                setFileDetails((File)node.getUserObject());
            }
        };

        view.getTree().addTreeSelectionListener(treeSelectionListener);
        view.getTree().setCellRenderer(new FileTreeCellRenderer());

        listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                int row = view.getTable().getSelectionModel().getLeadSelectionIndex();
                setFileDetails( ((FileTableModel)view.getTable().getModel()).getFile(row) );
            }
        };

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
                    setTableData(files);
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

    private void setFileDetails(File file) {
        //currentFile = file;
        Icon icon = fileSystemView.getSystemIcon(file);
        view.getFileName().setIcon(icon);
        view.getFileName().setText(fileSystemView.getSystemDisplayName(file));
        view.getPath().setText(file.getPath());
        view.getDate().setText(new Date(file.lastModified()).toString());
        view.getSize().setText(file.length() + " bytes");
        view.getIsDirectory().setSelected(file.isDirectory());
        view.getIsFile().setSelected(file.isFile());

        view.getGuiPanel().repaint();
    }

    private void setTableData(final File[] files) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (view.getTable() == null) {
                    view.getTable().setModel(new FileTableModel());
                }
                view.getTable().getSelectionModel().removeListSelectionListener(listSelectionListener);
                ((FileTableModel)view.getTable().getModel()).setFiles(files);
                view.getTable().getSelectionModel().addListSelectionListener(listSelectionListener);
            }
        });
    }
}
