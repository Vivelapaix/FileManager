package preview;

import preview.ImagePanel;

import javax.swing.*;

public interface PreviewView {

    JPanel getPreview();
    
    JLabel getNoPreview();
    
    JScrollPane getTextPreviewScroll();
    
    JTextArea getTextPreview();

    ImagePanel getImagePreview();
    
    void hidePreviews();
}
