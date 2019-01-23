package file_table;

import javax.swing.table.DefaultTableModel;
import java.util.Date;

public class FileTableModel extends DefaultTableModel {

    private static final String[] HEADERS = {
            "Icon",
            "File",
            "Path/name",
            "Size",
            "Modified",
    };

    public FileTableModel() {
        super(HEADERS, 0);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Date.class;
            case 2:
                return Long.class;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}