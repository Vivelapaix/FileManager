package filemanager.view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import static filemanager.utils.Constants.FILE_LABEL;
import static filemanager.utils.Constants.FILE_LOADING_LABEL;
import static filemanager.utils.Constants.MODIFIED_LABEL;
import static filemanager.utils.Constants.OK_LABEL;
import static filemanager.utils.Constants.PATH_LABEL;
import static filemanager.utils.Constants.SIZE_LABEL;
import static filemanager.utils.Constants.SOMETHING_WRONG_LABEL;
import static filemanager.utils.Constants.STATUS_LABEL;

/**
 * FilePropertiesView is the class that displays main information about
 * selected file.
 */
public class FilePropertiesView extends JPanel {

    private JLabel fileName;
    private JTextField filePath;
    private JLabel fileDate;
    private JLabel fileSize;
    /**
     * File status indicates state of file like OK, Loading or Error
     * occurred while opening the file.
     */
    private JLabel fileStatus;

    /**
     * Class constructor initializing components for file properties.
     */
    FilePropertiesView() {
        super(new BorderLayout(4,2));
        setBorder(new EmptyBorder(0,6,0,6));
        initFileProperties();
    }

    private void initFileProperties() {
        JPanel labels = new JPanel(new GridLayout(0,1,2,2));
        add(labels, BorderLayout.WEST);

        JPanel values = new JPanel(new GridLayout(0,1,2,2));
        add(values, BorderLayout.CENTER);

        labels.add(new JLabel(FILE_LABEL, JLabel.TRAILING));
        fileName = new JLabel();
        values.add(fileName);

        labels.add(new JLabel(PATH_LABEL, JLabel.TRAILING));
        filePath = new JTextField(5);
        filePath.setEditable(false);
        values.add(filePath);

        labels.add(new JLabel(MODIFIED_LABEL, JLabel.TRAILING));
        fileDate = new JLabel();
        values.add(fileDate);

        labels.add(new JLabel(SIZE_LABEL, JLabel.TRAILING));
        fileSize = new JLabel();
        values.add(fileSize);

        labels.add(new JLabel(STATUS_LABEL, JLabel.TRAILING));
        fileStatus = new JLabel();
        fileStatus.setFont(
                new Font(null, Font.BOLD, fileStatus.getFont().getSize()));
        values.add(fileStatus);
    }

    public void setErrorFileStatus() {
        fileStatus.setForeground(Color.RED);
        fileStatus.setText(SOMETHING_WRONG_LABEL);
    }

    public void setLoadingFileStatus() {
        fileStatus.setForeground(Color.BLUE);
        fileStatus.setText(FILE_LOADING_LABEL);
    }

    public void setOkFileStatus() {
        fileStatus.setForeground(Color.GREEN);
        fileStatus.setText(OK_LABEL);
    }

    public JLabel getFileName() {
        return fileName;
    }

    public JTextField getFilePath() {
        return filePath;
    }

    public JLabel getFileDate() {
        return fileDate;
    }

    public JLabel getFileSize() {
        return fileSize;
    }
}
