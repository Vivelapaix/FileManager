package filemanager;

import filemanager.exceptions.ExceptionHandler;
import filemanager.file_table.FileTableModel;
import filemanager.file_tree.FileTreeCellRenderer;
import filemanager.file_tree.FileTreeModel;
import filemanager.preview.Preview;
import filemanager.preview.PreviewFactory;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static filemanager.utils.Constants.ERROR_SELECT_FILE;
import static filemanager.utils.Constants.SOMETHING_WRONG_LABEL;

public class FileManagerController implements ExceptionHandler {

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

        TreeSelectionListener treeSelectionListener = tse -> {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
            showChildren(node);
            updateView((File)node.getUserObject());
        };

        view.getTree().addTreeSelectionListener(treeSelectionListener);
        view.getTree().setCellRenderer(new FileTreeCellRenderer());

        listSelectionListener = (event) -> {
            try {
                updateView(getSelectedFile());
            } catch (Exception e) {
                handleException(ERROR_SELECT_FILE, e);
            }
        };

        view.getTable().getSelectionModel().addListSelectionListener(listSelectionListener);

        view.getOpenFile().addActionListener(ae -> {
            try {
                if (!currentFile.isDirectory()) {
                    desktop.open(currentFile);
                }
            } catch(IOException e) {
                handleException(e.getMessage(), e);
            }
        });

        view.getEditFile().addActionListener(ae -> {
            try {
                if (!currentFile.isDirectory()) {
                    desktop.edit(currentFile);
                }
            } catch(IOException e) {
                handleException(e.getMessage(), e);
            }
        });

        view.getPrintFile().addActionListener(ae -> {
            try {
                if (!currentFile.isDirectory()) {
                    desktop.print(currentFile);
                }
            } catch(IOException e) {
                handleException(e.getMessage(), e);
            }
        });

        return view.getFrame();
    }

    public void handleException(String message, Exception error) {
        JOptionPane.showMessageDialog(
                view.getGuiPanel(),
                message,
                error.getClass().getName(),
                JOptionPane.ERROR_MESSAGE
        );
        view.setFileStatus(SOMETHING_WRONG_LABEL);
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
            setFileDetails(currentFile);
            previewFile(currentFile);
        }

        if (currentFile.isDirectory()) {
            view.disableFileOperations();
        } else {
            enableFileOperations();
        }
    }

    private void previewFile(File file) {
        Preview preview = previewFactory.createPreview(view, file, this);
        preview.show();
        view.getPreview().repaint();
        view.clearFileStatus();
    }

    private void showChildren(final DefaultMutableTreeNode node) {
        view.getTree().setEnabled(false);

        SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
            @Override
            public Void doInBackground() {
                File file = (File) node.getUserObject();
                if (file.isDirectory()) {
                    File[] files = fileSystemView.getFiles(file, true);
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
        SwingUtilities.invokeLater(() -> {
            if (view.getTable() == null) {
                view.getTable().setModel(new FileTableModel());
            }
            view.getTable().getSelectionModel().removeListSelectionListener(listSelectionListener);
            ((FileTableModel)view.getTable().getModel()).setFiles(files);
            view.getTable().getSelectionModel().addListSelectionListener(listSelectionListener);
        });
    }
}
