package com.oceanos.FXMapModule.app.utills;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @autor slonikmak on 26.06.2018.
 */
public class FilesUtills {
    public static void deleteDirectory(Path dir) throws IOException {
        Files.walk(dir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public static String normalizePath(String path) {
        if (path.startsWith("file:/")) {
            path = path.replace("file:/", "");
        }
        String result = new File(path).toURI().toString();
        return result;
    }

    public static Path saveFile(Path toPath, String content, boolean rename) {
        try {
            if (rename && Files.exists(toPath)) {
                //String fileName = toPath.getFileName().toString().split("\\.")[0];
                String[] parts = toPath.toString().split("\\.");
                parts[0] = parts[0]+"1";
                toPath = Paths.get(parts[0]+"."+parts[1]);
            }
            Files.write(toPath, content.getBytes(Charset.forName("UTF-8")), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toPath;
    }

    public static String openFile(Path toPath) {
        StringBuilder builder = new StringBuilder();
        try {
            Files.lines(toPath, Charset.forName("UTF-8")).forEach(builder::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static List<String> getFilenamesForDirnameFromCP(String directoryName) throws URISyntaxException, UnsupportedEncodingException, IOException {
        List<String> filenames = new ArrayList<String>();

        URL url = Thread.currentThread().getContextClassLoader().getResource(directoryName);
        if (url != null) {
            if (url.getProtocol().equals("file")) {
                File file = Paths.get(url.toURI()).toFile();
                if (file != null) {
                    File[] files = file.listFiles();
                    if (files != null) {
                        for (File filename: files) {
                            filenames.add(filename.toString());
                        }
                    }
                }
            } else if (url.getProtocol().equals("jar")) {
                String dirname = directoryName + "/";
                String path = url.getPath();
                String jarPath = path.substring(5, path.indexOf("!"));
                try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name()))) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith(dirname) && !dirname.equals(name)) {
                            URL resource = Thread.currentThread().getContextClassLoader().getResource(name);
                            filenames.add(resource.toString());
                        }
                    }
                }
            }
        }
        return filenames;
    }

    public static List<Path> copyFiles(List<String> files, Path directoryTo) throws IOException {
        List<Path> result = new ArrayList<>();
        if (!Files.exists(directoryTo)) Files.createDirectory(directoryTo);
        if (files.get(0).startsWith("jar:")) {
            files.forEach(f -> {
                String fileName = f.split("!")[1];
                Path newFile = directoryTo.resolve(Paths.get(fileName).getFileName());
                try {
                    Files.copy(FilesUtills.class.getResourceAsStream(fileName), newFile, StandardCopyOption.REPLACE_EXISTING);
                    result.add(newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            files.forEach(f -> {
                Path filename = Paths.get(f).getFileName();
                Path to = directoryTo.resolve(filename);
                try {
                    Files.copy(Paths.get(f), to, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result.add(to);
            });
        }
        return result;
    }
}
