package com.example.fileorganizer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OperationLogger {
    private final Path logPath;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public OperationLogger(Path logPath) {
        this.logPath = logPath;
    }

    public void writeBatch(List<MoveRecord> records) throws IOException {
        Files.createDirectories(logPath.getParent());
        for (MoveRecord record : records) {
            String message = "MOVE | " + record.getOriginalPath()
                    + " -> " + record.getMovedPath();
            appendLine(message);
        }
    }

    public void writeMessage(String action, String message) throws IOException {
        Files.createDirectories(logPath.getParent());
        appendLine(action + " | " + message);
    }

    private void appendLine(String message) throws IOException {
        String line = "[" + LocalDateTime.now().format(FORMATTER) + "] "
                + message + System.lineSeparator();
        Files.write(logPath, line.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public Path getLogPath() {
        return logPath;
    }
}
