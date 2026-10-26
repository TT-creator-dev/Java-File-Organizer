package com.example.fileorganizer;

import java.nio.file.Path;

public class MoveRecord {
    private final Path originalPath;
    private final Path movedPath;

    public MoveRecord(Path originalPath, Path movedPath) {
        this.originalPath = originalPath;
        this.movedPath = movedPath;
    }

    public Path getOriginalPath() {
        return originalPath;
    }

    public Path getMovedPath() {
        return movedPath;
    }
}
