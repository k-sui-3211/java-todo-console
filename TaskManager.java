import java.util.ArrayList;
import java.util.List;

//タスクの処理内容を管理するクラス
public class TaskManager {

    private List<Task> tasks = new ArrayList<>();
    private int nextId = 1;
    private TaskRepository taskRepository = new TaskRepository();

    // DBから復元するメソッド
    public void loadFromDatabase() {
        tasks.clear(); // 今のリストを初期化

        List<Object[]> rows = taskRepository.getTasks("1=1");
        for (Object[] row : rows) {
            int id = (int) row[0];
            String title = (String) row[1];
            boolean isDone = (int) row[2] == 1;

            Task task = new Task(id, title);
            if (isDone) {
                task.markDone();
            }
            tasks.add(task);

            if (id >= nextId) {
                nextId = id + 1;
            }

        }
        System.out.println("SQLite復元完了: " + tasks.size() + " 件");

    }

    // タスク一覧を表示するメソッド
    public void printTasks() {
        printTasks(1); // デフォルト＝全表示
    }

    public void printTasks(int mode) {
        String whereClause = switch (mode) {
            case 1 -> "1=1";
            case 2 -> "isDone = 0";
            case 3 -> "isDone = 1";
            default -> {
                System.out.println("無効な選択です。全件を表示します。");
                yield "1=1";
            }
        };

        // ここでモードラベルを設定
        String modeLabel = switch (mode) {
            case 1 -> "すべて表示";
            case 2 -> "未完了のみ";
            case 3 -> "完了のみ";
            default -> "不明";
        };

        System.out.println("\n====タスク一覧(表示モード" + modeLabel + ")====");

        List<Task> tasksToDisplay = getFilteredTasks(whereClause);

        if (tasksToDisplay.isEmpty()) {
            System.out.println("表示対象のタスクがありません。");
            return;
        }

        int i = 1;
        for (Task task : tasksToDisplay) {
            System.out.println(i + ". " + task);
            i++;
        }
    }

    // タスクの追加をするメソッド
    public void addTask(String title) {
        Task task = new Task(nextId++, title);
        tasks.add(task);

        taskRepository.insertTask(task);
        System.out.println("タスクを追加しました。");
    }

    // タスク編集メソッド
    public void editTask(int taskNumber, String newTitle) {
        if (taskNumber >= 1 && taskNumber <= tasks.size()) {
            Task task = tasks.get(taskNumber - 1);
            task.setTitle(newTitle);
            taskRepository.updateTaskTitle(task.getId(), newTitle);

            System.out.println("タスクを編集しました。");

        } else {
            System.out.println("その番号のタスクは存在しません。");
        }
    }

    // 入力された番号のタスクを完了状態にする
    public void completeTask(int taskNumber) {
        System.out.println("完了とするタスクの番号を入力してください。");

        if (taskNumber >= 1 && taskNumber <= tasks.size()) {
            Task task = tasks.get(taskNumber - 1);
            task.markDone();
            taskRepository.updateTaskStatus(task.getId(), 1);

            System.out.println("タスクを完了しました。");
        } else {
            System.out.println("その番号のタスクは存在しません。");
        }
    }

    // タスクを削除するメソッド
    public void deleteTask(int taskNumber) {
        System.out.println("削除するタスク番号を入力してください。");

        if (taskNumber >= 1 && taskNumber <= tasks.size()) {
            Task task = tasks.get(taskNumber - 1);
            tasks.remove(taskNumber - 1);
            taskRepository.deleteTask(task.getId());

            System.out.println("タスクを削除しました。");
        } else {
            System.out.println("その番号のタスクは存在しません！");
        }
    }

    // 表示モードを変更するメソッド
    public List<Task> getFilteredTasks(String whereClause) {
        List<Task> filteredTasks = new ArrayList<>();
        List<Object[]> rows = taskRepository.getTasks(whereClause);

        for (Object[] row : rows) {
            int id = (int) row[0];
            String title = (String) row[1];
            boolean isDone = (int) row[2] == 1;

            Task task = new Task(id, title);
            if (isDone)
                task.markDone();
            filteredTasks.add(task);
        }

        return filteredTasks;
    }

}
