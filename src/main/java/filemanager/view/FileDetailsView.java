package filemanager.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * FileDetailsView is the class which combines the contents of current directory
 * as table, file operations on selected file and properties of selected file.
 *
 * @see filemanager.view.FileOperationsView
 * @see filemanager.view.FilePropertiesView
 */
class FileDetailsView extends JPanel {

    private JTable table;

    private FileOperationsView fileOperations;

    private FilePropertiesView fileProperties;

    FileDetailsView() {
        super(new BorderLayout(3,3));
        initFileDetails();
    }

    /**
     * Class constructor initializing file table with file operations and
     * file properties.
     */
    private void initFileDetails() {
        initFileTable();
        initFileView();
    }

    private void initFileTable() {
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setShowVerticalLines(false);

        JScrollPane tableScroll = new JScrollPane(table);
        Dimension d = tableScroll.getPreferredSize();
        tableScroll.setPreferredSize(
                new Dimension((int)d.getWidth(), (int)d.getHeight()/2));

        add(tableScroll, BorderLayout.CENTER);
    }

    private void initFileView() {
        JPanel fileView = new JPanel(new BorderLayout(3,3));
        fileOperations = new FileOperationsView();
        fileProperties = new FilePropertiesView();

        fileView.add(fileOperations, BorderLayout.NORTH);
        fileView.add(fileProperties, BorderLayout.CENTER);
        add(fileView, BorderLayout.SOUTH);
    }

    public JTable getTable() {
        return table;
    }

    public FileOperationsView getFileOperations() {
        return fileOperations;
    }

    public FilePropertiesView getFileProperties() {
        return fileProperties;
    }
}
