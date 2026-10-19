package com.example.fileorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class FileScanner {
    public List<MovePlan> createPlans(Path folder) throws IOException {
        List<MovePlan> plans = new ArrayList<>();

        try (Stream<Path> paths = Files.list(folder)) {
            paths.filter(Files::isRegularFile)
                    .sorted(Comparator.comparing(path ->
                            path.getFileName().toString().toLowerCase()))
                    .forEach(source -> {
                        String category = FileClassifier.classify(source);
                        Path targetDirectory = folder.resolve(category);
                        Path target = targetDirectory.resolve(source.getFileName());
                        plans.add(new MovePlan(source, target));
                    });
        }

        return plans;
    }
}
