package filemanager.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import java.awt.Color;

import static filemanager.utils.Constants.EDIT_LABEL;
import static filemanager.utils.Constants.OPEN_LABEL;
import static filemanager.utils.Constants.PRINT_LABEL;

public class FileOperationsView extends JToolBar {

    private JButton openFile;
    private JButton printFile;
    private JButton editFile;

    private JLabel fileStatus;

    public FileOperationsView() {
        setFloatable(false);
        initFileOperations();
    }

    private void initFileOperations() {
        buildOpenFileButton();
        buildEditFileButton();
        buildPrintFileButton();
        buildFileStatusLabel();
    }

    private void buildOpenFileButton() {
        openFile = new JButton(OPEN_LABEL);
        openFile.setMnemonic('o');
        add(openFile);
    }

    private void buildEditFileButton() {
        editFile = new JButton(EDIT_LABEL);
        editFile.setMnemonic('e');
        add(editFile);
    }

    private void buildPrintFileButton() {
        printFile = new JButton(PRINT_LABEL);
        printFile.setMnemonic('p');
        add(printFile);
    }

    private void buildFileStatusLabel() {
        fileStatus = new JLabel();
        fileStatus.setForeground(Color.RED);
        fileStatus.setVisible(true);
        add(fileStatus);
    }

    public void setFileStatus(String text) {
        fileStatus.setText(text);
    }

    public void clearFileStatus() {
        fileStatus.setText("");
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

    public JLabel getFileStatus() {
        return fileStatus;
    }
}
