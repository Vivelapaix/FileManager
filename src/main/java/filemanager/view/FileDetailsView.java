package filemanager.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class FileDetailsView extends JPanel {

    private JTable table;

    private FileOperationsView fileOperations;

    private FilePropertiesView fileProperties;

    public FileDetailsView() {
        super(new BorderLayout(3,3));
        initFileDetails();
    }

    private void initFileDetails() {
        buildFileTable();
        buildFileView();
    }

    private void buildFileTable() {
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

    private void buildFileView() {
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
