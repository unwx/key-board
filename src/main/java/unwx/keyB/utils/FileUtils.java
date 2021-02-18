package unwx.keyB.utils;

import org.springframework.web.multipart.MultipartFile;
import unwx.keyB.exceptions.rest.exceptions.BadRequestException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    public static String getFileExtension(MultipartFile file) {
        String fileName = file.getContentType();
        if (fileName != null && fileName.lastIndexOf("/") != -1 && fileName.lastIndexOf("/") != 0)
            return fileName.substring(fileName.lastIndexOf("/") + 1);
        else return "";
    }

    public static int[] loadFileAsResource(String fullPath) throws IOException {
        if (new File(fullPath).exists()) {
            return convertBytesToInts(Files.readAllBytes(Path.of(fullPath)));
        } else throw new BadRequestException("this file doesn't exist.");
    }

    private static int[] convertBytesToInts(byte[] bytes) {
        int[] arr = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            arr[i] = bytes[i];
        }
        return arr;
    }


}
