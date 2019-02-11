package filemanager.view;

import javax.swing.JButton;
import javax.swing.JToolBar;

import static filemanager.utils.Constants.EDIT_LABEL;
import static filemanager.utils.Constants.OPEN_LABEL;
import static filemanager.utils.Constants.PRINT_LABEL;

/**
 * FileOperationsView is the class that defines buttons with available
 * operations on the selected file.
 */
public class FileOperationsView extends JToolBar {

    private JButton openFile;
    private JButton printFile;
    private JButton editFile;

    /**
     * Class constructor initializing buttons with available operations.
     */
    FileOperationsView() {
        setFloatable(false);
        initFileOperations();
    }

    private void initFileOperations() {
        initOpenFileButton();
        initEditFileButton();
        initPrintFileButton();
    }

    private void initOpenFileButton() {
        openFile = new JButton(OPEN_LABEL);
        openFile.setMnemonic('o');
        add(openFile);
    }

    private void initEditFileButton() {
        editFile = new JButton(EDIT_LABEL);
        editFile.setMnemonic('e');
        add(editFile);
    }

    private void initPrintFileButton() {
        printFile = new JButton(PRINT_LABEL);
        printFile.setMnemonic('p');
        add(printFile);
    }

    public void disableFileOperations() {
        openFile.setEnabled(false);
        editFile.setEnabled(false);
        printFile.setEnabled(false);
    }

    public JButton getOpenFile() {
        return openFile;
    }

    public JButton getPrintFile() {
        return printFile;
    }

    public JButton getEditFile() {
        return editFile;
    }
}
