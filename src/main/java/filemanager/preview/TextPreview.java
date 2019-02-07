package filemanager.preview;


import filemanager.exceptions.ExceptionHandler;
import filemanager.exceptions.FileManagerException;
import filemanager.utils.Constants;
import filemanager.view.FileManagerView;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import java.awt.CardLayout;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static filemanager.utils.Constants.ERROR_READ_FILE;

public class TextPreview implements Preview {
    
    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final int PREVIEW_BUFFER_SIZE = 3 * 1024;
    
    private static final List<String> EXTENSIONS = Arrays.asList("txt", "html", "java",
            "css", "js", "xml", "rb", "py", "csv", "c", "cpp");
    
    private static final Set<String> TEXT_EXTENSIONS = new HashSet<>(EXTENSIONS);

    private final FileManagerView view;
    
    private final JScrollPane textPreviewScrollPane;
    
    private final JTextArea textPreview;
    
    private final File file;

    private final ExceptionHandler exceptionHandler;
    
    public TextPreview(FileManagerView view, JScrollPane textPreviewScrollPane,
                       JTextArea textPreview, File file,
                       ExceptionHandler exceptionHandler) {
        this.view = view;
        this.textPreviewScrollPane = textPreviewScrollPane;
        this.textPreview = textPreview;
        this.file = file;
        this.exceptionHandler = exceptionHandler;
    }

    public void show() {

        SwingWorker<String, Object> previewLoader = new SwingWorker<String, Object>() {

            @Override
            public String doInBackground() throws FileManagerException {
                try {
                    return new String(readContent(), DEFAULT_ENCODING);
                }  catch (UnsupportedEncodingException e) {
                    throw new FileManagerException(ERROR_READ_FILE, e);
                }
            }

            @Override
            protected void done() {
                view.getFilePreview().hidePreviews();
                textPreviewScrollPane.setVisible(true);
                ((CardLayout)view.getFilePreview().getLayout())
                        .show(view.getFilePreview(), Constants.TEXT_PREVIEW_LABEL);
                try {
                    textPreview.setText(get());
                    textPreview.setCaretPosition(0);
                } catch (Exception e) {
                    exceptionHandler.handleException(e.getMessage(), e);
                }
            }
        };
        previewLoader.execute();
    }

    private byte[] readContent() throws FileManagerException {

        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            long fileLength = file.length();
            long size = fileLength > PREVIEW_BUFFER_SIZE ?
                    PREVIEW_BUFFER_SIZE : fileLength;
            byte[] bytes = new byte[(int)size];
            dis.readFully(bytes);

            return bytes;

        } catch (IOException e) {
            throw new FileManagerException(ERROR_READ_FILE, e);
        }
    }

    public static boolean acceptsExtension(String extension) {
        return TEXT_EXTENSIONS.contains(extension);
    }
}
