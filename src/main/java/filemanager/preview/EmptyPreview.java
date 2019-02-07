package filemanager.preview;


import filemanager.view.FileManagerView;

import javax.swing.JLabel;
import java.awt.CardLayout;

import static filemanager.utils.Constants.NO_PREVIEW_AVAILABLE_LABEL;
import static filemanager.utils.Constants.NO_PREVIEW_LABEL;


public class EmptyPreview implements Preview {
    
    private final FileManagerView view;
    
    private final JLabel noPreviewLabel;
    
    EmptyPreview(FileManagerView view, JLabel noPreviewLabel) {
        this.view = view;
        this.noPreviewLabel = noPreviewLabel;
    }

    public void show() {
        this.view.getFilePreview().hidePreviews();
        ((CardLayout)this.view.getFilePreview().getLayout())
                .show(view.getFilePreview(), NO_PREVIEW_LABEL);
        this.noPreviewLabel.setText(NO_PREVIEW_AVAILABLE_LABEL);
        this.noPreviewLabel.setVisible(true);
    }
}
