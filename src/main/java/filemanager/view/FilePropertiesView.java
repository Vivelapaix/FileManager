package filemanager.view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import static filemanager.utils.Constants.DIRECTORY_LABEL;
import static filemanager.utils.Constants.FILE_LABEL;
import static filemanager.utils.Constants.MODIFIED_LABEL;
import static filemanager.utils.Constants.PATH_LABEL;
import static filemanager.utils.Constants.SIZE_LABEL;
import static filemanager.utils.Constants.TYPE_LABEL;

public class FilePropertiesView extends JPanel {

    private JLabel fileName;
    private JTextField filePath;
    private JLabel fileDate;
    private JLabel fileSize;
    private JRadioButton isDirectory;
    private JRadioButton isFile;

    public FilePropertiesView() {
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

        labels.add(new JLabel(TYPE_LABEL, JLabel.TRAILING));
        JPanel flags = new JPanel(new FlowLayout(FlowLayout.LEADING,4,0));
        isDirectory = new JRadioButton(DIRECTORY_LABEL);
        isDirectory.setEnabled(false);
        flags.add(isDirectory);
        isFile = new JRadioButton(FILE_LABEL);
        isFile.setEnabled(false);
        flags.add(isFile);
        values.add(flags);
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

    public JRadioButton getIsDirectory() {
        return isDirectory;
    }

    public JRadioButton getIsFile() {
        return isFile;
    }
}
