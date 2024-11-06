package FileInfoGetters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesInfoGetter {
    public String getContent(String path, String filename) throws IOException {
        String content;

        Path contractPath = Path.of(path + filename);
        content = Files.readString(contractPath);

        return content;
    }
}
