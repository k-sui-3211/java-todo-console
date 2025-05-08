import java.util.List;

public class TaskRepository {

    // タスクの保存（INSERT / UPDATE）

    public void insertTask(Task task) {
        String sql = "INSERT INTO tasks (id, title, isDone) VALUES (?, ?, ?)";
        DatabaseHelper.executeUpdate(sql, task.getId(), task.getTitle(), task.isDone() ? 1 : 0);
    }

    // タスクのタイトルを更新する
    public void updateTaskTitle(int taskId, String newTitle) {
        String sql = "UPDATE tasks SET title = ? WHERE id = ?";
        DatabaseHelper.executeUpdate(sql, newTitle, taskId);
    }

    // タスクの完了状態を更新する
    public void updateTaskStatus(int taskId, int isDone) {
        String sql = "UPDATE tasks SET isDone = ? WHERE id = ?";
        DatabaseHelper.executeUpdate(sql, isDone, taskId);
    }

    // タスクの削除
    public void deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        DatabaseHelper.executeUpdate(sql, taskId);
    }

    // 条件を指定してタスクを取得する
    public List<Object[]> getTasks(String whereClause) {
        String sql = "SELECT id, title, isDone FROM tasks WHERE " + whereClause + " ORDER BY id ASC";
        return DatabaseHelper.executeQuery(sql);
    }
}
