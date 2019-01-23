package preview;


import javax.swing.*;

public class EmptyPreview implements Preview {
    
    private final PreviewView view;
    
    private final JLabel noPreviewLabel;
    
    public EmptyPreview(PreviewView view, JLabel noPreviewLabel) {
        this.view = view;
        this.noPreviewLabel = noPreviewLabel;
    }

    public void show() {
        this.view.hidePreviews();
        this.noPreviewLabel.setVisible(true);
    }
}