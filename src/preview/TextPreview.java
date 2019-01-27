package preview;


import utils.Constants;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import java.awt.CardLayout;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TextPreview implements Preview {
    
    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final int PREVIEW_BUFFER_SIZE = 3 * 1024;
    
    private static final List<String> EXTENSIONS = Arrays.asList("txt", "html", "java",
            "css", "js", "xml", "rb", "py", "csv", "c", "cpp");
    
    private static final Set<String> TEXT_EXTENSIONS = new HashSet<String>(EXTENSIONS);

    private final PreviewView view;
    
    private final JScrollPane textPreviewScrollPane;
    
    private final JTextArea textPreview;
    
    private final File file;
    
    public TextPreview(PreviewView view, JScrollPane textPreviewScrollPane, JTextArea textPreview, File file) {
        this.view = view;
        this.textPreviewScrollPane = textPreviewScrollPane;
        this.textPreview = textPreview;
        this.file = file;
    }

    public void show() {

        SwingWorker<String, Object> previewLoader = new SwingWorker<String, Object>() {

            @Override
            public String doInBackground(){
                try {
                    return new String(readContent(), DEFAULT_ENCODING);
                } catch (Exception e) {
                }
                return null;
            }

            @Override
            protected void done() {
                view.hidePreviews();
                textPreviewScrollPane.setVisible(true);
                ((CardLayout)view.getPreview().getLayout())
                        .show(view.getPreview(), Constants.TEXT_PREVIEW_LABEL);
                try {
                    textPreview.setText(get());
                } catch (Exception e) {
                }
            }
        };
        previewLoader.execute();
    }

    private byte[] readContent() {

        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            long fileLength = file.length();
            long size = fileLength > PREVIEW_BUFFER_SIZE ?
                    PREVIEW_BUFFER_SIZE : fileLength;
            byte[] bytes = new byte[(int)size];
            dis.read(bytes);

            return bytes;

        } catch (IOException e) {

        }

        return null;
    }

    public static boolean acceptsExtension(String extension) {
        return TEXT_EXTENSIONS.contains(extension);
    }
}
