package com.example.fileorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileOrganizer {
    private List<MoveRecord> lastBatch = new ArrayList<>();

    public List<MoveRecord> execute(List<MovePlan> plans) {
        List<MoveRecord> completed = new ArrayList<>();
        System.out.println("DEBUG：已进入文件移动方法");
        for (MovePlan plan : plans) {
            Path source = plan.getSource();
            if (!Files.exists(source) || !Files.isRegularFile(source)) {
                System.out.println("跳过：源文件不存在或不再是普通文件：" + source);
                continue;
            }

            try {
                Path targetDirectory = plan.getTarget().getParent();
                Files.createDirectories(targetDirectory);

                Path target = chooseAvailableTarget(plan.getTarget());
                Files.move(source, target);
                completed.add(new MoveRecord(source, target));
                System.out.println("已移动：" + source.getFileName() + " -> " + target);
            } catch (IOException e) {
                System.out.println("移动失败：" + source + "；原因：" + e.getMessage());
            }
        }

        return completed;
    }

    private Path chooseAvailableTarget(Path requestedTarget) {
        if (!Files.exists(requestedTarget)) {
            return requestedTarget;
        }

        String fileName = requestedTarget.getFileName().toString();
        String baseName = fileName;
        String extension = "";

        int dot = fileName.lastIndexOf('.');
        if (dot > 0) {
            baseName = fileName.substring(0, dot);
            extension = fileName.substring(dot);
        }

        int number = 1;
        Path candidate;
        do {
            candidate = requestedTarget.resolveSibling(
                    baseName + " (" + number + ")" + extension);
            number++;
        } while (Files.exists(candidate));

        return candidate;
    }

    public void setLastBatch(List<MoveRecord> records) {
        lastBatch = new ArrayList<>(records);
    }

    public List<MoveRecord> getLastBatch() {
        return Collections.unmodifiableList(lastBatch);
    }

    public int undoLastBatch() {
        int restored = 0;

        for (int i = lastBatch.size() - 1; i >= 0; i--) {
            MoveRecord record = lastBatch.get(i);
            Path original = record.getOriginalPath();
            Path moved = record.getMovedPath();

            if (!Files.exists(moved)) {
                System.out.println("跳过：整理后的文件不存在：" + moved);
                continue;
            }
            if (Files.exists(original)) {
                System.out.println("跳过：原路径已被占用，为避免覆盖：" + original);
                continue;
            }

            try {
                Files.createDirectories(original.getParent());
                Files.move(moved, original, StandardCopyOption.ATOMIC_MOVE);
                restored++;
            } catch (IOException atomicMoveFailure) {
                try {
                    Files.move(moved, original);
                    restored++;
                } catch (IOException e) {
                    System.out.println("恢复失败：" + moved + "；原因：" + e.getMessage());
                }
            }
        }

        lastBatch = new ArrayList<>();
        return restored;
    }
}
