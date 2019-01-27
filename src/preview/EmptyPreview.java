package preview;


import sun.security.pkcs11.wrapper.Constants;

import javax.swing.JLabel;
import java.awt.*;

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
                .show(view.getPreview(), Constant.Constants.NO_PREVIEW_LABEL);
        this.noPreviewLabel.setVisible(true);
    }
}