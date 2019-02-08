package filemanager.controller;

import filemanager.exceptions.ExceptionHandler;
import filemanager.models.FileTableModel;
import filemanager.models.FileTreeModel;
import filemanager.preview.Preview;
import filemanager.preview.PreviewFactory;
import filemanager.view.FileManagerView;
import filemanager.view.FileTreeCellRenderer;
import org.apache.log4j.Logger;

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

public class FileManagerController implements ExceptionHandler {

    private static final Logger logger =
            Logger.getLogger(FileManagerController.class.getName());

    private FileManagerView view;

    private final PreviewFactory previewFactory;

    private final FileSystemView fileSystemView;

    private final Desktop desktop;

    private ListSelectionListener listSelectionListener;

    private File currentFile;

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
        view.getFileOperations().disableFileOperations();
    }

    private void enableFileOperations() {
        view.getFileOperations().getOpenFile()
                .setEnabled(desktop.isSupported(Desktop.Action.OPEN));
        view.getFileOperations().getEditFile()
                .setEnabled(desktop.isSupported(Desktop.Action.EDIT));
        view.getFileOperations().getPrintFile()
                .setEnabled(desktop.isSupported(Desktop.Action.PRINT));
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

        view.getTable().getSelectionModel()
                .addListSelectionListener(listSelectionListener);

        initFileOperationsAction();

        return view.getFrame();
    }

    private void initFileOperationsAction() {

        view.getFileOperations().getOpenFile().addActionListener(ae -> {
            try {
                if (!currentFile.isDirectory()) {
                    desktop.open(currentFile);
                }
            } catch(IOException e) {
                handleException(e.getMessage(), e);
            }
        });

        view.getFileOperations().getEditFile().addActionListener(ae -> {
            try {
                if (!currentFile.isDirectory()) {
                    desktop.edit(currentFile);
                }
            } catch(IOException e) {
                handleException(e.getMessage(), e);
            }
        });

        view.getFileOperations().getPrintFile().addActionListener(ae -> {
            try {
                if (!currentFile.isDirectory()) {
                    desktop.print(currentFile);
                }
            } catch(IOException e) {
                handleException(e.getMessage(), e);
            }
        });
    }

    public void handleException(String message, Exception error) {
        logger.error(message, error);
        JOptionPane.showMessageDialog(
                view.getGuiPanel(),
                message,
                error.getClass().getName(),
                JOptionPane.ERROR_MESSAGE
        );
        view.getFileProperties().setErrorFileStatus();
    }

    private File getSelectedFile() {
        int selectedRow = view.getTable().getSelectionModel()
                .getLeadSelectionIndex();
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
            view.getFileOperations().disableFileOperations();
        } else {
            enableFileOperations();
        }
    }

    private void previewFile(File file) {
        Preview preview = previewFactory.createPreview(view, file, this);
        preview.show();
        view.getFilePreview().repaint();
        view.getFileProperties().setOkFileStatus();
    }

    private void showChildren(final DefaultMutableTreeNode node) {
        view.getTree().setEnabled(false);
        view.getFileProperties().setLoadingFileStatus();

        SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
            @Override
            public Void doInBackground() {
                File file = (File) node.getUserObject();
                if (file.isDirectory()) {
                    File[] files = fileSystemView.getFiles(file, true);
                    if (node.isLeaf()) {
                        for (File child : files) {
                            publish(child);
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
                view.getFileProperties().setOkFileStatus();
            }
        };
        worker.execute();
    }

    private void setFileDetails(File file) {
        Icon icon = fileSystemView.getSystemIcon(file);
        view.getFileProperties().getFileName().setIcon(icon);
        view.getFileProperties().getFileName()
                .setText(fileSystemView.getSystemDisplayName(file));
        view.getFileProperties().getFilePath().setText(file.getPath());
        view.getFileProperties().getFileDate()
                .setText(new Date(file.lastModified()).toString());
        view.getFileProperties().getFileSize().setText(file.length() + " bytes");

        view.getGuiPanel().repaint();
    }

    private void setTableData(final File[] files) {
        SwingUtilities.invokeLater(() -> {
            if (view.getTable() == null) {
                view.getTable().setModel(new FileTableModel());
            }
            view.getTable().getSelectionModel()
                    .removeListSelectionListener(listSelectionListener);
            ((FileTableModel)view.getTable().getModel()).setFiles(files);
            view.getTable().getSelectionModel()
                    .addListSelectionListener(listSelectionListener);
        });
    }
}
