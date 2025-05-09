import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

    private TaskRepository taskRepository;
    private Scanner scanner;

    public TaskManager(Scanner scanner) {
        this.taskRepository = new TaskRepository();
        this.scanner = scanner;
    }

    public void printTasks(Boolean isDone, LocalDate dueDate, Priority priority, String orderBy) {
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
            System.out.println("表示対象のタスクがありません。");
            return;
        }

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + ". " + task);
        }
    }

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

        System.out.println("\n優先度 (1: HIGH, 2: MEDIUM, 3: LOW)：");
        int priorityInput = scanner.nextInt();
        Priority priority = Priority.fromLevel(priorityInput);

        taskRepository.insertTask(new Task(title, dueDate, priority));
        System.out.println("\nタスクを追加しました。");

        printTasks(null, null, null, null);
    }

    public void editTask() {
        printTasks(null, null, null, null);
        System.out.println("\n編集するタスクの番号を入力：");
        int index = scanner.nextInt();
        scanner.nextLine(); // 改行消費

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

        printTasks(null, null, null, null);
    }

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

        printTasks(null, null, null, null);
    }

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

        printTasks(null, null, null, null);
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
            case 1 -> printTasks(null, null, null, null); // 全件表示
            case 2 -> printTasks(false, null, null, null); // 未完了のみ
            case 3 -> printTasks(true, null, null, null); // 完了のみ
            case 4 -> orderBy = "dueDate ASC"; // 期限順
            case 5 -> orderBy = "priority ASC"; // 優先度順
            default -> {
                System.out.println("\n無効な選択です。");
                return;
            }
        }

        if (orderBy != null) {
            printTasks(null, null, null, orderBy);
        }
    }
}
