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
        resourceFolder = Paths.get(System.getProperty("user.home") +
                "/" +
                PropertyManager.getInstance().getProjectResourceFolderName());
        if (!Files.exists(resourceFolder)) {
            createResourceFolder();
        }
        if (!Files.exists(resourceFolder.resolve(PropertyManager.getInstance().getDefaultIconsFolder()))) {
            addIconsToResourceFolder();
        }
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            try {
                instance = new ResourceManager();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private void addIconsToResourceFolder() throws IOException {
        System.out.println("add icons");
        iconsFolder = resourceFolder.resolve("icons");
        Files.createDirectory(iconsFolder);
        copyIcons();
    }

    private void copyIcons() throws IOException {
        System.out.println("copy icons");
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("icons").getFile());
        Path path = file.toPath();
        Files.list(path).forEach(f -> {
            System.out.println("copy");
            try {
                Files.copy(f, iconsFolder.resolve(f.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void createResourceFolder() throws IOException {
        System.out.println("create resource folder");
        Files.createDirectory(resourceFolder);
    }

    public List<String> getIconsList() {
        List<String> list = null;
        try {
            list = Files.list(iconsFolder).map(Path::toString).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();

        }
        return list;
    }

    public String getDefaultIcon() {
        return iconsFolder.resolve(PropertyManager.getInstance().getDefaultIcon()).toString();
    }

    private File[] getResourceFolderFiles(String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath();
        return new File(path).listFiles();
    }


    public void close() throws IOException {
        FilesUtills.deleteDirectory(resourceFolder);
    }

}
