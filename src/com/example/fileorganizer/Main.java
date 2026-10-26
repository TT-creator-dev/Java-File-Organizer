package com.example.fileorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner INPUT = new Scanner(System.in);
    private static final FileScanner FILE_SCANNER = new FileScanner();
    private static final FileOrganizer ORGANIZER = new FileOrganizer();
    private static final OperationLogger LOGGER = new OperationLogger(
            Paths.get("logs", "operations.log"));

    public static void main(String[] args) {
        System.out.println("==================================");
        System.out.println("       Java 文件整理助手");
        System.out.println("==================================");
        System.out.println("提示：第一次运行请使用专门的测试文件夹。");

        while (true) {
            printMenu();
            String choice = INPUT.nextLine().trim();

            switch (choice) {
                case "1":
                    preview();
                    break;
                case "2":
                    organize();
                    break;
                case "3":
                    undoLastBatch();
                    break;
                case "4":
                    System.out.println("感谢使用，再见！");
                    return;
                default:
                    System.out.println("无效选项，请输入 1、2、3 或 4。");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("请选择操作：");
        System.out.println("1. 预览分类结果");
        System.out.println("2. 执行文件整理");
        System.out.println("3. 撤销本次程序运行中最近一次整理");
        System.out.println("4. 退出");
        System.out.print("请输入选项：");
    }

    private static Path askForFolder() {
        System.out.print("请输入要整理的文件夹完整路径：");
        String input = INPUT.nextLine().trim();

        if (input.length() >= 2 &&
                input.startsWith("\"") && input.endsWith("\"")) {
            input = input.substring(1, input.length() - 1);
        }

        if (input.isEmpty()) {
            System.out.println("路径不能为空。");
            return null;
        }

        Path folder = Paths.get(input).toAbsolutePath().normalize();
        if (!Files.exists(folder)) {
            System.out.println("文件夹不存在：" + folder);
            return null;
        }
        if (!Files.isDirectory(folder)) {
            System.out.println("输入的路径不是文件夹：" + folder);
            return null;
        }
        return folder;
    }

    private static void preview() {
        Path folder = askForFolder();
        if (folder == null) {
            return;
        }

        try {
            List<MovePlan> plans = FILE_SCANNER.createPlans(folder);
            printPlans(plans);
            System.out.println("预览完成。没有移动任何文件。");
        } catch (IOException e) {
            System.out.println("扫描失败：" + e.getMessage());
        }
    }

    private static void organize() {
        Path folder = askForFolder();
        if (folder == null) {
            return;
        }

        try {
            List<MovePlan> plans = FILE_SCANNER.createPlans(folder);
            printPlans(plans);

            if (plans.isEmpty()) {
                System.out.println("没有需要整理的文件。");
                return;
            }

            System.out.print("确认执行以上操作吗？输入 Y 确认，其他任意键取消：");
            String answer = INPUT.nextLine().trim();
            if (!"Y".equalsIgnoreCase(answer)) {
                System.out.println("已取消，没有移动文件。");
                return;
            }

            List<MoveRecord> completed = ORGANIZER.execute(plans);
            if (completed.isEmpty()) {
                System.out.println("没有文件成功移动。");
                return;
            }

            LOGGER.writeBatch(completed);
            ORGANIZER.setLastBatch(completed);
            System.out.println("整理结束，成功移动 " + completed.size() + " 个文件。");
            System.out.println("日志位置：" + LOGGER.getLogPath().toAbsolutePath());
            System.out.println("你可以在本次程序运行期间选择菜单 3 撤销这批操作。");
        } catch (IOException e) {
            System.out.println("整理失败：" + e.getMessage());
        }
    }

    private static void undoLastBatch() {
        List<MoveRecord> lastBatch = ORGANIZER.getLastBatch();
        if (lastBatch.isEmpty()) {
            System.out.println("没有可撤销的整理记录。注意：重启程序后，内存中的撤销记录不会保留。");
            return;
        }

        System.out.print("确定撤销最近一次整理吗？输入 Y 确认：");
        if (!"Y".equalsIgnoreCase(INPUT.nextLine().trim())) {
            System.out.println("已取消撤销操作。");
            return;
        }

        int restored = ORGANIZER.undoLastBatch();
        try {
            LOGGER.writeMessage("UNDO", "尝试撤销最近一次整理，成功恢复 " + restored + " 个文件。");
        } catch (IOException e) {
            System.out.println("警告：撤销日志写入失败：" + e.getMessage());
        }
        System.out.println("撤销完成，成功恢复 " + restored + " 个文件。");
        System.out.println("如果原路径已被其他文件占用，该文件会跳过，不会覆盖现有文件。");
    }

    private static void printPlans(List<MovePlan> plans) {
        System.out.println();
        System.out.println("---------- 整理预览 ----------");
        if (plans.isEmpty()) {
            System.out.println("当前文件夹中没有可整理的普通文件。");
            return;
        }
        for (MovePlan plan : plans) {
            System.out.println("原位置：" + plan.getSource());
            System.out.println("新位置：" + plan.getTarget());
            System.out.println("-----------------------------");
        }
        System.out.println("共 " + plans.size() + " 个文件。");
    }
}
