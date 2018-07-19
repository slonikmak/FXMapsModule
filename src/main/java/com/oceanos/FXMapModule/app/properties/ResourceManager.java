package com.oceanos.FXMapModule.app.properties;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.app.utills.FilesUtills;
import com.oceanos.FXMapModule.layers.TileLayer;
import com.oceanos.FXMapModule.layers.WMSTileLayer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @autor slonikmak on 26.06.2018.
 */
public class ResourceManager {
    private static ResourceManager instance;

    private Path resourceFolder;
    private Path iconsFolder;
    private Path defaultStylesFolder;
    private Path layersFolder;
    private Path layersTileFolder;
    private Path layersWmsFolder;
    private Path layersWmsFile;
    private Path layersTileFile;

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
        if (!Files.exists(resourceFolder.resolve(PropertyManager.getInstance().getDefaultStylesFolder()))) {
            createDefaultStilesFolder();
        } if (!Files.exists(resourceFolder.resolve(PropertyManager.getInstance().getLayersFolder()))) {
            createLayersFolder();
        } if (!Files.exists(resourceFolder.resolve(PropertyManager.getInstance().getLayersTileFolder()))) {
            createLayersTileFolder();
        } if (!Files.exists(resourceFolder.resolve(PropertyManager.getInstance().getLayersWmsFolder()))) {
            createLayersWmsFolder();
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

    private void createDefaultStilesFolder() throws IOException {
        defaultStylesFolder =  resourceFolder.resolve(PropertyManager.getInstance().getDefaultStylesFolder());
        Files.createDirectory(defaultStylesFolder);
        copyStyles();
    }

    private void createLayersFolder() throws IOException {
        layersFolder = resourceFolder.resolve(PropertyManager.getInstance().getLayersFolder());
        Files.createDirectory(layersFolder);
    }

    private void createLayersTileFolder() throws IOException {
        layersTileFolder = resourceFolder.resolve(PropertyManager.getInstance().getLayersTileFolder());
        Files.createDirectory(layersTileFolder);
        copyTiles();
    }

    private void copyTiles() throws IOException {
        try {
            FilesUtills.copyFiles(FilesUtills.getFilenamesForDirnameFromCP(PropertyManager.getInstance().getLayersTileFolder()), layersTileFolder);
            layersTileFile = layersTileFolder.resolve(PropertyManager.getInstance().getLayersTilesFile());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void createLayersWmsFolder() throws IOException {
        layersWmsFolder = resourceFolder.resolve(PropertyManager.getInstance().getLayersWmsFolder());
        Files.createDirectory(layersWmsFolder);
        copyWms();
    }

    private void copyWms() throws IOException {
        try {
            FilesUtills.copyFiles(FilesUtills.getFilenamesForDirnameFromCP(PropertyManager.getInstance().getLayersWmsFolder()), layersWmsFolder);
            layersWmsFile = layersWmsFolder.resolve(PropertyManager.getInstance().getLayersWmsFile());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void copyStyles() throws IOException {
        try {
            FilesUtills.copyFiles(FilesUtills.getFilenamesForDirnameFromCP(PropertyManager.getInstance().getDefaultStylesFolder()), defaultStylesFolder);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void copyIcons() throws IOException {
        System.out.println("copy icons");

        try {
            FilesUtills.copyFiles(FilesUtills.getFilenamesForDirnameFromCP("icons"), iconsFolder);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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

    public String getDefaultMissionOptions() {
        String missionFile = PropertyManager.getInstance().getMissionStylesFile();
        Path missionOptions = defaultStylesFolder.resolve(missionFile);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Files.lines(missionOptions).forEach(stringBuilder::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public String getDefaultWaypointOptions() {
        String waypointFile = PropertyManager.getInstance().getWaypointStylesFile();
        Path waypointOptyons = defaultStylesFolder.resolve(waypointFile);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Files.lines(waypointOptyons).forEach(stringBuilder::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public Optional<String> getIconPath(String iconName){
        return getIconsList().stream().filter(i->Paths.get(i).getFileName().toString().equals(iconName)).findFirst();
    }

    public List<TileLayer> getTileLayers(){
        List<TileLayer> list = new ArrayList<>();
        try {
            Files.lines(layersTileFile).reduce((s1, s2) -> s1 + s2).ifPresent(s -> {
                JsonParser parser = new JsonParser();
                JsonArray arr = parser.parse(s).getAsJsonArray();
                arr.forEach(o -> list.add(TileLayer.getFromJson(o.toString())));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<WMSTileLayer> getWmsLayers(){
        List<WMSTileLayer> list = new ArrayList<>();
        try {
            Files.lines(layersWmsFile).reduce((s1, s2) -> s1 + s2).ifPresent(s -> {
                JsonParser parser = new JsonParser();
                JsonArray arr = parser.parse(s).getAsJsonArray();
                arr.forEach(o -> list.add(WMSTileLayer.getFromJson(o.toString())));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Path getIconsFolder() {
        return iconsFolder;
    }

    public Path getLayersWmsFile() {
        return layersWmsFile;
    }

    public Path getLayersTileFile() {
        return layersTileFile;
    }
}
