package filemanager.preview;


import filemanager.exceptions.ExceptionHandler;
import filemanager.view.FileManagerView;

import java.io.File;

/**
 * PreviewFactory is the class that is used to create empty preview,
 * text preview and image preview.
 */
public class PreviewFactory {

    /**
     * Create preview class depending on file type.
     *
     * @param view FileManager main view
     * @param file user selected file
     * @param exceptionHandler exceprion handler for errors
     * @return class depending on file type
     * @see filemanager.preview.EmptyPreview
     * @see filemanager.preview.TextPreview
     * @see filemanager.preview.ImagePreview
     */
    public Preview createPreview(FileManagerView view, File file,
                                 ExceptionHandler exceptionHandler) {
        if (file.isDirectory()) {
            return new EmptyPreview(view, view.getFilePreview().getNoPreview());
        }

        String fileExtension = "";
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex >= 0 && lastDotIndex <= fileName.length() - 2) {
            fileExtension = fileName.substring(lastDotIndex + 1).toLowerCase();
        }

        if (TextPreview.acceptsExtension(fileExtension)) {
            return new TextPreview(view, view.getFilePreview().getTextPreviewScroll(),
                    view.getFilePreview().getTextPreview(), file, exceptionHandler);
        } else if (ImagePreview.acceptsExtension(fileExtension)) {
            return new ImagePreview(view,
                    view.getFilePreview().getImagePreview(), file, exceptionHandler);
        }
        return new EmptyPreview(view, view.getFilePreview().getNoPreview());
    }
}
