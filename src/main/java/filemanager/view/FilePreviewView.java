package filemanager.view;

import filemanager.models.ImagePanel;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import static filemanager.utils.Constants.IMAGE_PREVIEW_LABEL;
import static filemanager.utils.Constants.NO_PREVIEW_LABEL;
import static filemanager.utils.Constants.TEXT_PREVIEW_LABEL;

public class FilePreviewView extends JPanel {

    private JLabel noPreview;
    private JTextArea textPreview;
    private ImagePanel imagePreview;
    private JScrollPane textPreviewScroll;

    FilePreviewView() {
        super(new CardLayout(3, 3));
        setBorder(BorderFactory.createLineBorder(Color.gray));
        Dimension d = getPreferredSize();
        setPreferredSize(new Dimension(200, (int)d.getHeight()));

        initPreviews();
    }

    private void initPreviews() {
        buildNoPreview();
        buildTextPreview();
        buildImagePreview();
    }

    private void buildNoPreview() {
        noPreview = new JLabel();
        noPreview.setHorizontalAlignment(JLabel.CENTER);
        noPreview.setVisible(false);
        add(noPreview, NO_PREVIEW_LABEL);
    }

    private void buildTextPreview() {
        textPreview = new JTextArea();
        textPreview.setWrapStyleWord(true);
        textPreview.setLineWrap(true);
        textPreview.setEditable(false);
        textPreviewScroll = new JScrollPane(textPreview,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        textPreviewScroll.setVisible(false);

        add(textPreviewScroll, TEXT_PREVIEW_LABEL);
    }

    private void buildImagePreview() {
        imagePreview = new ImagePanel();
        imagePreview.setVisible(false);
        add(imagePreview, IMAGE_PREVIEW_LABEL);
    }

    public void hidePreviews() {
        noPreview.setVisible(false);
        textPreviewScroll.setVisible(false);
        imagePreview.setVisible(false);
    }

    public JLabel getNoPreview() {
        return noPreview;
    }

    public JTextArea getTextPreview() {
        return textPreview;
    }

    public ImagePanel getImagePreview() {
        return imagePreview;
    }

    public JScrollPane getTextPreviewScroll() {
        return textPreviewScroll;
    }
}
