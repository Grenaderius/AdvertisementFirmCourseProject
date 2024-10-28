package ImageChangers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSizeChanger {
    public BufferedImage changeSize(File file, int wight, int height) throws IOException {
        BufferedImage image = ImageIO.read(file);
        BufferedImage imgWithChangedSize = new BufferedImage(wight, height, image.getType());

        Graphics2D g2d = imgWithChangedSize.createGraphics();
        g2d.drawImage(image, 0, 0, wight, height, null);
        g2d.dispose();

        return imgWithChangedSize;
    }
}
