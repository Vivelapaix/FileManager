package preview;


import exceptions.ExceptionHandler;
import exceptions.FileManagerException;

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
import java.util.concurrent.ExecutionException;

import static utils.Constants.ERROR_READ_FILE;
import static utils.Constants.FILE_IS_LARGE_FOR_PREVIEW_LABEL;
import static utils.Constants.FILE_LOADING_LABEL;
import static utils.Constants.IMAGE_PREVIEW_LABEL;

public class ImagePreview implements Preview {

    private static final int PREVIEW_BUFFER_SIZE = 20 * 1024 * 1024;

    private static final List<String> EXTENSIONS = Arrays.asList("png", "jpg", "jpeg");
    
    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<>(EXTENSIONS);
    
    private final PreviewView view;
    
    private final ImagePanel panel;
    
    private final File file;

    private final ExceptionHandler exceptionHandler;
    
    public ImagePreview(PreviewView view, ImagePanel panel,
                        File file, ExceptionHandler exceptionHandler) {
        this.view = view;
        this.panel = panel;
        this.file = file;
        this.exceptionHandler = exceptionHandler;
    }

    public void show() {

        SwingWorker<BufferedImage, Object> previewLoader = new SwingWorker<BufferedImage, Object>() {

            @Override
            public BufferedImage doInBackground() throws FileManagerException {
                try {
                    view.hidePreviews();
                    view.getNoPreview().setText(FILE_LOADING_LABEL);
                    return ImageIO.read(getInputStream());
                } catch (IOException e) {
                    throw new FileManagerException(ERROR_READ_FILE, e);
                }
            }

            @Override
            protected void done() {
                view.hidePreviews();
                panel.setVisible(true);
                ((CardLayout)view.getPreview().getLayout())
                        .show(view.getPreview(), IMAGE_PREVIEW_LABEL);
                try {
                    panel.setImage(get());
                } catch (InterruptedException|ExecutionException e) {
                    exceptionHandler.handleException(e.getMessage(), e);
                } catch (Exception e) {
                    exceptionHandler.handleException(e.getMessage(), e);
                }
            }
        };

        if (file.length() < PREVIEW_BUFFER_SIZE) {
            previewLoader.execute();
        } else {
            view.hidePreviews();
            view.getNoPreview().setText(FILE_IS_LARGE_FOR_PREVIEW_LABEL);
        }
    }

    private InputStream getInputStream() throws FileManagerException {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new FileManagerException(ERROR_READ_FILE, e);
        }
    }

    public static boolean acceptsExtension(String extension) {
        return IMAGE_EXTENSIONS.contains(extension);
    }
}
