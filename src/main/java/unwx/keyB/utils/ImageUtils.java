package unwx.keyB.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static BufferedImage getImageFromMultipartFile(MultipartFile file, String extension) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        File outputFile = new File("saved." + extension);
        ImageIO.write(bufferedImage, extension, outputFile);
        return ImageIO.read(new File("saved." + extension));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteSavedTempFile(String extension) {
        new File("saved." + extension).delete();
    }
}
