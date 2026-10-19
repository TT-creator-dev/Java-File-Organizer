package com.example.fileorganizer;

import java.nio.file.Path;

public class MovePlan {
    private final Path source;
    private final Path target;

    public MovePlan(Path source, Path target) {
        this.source = source;
        this.target = target;
    }

    public Path getSource() {
        return source;
    }

    public Path getTarget() {
        return target;
    }
}
