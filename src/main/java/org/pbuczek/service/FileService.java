package org.pbuczek.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pbuczek.exception.DuplicateIdException;
import org.pbuczek.post.Post;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileService {
    private DataService dataService = new DataService(new ObjectMapper());
    public static final String JSON_PLACEHOLDER_POSTS_URL_ADDRESS = "https://jsonplaceholder.typicode.com/posts";

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void createDirectory(String folderPath) throws IOException {
        try {
            Path path = Paths.get(folderPath);
            Files.createDirectories(path);

        } catch (IOException e) {
            throw new IOException("Failed to create directory:" + folderPath + e.getMessage());
        }
    }

    public String downloadPostsToJsonFiles() {
        String folderPath = "";
        try {
            String jsonPosts = dataService.getJsonFromUrlAddress(JSON_PLACEHOLDER_POSTS_URL_ADDRESS);
            List<Post> posts = dataService.mapJsonToPosts(jsonPosts);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM(MMM)_dd_HH_mm_ss");
            folderPath = "results/posts_" + formatter.format(java.time.LocalDateTime.now());
            createDirectory(folderPath);

            dataService.savePostsToFiles(posts, folderPath);
        } catch (DuplicateIdException | IOException e) {
            //probably would be better to log with @SLF4J or something
            e.printStackTrace();
        }

        return folderPath;
    }
}
