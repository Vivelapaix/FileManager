package filemanager.preview;


import filemanager.exceptions.ExceptionHandler;

import java.io.File;

public class PreviewFactory {
    
    public Preview createPreview(PreviewView view, File file, ExceptionHandler exceptionHandler) {
        if (file.isDirectory()) {
            return new EmptyPreview(view, view.getNoPreview());
        }

        String fileExtension = "";
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex >= 0 && lastDotIndex <= fileName.length() - 2) {
            fileExtension = fileName.substring(lastDotIndex + 1).toLowerCase();
        }

        if (TextPreview.acceptsExtension(fileExtension)) {
            return new TextPreview(view, view.getTextPreviewScroll(),
                    view.getTextPreview(), file, exceptionHandler);
        } else if (ImagePreview.acceptsExtension(fileExtension)) {
            return new ImagePreview(view, view.getImagePreview(), file, exceptionHandler);
        }
        return new EmptyPreview(view, view.getNoPreview());
    }
}