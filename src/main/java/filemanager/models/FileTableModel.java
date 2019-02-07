package filemanager.models;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.Date;

public class FileTableModel extends AbstractTableModel {

    private File[] files;
    private final FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    private final String[] columns = {
            "Icon",
            "File",
            "Type",
            "Size",
            "Modified",
    };

    public FileTableModel() {
        this(new File[0]);
    }

    private FileTableModel(File[] files) {
        this.files = files;
    }

    public Object getValueAt(int row, int column) {
        File file = files[row];
        switch (column) {
            case 0:
                return fileSystemView.getSystemIcon(file);
            case 1:
                return fileSystemView.getSystemDisplayName(file);
            case 2:
                return getFileExtension(file);
            case 3:
                return file.length();
            case 4:
                return file.lastModified();
            default:
                System.err.println("Error");
        }
        return "";
    }

    public int getColumnCount() {
        return columns.length;
    }

    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return ImageIcon.class;
            case 3:
                return Long.class;
            case 4:
                return Date.class;
        }
        return String.class;
    }

    public String getColumnName(int column) {
        return columns[column];
    }

    public int getRowCount() {
        return files.length;
    }

    public File getFile(int row) {
        return files[row];
    }

    public void setFiles(File[] files) {
        this.files = files;
        fireTableDataChanged();
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    private String getFileExtension(File file) {
        String extension = "";

        try {
            if (file != null && file.exists()) {
                if (file.isDirectory()) {
                    return "Folder";
                }

                String name = fileSystemView.getSystemDisplayName(file);
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
            extension = "Unknown";
        }

        return extension;
    }
}
