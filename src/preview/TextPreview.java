package preview;


import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class TextPreview implements Preview {
    
    private static final String DEFAULT_ENCODING = "UTF-8";
    
    private static final List<String> ACCEPTED_EXTENSIONS_LIST = Arrays.asList("txt", "html", "java",
            "css", "js", "xml", "coffee", "dart", "ts", "rb");
    
    private static final Set<String> ACCEPTED_EXTENSIONS = new HashSet<String>(ACCEPTED_EXTENSIONS_LIST);
    
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

        //Loading preview entries in a separate thread as it can be time consuming
        SwingWorker<String, Object> previewLoader = new SwingWorker<String, Object>() {

            @Override
            public String doInBackground(){
                try {
                    return new String(readContent(), DEFAULT_ENCODING);
                } catch (UnsupportedEncodingException e) {
                }
                return null;
            }

            @Override
            protected void done() {
                view.hidePreviews();
                textPreviewScrollPane.setVisible(true);
                try {
                    textPreview.setText(get());
                } catch (Exception e) {
                }
            }
        };
        previewLoader.execute();
    }

    private byte[] readContent() {

        try {
            byte[] bytes = new byte[(int) file.length()];
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(bytes);
            dis.close();
            return bytes;
        } catch (IOException e) {
        } finally {
        }

        return null;
    }

    public static boolean acceptsEntryExtension(String extension) {
        return ACCEPTED_EXTENSIONS.contains(extension);
    }
}
