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
    
    private static final List<String> EXTENSIONS = Arrays.asList("png", "jpg", "jpeg");
    
    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<String>(EXTENSIONS);
    
    private final PreviewView view;
    
    private final ImagePanel panel;
    
    private final File file;
    
    public ImagePreview(PreviewView view, ImagePanel panel, File file) {
        this.view = view;
        this.panel = panel;
        this.file = file;
    }

    public void show() {

        SwingWorker<BufferedImage, Object> previewLoader = new SwingWorker<BufferedImage, Object>() {

            @Override
            public BufferedImage doInBackground() {
                try {
                    view.hidePreviews();
                    view.getNoPreview().setText(Constants.FILE_LOADING_LABEL);
                    return ImageIO.read(getInputStream());
                } catch (IOException e) {
                }
                return null;
            }

            @Override
            protected void done() {
                view.hidePreviews();
                view.getNoPreview().setText(Constants.NO_PREVIEW_AVAILABLE_LABEL);
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

    public static boolean acceptsExtension(String extension) {
        return IMAGE_EXTENSIONS.contains(extension);
    }
}
