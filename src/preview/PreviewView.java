package preview;


import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public interface PreviewView {

    JPanel getPreview();
    
    JLabel getNoPreview();
    
    JScrollPane getTextPreviewScroll();
    
    JTextArea getTextPreview();

    ImagePanel getImagePreview();
    
    void hidePreviews();
}
