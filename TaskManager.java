import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

//タスク管理（タスクの追加・編集・完了・削除、ソート機能）を行うクラス
public class TaskManager {

    private TaskRepository taskRepository;
    private Scanner scanner;

    // コンストラクタ
    public TaskManager(Scanner scanner) {
        this.taskRepository = new TaskRepository();
        this.scanner = scanner;
    }

    // タスク追加
    public void addTask() {
        System.out.println("\nタスクの内容を入力（必須）：");
        String title = scanner.nextLine();
        if (title.isEmpty()) {
            System.out.println("\nタイトルは必須です。");
            return;
        }

        LocalDate dueDate = null;
        while (true) {
            System.out.println("\n期限 (yyyy-MM-dd) を入力（省略可）：");
            String dateInput = scanner.nextLine();
            if (dateInput.isEmpty())
                break;

            try {
                dueDate = LocalDate.parse(dateInput);
                break;
            } catch (Exception e) {
                System.out.println("\n不正な日付形式です。再入力してください。");
            }
        }

        Priority priority = Priority.LOW; // デフォルトは LOW

        System.out.println("\n優先度 (1: HIGH, 2: MEDIUM, 3: LOW)：");
        try {
            int priorityLevel = scanner.nextInt();
            priority = Priority.fromLevel(priorityLevel);
        } catch (InputMismatchException e) {
            System.out.println("\n無効な入力です。LOW に設定されます。");
        } finally {
            scanner.nextLine(); // 改行文字を消費しておく（次の入力を受け取るため）
        }

        taskRepository.insertTask(new Task(title, dueDate, priority));
        System.out.println("\nタスクを追加しました。");

        printTasks(null, null, null, null, false);
    }

    // タスク表示
    public void printTasks(Boolean isDone, LocalDate dueDate, Priority priority, String orderBy,
            boolean confirmDelete) {
        List<Task> tasks = taskRepository.getSortedTasks(isDone, dueDate, priority, orderBy);

        StringBuilder outputLabel = new StringBuilder("\n==== タスク一覧（");

        if (isDone == null) {
            outputLabel.append("全件表示");
        } else if (isDone) {
            outputLabel.append("完了のみ");
        } else {
            outputLabel.append("未完了のみ");
        }

        if (orderBy != null) {
            outputLabel.append(" / ").append(orderBy.replace(" ASC", " 昇順").replace(" DESC", " 降順"));
        }

        outputLabel.append("） ====");
        System.out.println(outputLabel);

        if (tasks.isEmpty()) {
            System.out.println("\n表示対象のタスクがありません。");
            return;
        }

        System.out.printf("%-3s %-6s %-15s %-12s %-10s\n", "No.", "状態", "内容", "期限", "優先度");
        System.out.println("--------------------------------------------------------------");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (confirmDelete) {
                System.out.println("- " + task); // 削除確認モード時の簡潔表示
            } else {
                System.out.printf("%-3d %s\n", (i + 1), task);
            }
        }
    }

    // タスク編集
    public void editTask() {
        printTasks(null, null, null, null, false);
        System.out.println("\n編集するタスクの番号を入力：");
        int index = scanner.nextInt();

        List<Task> tasks = taskRepository.getTasks();
        if (index < 1 || index > tasks.size()) {
            System.out.println("\n無効な番号です。");
            return;
        }

        Task task = tasks.get(index - 1);

        System.out.println("\n1. タイトルを編集");
        System.out.println("2. 期限を編集");
        System.out.println("3. 優先度を編集");
        System.out.println("4. すべてを編集");
        System.out.println("0. キャンセル");
        System.out.print("編集する項目を選択：");

        int choice = scanner.nextInt();
        scanner.nextLine(); // 改行消費

        switch (choice) {
            case 1 -> editTitle(task);
            case 2 -> editDueDate(task);
            case 3 -> editPriority(task);
            case 4 -> {
                editTitle(task);
                editDueDate(task);
                editPriority(task);
            }
            case 0 -> System.out.println("\n編集をキャンセルしました。");
            default -> System.out.println("\n無効な選択です。");
        }

        printTasks(null, null, null, null, false);
    }

    // タスク内容更新
    private void editTitle(Task task) {
        System.out.println("\n新しいタイトル（Enterでスキップ）：");
        String newTitle = scanner.nextLine();
        if (!newTitle.isEmpty()) {
            taskRepository.updateTaskTitle(task.getId(), newTitle);
            System.out.println("\nタイトルを更新しました。");
        } else {
            System.out.println("\nタイトルは変更されませんでした。");
        }
    }

    // 期限更新
    private void editDueDate(Task task) {
        while (true) {
            System.out.println("\n新しい期限 (yyyy-MM-dd)（Enterでスキップ）：");
            String dateInput = scanner.nextLine();
            if (dateInput.isEmpty()) {
                System.out.println("\n期限は変更されませんでした。");
                break;
            }

            try {
                LocalDate newDueDate = LocalDate.parse(dateInput);
                taskRepository.updateTaskDueDate(task.getId(), newDueDate);
                System.out.println("\n期限を更新しました。");
                break;
            } catch (Exception e) {
                System.out.println("\n不正な日付形式です。再入力してください。");
            }
        }
    }

    // 優先度更新
    private void editPriority(Task task) {
        System.out.println("\n新しい優先度 (1: HIGH, 2: MEDIUM, 3: LOW)（Enterでスキップ）：");
        String priorityInput = scanner.nextLine();
        if (!priorityInput.isEmpty()) {
            try {
                int priorityLevel = Integer.parseInt(priorityInput);
                Priority newPriority = Priority.fromLevel(priorityLevel);
                taskRepository.updateTaskPriority(task.getId(), newPriority);
                System.out.println("\n優先度を更新しました。");
            } catch (NumberFormatException e) {
                System.out.println("\n無効な入力です。優先度は変更されませんでした。");
            }
        } else {
            System.out.println("\n優先度は変更されませんでした。");
        }
    }

    // タスク完了
    public void completeTask() {
        List<Task> tasks = taskRepository.getTasks();

        if (tasks.isEmpty()) {
            System.out.println("\nタスクがありません。");
            return;
        }

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).toString());
        }

        System.out.println("\n完了にするタスクの番号を入力：");
        int index = scanner.nextInt();
        scanner.nextLine(); // 改行消費

        if (index < 1 || index > tasks.size()) {
            System.out.println("\n無効な番号です。");
            return;
        }

        Task task = tasks.get(index - 1);
        taskRepository.updateTaskStatus(task.getId(), true);
        System.out.println("\nタスクを完了しました。");

        printTasks(null, null, null, null, false);
    }

    // タスク削除
    public void deleteTask() {
        List<Task> tasks = taskRepository.getTasks();

        if (tasks.isEmpty()) {
            System.out.println("\n削除するタスクがありません。");
            return;
        }

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).toString());
        }

        System.out.println("\n削除するタスクの番号を入力：");
        int index = scanner.nextInt();
        scanner.nextLine(); // 改行消費

        if (index < 1 || index > tasks.size()) {
            System.out.println("\n無効な番号です。");
            return;
        }

        Task task = tasks.get(index - 1);
        taskRepository.deleteTask(task.getId());
        System.out.println("\nタスクを削除しました。");

        printTasks(null, null, null, null, false);
    }

    // 完了済タスクの一括削除
    public void deleteCompletedTasks() {
        List<Task> completedTasks = taskRepository.getSortedTasks(true, null, null, null);

        if (completedTasks.isEmpty()) {
            System.out.println("\n完了済のタスクはありません。");
            return;
        }

        System.out.println("\n完了済タスクが " + completedTasks.size() + " 件あります。削除してもよろしいですか？ (Y/N)");

        // 完了済みタスク一覧出力
        printTasks(true, null, null, null, true);

        String input = scanner.nextLine().trim().toUpperCase();

        if (input.equals("Y")) {
            taskRepository.deleteCompletedTasks();
            System.out.println("\n完了済タスクを削除しました。");
        } else {
            System.out.println("\n削除をキャンセルしました。");
        }
        // 通常表示モードで再出力
        printTasks(null, null, null, null, false);
    }

    // 並び替えメニュー
    public void sortTasks() {
        System.out.println("""

                1. 全て表示
                2. 未完了のみ表示
                3. 完了のみ表示
                4. 期限順（昇順）
                5. 優先度順（HIGH → LOW）
                """);

        int choice = scanner.nextInt();
        scanner.nextLine();

        String orderBy = null;

        switch (choice) {
            case 1 -> printTasks(null, null, null, null, false); // 全件表示
            case 2 -> printTasks(false, null, null, null, false); // 未完了のみ
            case 3 -> printTasks(true, null, null, null, false); // 完了のみ
            case 4 -> orderBy = "dueDate ASC"; // 期限順
            case 5 -> orderBy = "priority ASC"; // 優先度順
            default -> {
                System.out.println("\n無効な選択です。");
                return;
            }
        }

        if (orderBy != null) {
            printTasks(null, null, null, orderBy, false);
        }
    }

    // データベースのバックアップ作成
    public void backupData() {
        System.out.println("\nバックアップファイル名を入力してください（例：backup_20250510.db）：");
        String backupFileName = scanner.nextLine().trim();

        if (backupFileName.isEmpty()) {
            System.out.println("\nファイル名が空です。");
            return;
        }

        DatabaseHelper.backupDatabase(backupFileName);
        printTasks(null, null, null, null, false);

    }

    // データベースのリストア
    public void restoreData() {
        System.out.println("\nリストアするバックアップファイル名を入力してください：");
        String backupFileName = scanner.nextLine().trim();

        if (backupFileName.isEmpty()) {
            System.out.println("\nファイル名が空です。");
            return;
        }

        System.out.println("\n現在のデータが上書きされます。リストアしますか？ (Y/N)");
        String confirmation = scanner.nextLine().trim().toUpperCase();

        if (confirmation.equals("Y")) {
            DatabaseHelper.restoreDatabase(backupFileName);
        } else {
            System.out.println("\nリストアをキャンセルしました。");
        }
        printTasks(null, null, null, null, false);
    }

}
