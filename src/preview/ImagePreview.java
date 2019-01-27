package preview;


import utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;
import java.awt.CardLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImagePreview implements Preview {
    
    private static final List<String> ACCEPTED_EXTENSIONS_LIST = Arrays.asList("png", "jpg", "jpeg");
    
    private static final Set<String> ACCEPTED_EXTENSIONS = new HashSet<String>(ACCEPTED_EXTENSIONS_LIST);
    
    private final PreviewView view;
    
    private final ImagePanel panel;
    
    private final File file;
    
    public ImagePreview(PreviewView view, ImagePanel panel, File file) {
        this.view = view;
        this.panel = panel;
        this.file = file;
    }

    public void show() {

        //Loading preview entries in a separate thread as it can be time consuming
        SwingWorker<BufferedImage, Object> previewLoader = new SwingWorker<BufferedImage, Object>() {

            @Override
            public BufferedImage doInBackground() {
                try {
                    view.hidePreviews();
                    view.getNoPreview().setText("File is loading...");
                    return ImageIO.read(getInputStream());
                } catch (IOException e) {
                }
                return null;
            }

            @Override
            protected void done() {
                view.hidePreviews();
                view.getNoPreview().setText("NO PREVIEW AVAILABLE");
                panel.setVisible(true);
                ((CardLayout)view.getPreview().getLayout())
                        .show(view.getPreview(), Constants.IMAGE_PREVIEW_LABEL);
                try {
                    panel.setImage(get());
                } catch (Exception e) {
                }
            }
        };
        previewLoader.execute();
    }

    public InputStream getInputStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public static boolean acceptsEntryExtension(String extension) {
        return ACCEPTED_EXTENSIONS.contains(extension);
    }
}
