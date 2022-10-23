

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

@Deprecated
@SuppressWarnings("serial")
public class ImageCanvas extends Canvas {

    private BufferedImage img;

    public ImageCanvas() {
        try {
            img = ImageIO.read(new File("test.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return img == null ? new Dimension(200, 200) : new Dimension(img.getWidth(), img.getHeight());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (img != null) {
            int x = (getWidth() - img.getWidth()) / 2;
            int y = (getHeight() - img.getHeight()) / 2;
            g.drawImage(img, x, y, this);
        }
    }

}
