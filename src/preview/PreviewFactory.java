package preview;


import java.io.File;

public class PreviewFactory {
    
    public Preview createPreview(PreviewView view, File file) {
        if (file.isDirectory()) {
            return new EmptyPreview(view, view.getNoPreview());
        }
        String entryName = file.getName();
        int lastDotIndex = entryName.lastIndexOf(".");
        String entryExtension = "";
        
        if (lastDotIndex >= 0 && lastDotIndex <= entryName.length() - 2) {
            entryExtension = entryName.substring(lastDotIndex + 1).toLowerCase();
        }

        if (TextPreview.acceptsEntryExtension(entryExtension)) {
            return new TextPreview(view, view.getTextPreviewScroll(), view.getTextPreview(), file);
        } else if (ImagePreview.acceptsEntryExtension(entryExtension)) {
            return new ImagePreview(view, view.getImagePreview(), file);
        }
        return new EmptyPreview(view, view.getNoPreview());
    }
}