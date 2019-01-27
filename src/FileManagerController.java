import file_table.FileTableModel;
import file_tree.FileTreeCellRenderer;
import file_tree.FileTreeModel;
import preview.Preview;
import preview.PreviewFactory;

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.List;

public class FileManagerController {

    private FileManagerView view;

    private PreviewFactory previewFactory;

    private File currentFile;

    private FileSystemView fileSystemView;
    private ListSelectionListener listSelectionListener;
    private Desktop desktop;

    public FileManagerController() {
        fileSystemView = FileSystemView.getFileSystemView();
        desktop = Desktop.getDesktop();
        previewFactory = new PreviewFactory();
        initView();
    }

    private void initView() {
        view = new FileManagerView();
        view.getTree().setModel(new FileTreeModel(new DefaultMutableTreeNode()));
        view.getTable().setModel(new FileTableModel());
        view.disableFileOperations();
    }

    private void enableFileOperations() {
        view.getOpenFile().setEnabled(desktop.isSupported(Desktop.Action.OPEN));
        view.getEditFile().setEnabled(desktop.isSupported(Desktop.Action.EDIT));
        view.getPrintFile().setEnabled(desktop.isSupported(Desktop.Action.PRINT));
    }

    public JFrame createGUI() {

        TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent tse){
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                showChildren(node);
                updateView((File)node.getUserObject());
            }
        };

        view.getTree().addTreeSelectionListener(treeSelectionListener);
        view.getTree().setCellRenderer(new FileTreeCellRenderer());

        listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                updateView(getSelectedFile());

                if (currentFile.isDirectory()) {
                    view.disableFileOperations();
                } else {
                    enableFileOperations();
                }
            }
        };
        view.getTable().getSelectionModel().addListSelectionListener(listSelectionListener);

        view.getOpenFile().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                try {
                    if (!currentFile.isDirectory()) {
                        desktop.open(currentFile);
                    }
                } catch(Throwable t) {
                }
            }
        });

        view.getEditFile().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                try {
                    if (!currentFile.isDirectory()) {
                        desktop.edit(currentFile);
                    }
                } catch(Throwable t) {
                }
            }
        });

        view.getPrintFile().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                try {
                    if (!currentFile.isDirectory()) {
                        desktop.print(currentFile);
                    }
                } catch(Throwable t) {
                }
            }
        });

        return view.getFrame();
    }

    private File getSelectedFile() {
        int selectedRow = view.getTable().getSelectionModel().getLeadSelectionIndex();
        int rowCount = view.getTable().getModel().getRowCount();

        if (selectedRow >= 0 && selectedRow < rowCount) {
            int modelRow = view.getTable().convertRowIndexToModel(selectedRow);
            return ((FileTableModel)view.getTable().getModel()).getFile(modelRow);
        }

        return null;
    }

    private void updateView(File selectedFile) {

        if (selectedFile != null && selectedFile != currentFile) {
            currentFile = selectedFile;
            view.getFileStatus().setVisible(false);
            setFileDetails(currentFile);
            previewFile(currentFile);
        }
    }

    private void previewFile(File file) {
        Preview preview = previewFactory.createPreview(view, file);
        preview.show();
        view.getPreview().repaint();
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
