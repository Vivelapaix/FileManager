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

/**
 * FileManagerController is the class that defines
 * tree listener, table listener and file operation button listener,
 * controls loading, previewing files,
 * updating the contents of the selected directory,
 * updating the selected file properties.
 */
public class FileManagerController implements ExceptionHandler {

    private static final Logger logger =
            Logger.getLogger(FileManagerController.class.getName());

    private FileManagerView view;

    private final PreviewFactory previewFactory;

    private final FileSystemView fileSystemView;

    private final Desktop desktop;

    private ListSelectionListener listSelectionListener;

    /**
     * User selected file.
     */
    private File currentFile;

    /**
     * Constructor initializing application view.
     */
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

    /**
     * Enable buttons with system supported file actions.
     */
    private void enableFileOperations() {
        view.getFileOperations().getOpenFile()
                .setEnabled(desktop.isSupported(Desktop.Action.OPEN));
        view.getFileOperations().getEditFile()
                .setEnabled(desktop.isSupported(Desktop.Action.EDIT));
        view.getFileOperations().getPrintFile()
                .setEnabled(desktop.isSupported(Desktop.Action.PRINT));
    }

    /**
     * Create main frame with tree listener, table listener and file operation
     * buttons.
     *
     * @return frame main frame
     */
    public JFrame createGUI() {

        TreeSelectionListener treeSelectionListener = tse -> {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
            showDirectory(node);
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

    /**
     * Create message dialog with message and exception class.
     *
     * @param message for show
     * @param error application exception
     */
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

    /**
     * @return last user selected file in table (in the center panel)
     */
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

    /**
     * Update file properties and preview file if preview is available.
     *
     * @param selectedFile user selected file
     */
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

    /**
     * Create preview and show it in preview panel.
     *
     * @param file user selected file
     * @see filemanager.preview.PreviewFactory#createPreview(FileManagerView,
     * File, ExceptionHandler)
     */
    private void previewFile(File file) {
        Preview preview = previewFactory.createPreview(view, file, this);
        preview.show();
        view.getFilePreview().repaint();
        view.getFileProperties().setOkFileStatus();
    }

    /**
     * Add subdirectories for user selected directory to node as children
     * and set the table content.
     *
     * @param node user selected file or directory in tree panel.
     */
    private void showDirectory(final DefaultMutableTreeNode node) {
        view.getTree().setEnabled(false);
        view.getFileProperties().setLoadingFileStatus();
        view.getGuiPanel().repaint();

        SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
            @Override
            public Void doInBackground() {
                File file = (File) node.getUserObject();

                if (file.isFile() && ((FileTableModel)view.getTable().getModel())
                        .getCurrentDirectory().equals(file.getParentFile())) {
                    return null;
                }

                File[] files;

                if (file.isDirectory()) {
                    files = fileSystemView.getFiles(file, true);

                    if (node.isLeaf() || node.getChildCount() != files.length) {
                        for (File child : files) {
                            publish(child);
                        }
                    }
                } else {
                    file = file.getParentFile();
                    files = fileSystemView.getFiles(file, true);
                }

                setTableData(files, file);

                return null;
            }

            @Override
            protected void process(List<File> chunks) {
                chunks.stream()
                        .sorted((o1, o2) -> o1.getName()
                                .compareToIgnoreCase(o2.getName()))
                        .map(DefaultMutableTreeNode::new)
                        .forEach(node::add);
            }

            @Override
            protected void done() {
                view.getTree().setEnabled(true);
                view.getFileProperties().setOkFileStatus();
                view.getGuiPanel().repaint();
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

    private void setTableData(final File[] files, final File currentDirectory) {
        SwingUtilities.invokeLater(() -> {

            view.getTable().getSelectionModel()
                    .removeListSelectionListener(listSelectionListener);
            ((FileTableModel)view.getTable().getModel())
                    .setFiles(files, currentDirectory);
            view.getTable().getSelectionModel()
                    .addListSelectionListener(listSelectionListener);
        });
    }
}
