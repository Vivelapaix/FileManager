package preview;


import javax.swing.JLabel;
import java.awt.CardLayout;

public class EmptyPreview implements Preview {
    
    private final PreviewView view;
    
    private final JLabel noPreviewLabel;
    
    public EmptyPreview(PreviewView view, JLabel noPreviewLabel) {
        this.view = view;
        this.noPreviewLabel = noPreviewLabel;
    }

    public void show() {
        this.view.hidePreviews();
        ((CardLayout)this.view.getPreview().getLayout())
                .show(view.getPreview(), utils.Constants.NO_PREVIEW_LABEL);
        this.noPreviewLabel.setVisible(true);
    }
}