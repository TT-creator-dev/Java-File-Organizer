# Java 文件整理助手（Java File Organizer）

一个使用 Java 标准库实现的命令行文件整理工具，无需第三方依赖。

## 功能

- 扫描指定文件夹中的第一层普通文件（暂不递归扫描子文件夹）
- 按扩展名分类：图片、文档、视频、音频、压缩包、代码文件、其他
- 整理前预览，确认后才移动文件
- 目标文件重名时自动添加 `(1)`、`(2)` 等后缀，避免覆盖
- 将移动操作追加写入 `logs/operations.log`
- 在当前程序运行期间撤销最近一次整理

## 环境要求

- JDK 8 或更高版本
- IntelliJ IDEA（推荐）

## 在 IntelliJ IDEA 中运行

1. 解压项目压缩包。
2. 在 IDEA 中选择 **File → Open**，打开 `Java-File-Organizer` 文件夹。
3. 确认项目 SDK 已设置为已安装的 JDK。
4. 在 `src/com/example/fileorganizer/Main.java` 中点击运行按钮，或右键选择 **Run 'Main.main()'**。
5. 在控制台输入要整理的文件夹路径，例如 `D:\FileOrganizerTest`。
6. 选择菜单 `1` 先预览；确认无误后再选择 `2` 执行整理。

如果 IDEA 没有自动识别 `src`，可以右键 `src` 文件夹，选择 **Mark Directory as → Sources Root**。

## 建议的安全测试步骤

1. 在桌面新建一个文件夹，例如 `FileOrganizerTest`。
2. 复制几份测试文件进去，例如 `.jpg`、`.pdf`、`.mp4`、`.zip` 和 `.unknown` 文件。
3. 先运行预览功能，检查分类路径。
4. 执行整理，确认文件被移动到对应子文件夹。
5. 再次整理空的根目录，确认不会重复处理子文件夹中的内容。
6. 选择菜单 `3` 撤销最近一次整理，检查文件是否回到原位置。

**不要首次测试就对桌面、系统目录或包含重要资料的目录执行整理。** 本程序不会覆盖目标位置的同名文件，但整理仍会改变文件位置。

## 当前限制

- 只扫描目标文件夹的第一层，不递归处理子目录。
- 撤销记录只保存在当前程序运行内存中；退出或重启后不能通过菜单恢复上一批操作。
- 文件分类依赖扩展名，并不检测文件的真实内容。
- 文件权限、被占用文件或磁盘错误可能导致个别文件移动失败。

## 项目结构

```text
Java-File-Organizer/
├── src/
│   └── com/example/fileorganizer/
│       ├── Main.java
│       ├── FileScanner.java
│       ├── FileClassifier.java
│       ├── FileOrganizer.java
│       ├── MovePlan.java
│       ├── MoveRecord.java
│       └── OperationLogger.java
├── logs/                 # 运行后自动生成
├── README.md
├── LICENSE
└── .gitignore
```

## 开源与贡献

欢迎通过 GitHub Issues 报告问题或提出改进建议。提交 Issue 时，请说明操作步骤、预期结果、实际结果以及 Java/JDK 版本。

## 版本

当前为开发初始版本，建议在本地测试通过后再创建 `v1.0.0` Release。
