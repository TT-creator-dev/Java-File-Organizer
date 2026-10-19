package com.example.fileorganizer;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class FileClassifier {
    private static final Set<String> IMAGES = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "heic"));
    private static final Set<String> DOCUMENTS = new HashSet<>(Arrays.asList(
            "pdf", "doc", "docx", "txt", "rtf", "odt", "xls", "xlsx",
            "ppt", "pptx", "csv"));
    private static final Set<String> VIDEOS = new HashSet<>(Arrays.asList(
            "mp4", "mkv", "mov", "avi", "wmv", "flv", "webm", "m4v"));
    private static final Set<String> AUDIOS = new HashSet<>(Arrays.asList(
            "mp3", "wav", "aac", "flac", "ogg", "m4a", "wma"));
    private static final Set<String> ARCHIVES = new HashSet<>(Arrays.asList(
            "zip", "rar", "7z", "tar", "gz", "bz2"));
    private static final Set<String> PROGRAMS = new HashSet<>(Arrays.asList(
            "java", "class", "jar", "py", "js", "ts", "html", "css",
            "json", "xml", "sql", "c", "cpp", "h", "cs"));

    private FileClassifier() {
    }

    public static String classify(Path file) {
        String name = file.getFileName().toString();
        int dot = name.lastIndexOf('.');
        if (dot <= 0 || dot == name.length() - 1) {
            return "其他";
        }

        String extension = name.substring(dot + 1).toLowerCase(Locale.ROOT);
        if (IMAGES.contains(extension)) return "图片";
        if (DOCUMENTS.contains(extension)) return "文档";
        if (VIDEOS.contains(extension)) return "视频";
        if (AUDIOS.contains(extension)) return "音频";
        if (ARCHIVES.contains(extension)) return "压缩包";
        if (PROGRAMS.contains(extension)) return "代码文件";
        return "其他";
    }
}
