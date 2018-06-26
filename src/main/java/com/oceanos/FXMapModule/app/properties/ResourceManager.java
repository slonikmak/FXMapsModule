package com.oceanos.FXMapModule.app.properties;

import com.oceanos.FXMapModule.app.utills.FilesUtills;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor slonikmak on 26.06.2018.
 */
public class ResourceManager {
    private static ResourceManager instance;

    private Path resourceFolder;
    private Path iconsFolder;

    private ResourceManager() throws IOException {
        resourceFolder = Paths.get(System.getProperty("user.home")+
                "/"+
                PropertyManager.getInstance().getProjectResourceFolderName());
        if (!Files.exists(resourceFolder)){
            createResourceFolder();
            addIconsToResourceFolder();
        }
    }

    public static ResourceManager getInstance() throws IOException {
        if (instance == null){
            instance = new ResourceManager();
        }
        return instance;
    }

    private void addIconsToResourceFolder() throws IOException {
        iconsFolder = resourceFolder.resolve("icons");
        Files.createDirectory(iconsFolder);
        copyIcons();
    }

    private void copyIcons() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("icons").getFile());
        Path path = file.toPath();
        Files.list(path).forEach(f->{
            try {
                Files.copy(f, iconsFolder.resolve(f.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void createResourceFolder() throws IOException {
        Files.createDirectory(resourceFolder);
    }

    public List<Path> getIconsList() throws IOException {
       return Files.list(iconsFolder).collect(Collectors.toList());

    }

    private File[] getResourceFolderFiles (String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        System.out.println(url);
        String path = url.getPath();
        return new File(path).listFiles();
    }



    public void close() throws IOException {
        FilesUtills.deleteDirectory(resourceFolder);
    }

}
