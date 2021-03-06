package filemanager.models;

import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


/**
 * ImagePanel is the class which is intended for image rendering.
 *
 * @see filemanager.preview.ImagePreview#panel
 */
public class ImagePanel extends JLabel {

    private BufferedImage image;

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void paintComponent(Graphics g) {
        int imgWidth = image == null ? 0 : image.getWidth(null);
        int imgHeight = image == null ? 0 : image.getHeight(null);
        int labelWidth = getWidth();
        int labelHeight = getHeight();

        int x1 = 0;
        int y1 = 0;
        int x2;
        int y2;

        if (imgWidth < labelWidth && imgHeight < labelHeight) {
            x1 = (labelWidth - imgWidth)  / 2;
            y1 = (labelHeight - imgHeight) / 2;
            x2 = x1 + imgWidth;
            y2 = y1 + imgHeight;

        } else {
            double imgAspect = (double) imgHeight / imgWidth;
            double labelAspect = (double) labelHeight / labelWidth;

            if (labelAspect > imgAspect) {
                y1 = labelHeight;
                labelHeight = (int)(labelWidth * imgAspect);
                y1 = (y1 - labelHeight) / 2;
            } else {
                x1 = labelWidth;
                labelWidth = (int) (labelHeight / imgAspect);
                x1 = (x1 - labelWidth) / 2;
            }
            x2 = x1 + labelWidth;
            y2 = y1 + labelHeight;
        }

        g.drawImage(image, x1, y1, x2, y2, 0, 0, imgWidth, imgHeight, null);
    }
}
