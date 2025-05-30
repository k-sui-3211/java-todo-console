import java.time.LocalDate;
import java.util.List;

//DBアクセス専用クラス（CRUD処理）
public class TaskRepository {

    // タスクの保存（INSERT / UPDATE）
    public void insertTask(Task task) {
        String sql = "INSERT INTO tasks (title, dueDate, priority, isDone) VALUES (?, ?, ?, 0)";
        DatabaseHelper.executeUpdate(sql, task.getTitle(), task.getDueDate(), task.getPriority().getLevel());
    }

    // 全タスクを取得する
    public List<Task> getTasks() {
        String sql = "SELECT id, title, isDone, dueDate, priority FROM tasks ORDER BY id ASC";
        return DatabaseHelper.executeQuery(sql);
    }

    // 条件付きでタスクを取得する
    public List<Task> getSortedTasks(Boolean isDone, LocalDate dueDate, Priority priority, String orderBy) {
        StringBuilder sql = new StringBuilder("SELECT id, title, isDone, dueDate, priority FROM tasks");
        boolean hasCondition = false;

        if (isDone != null) {
            sql.append(" WHERE isDone = ").append(isDone ? 1 : 0);
            hasCondition = true;
        }

        if (dueDate != null) {
            sql.append(hasCondition ? " AND" : " WHERE").append(" dueDate = '").append(dueDate).append("'");
            hasCondition = true;
        }

        if (priority != null) {
            sql.append(hasCondition ? " AND" : " WHERE").append(" priority = ").append(priority.getLevel());
        }

        if (orderBy != null) {
            sql.append(" ORDER BY ").append(orderBy);
        } else {
            sql.append(" ORDER BY id ASC");
        }

        return DatabaseHelper.executeQuery(sql.toString());
    }

    // タスクのタイトルを更新する
    public void updateTaskTitle(int taskId, String newTitle) {
        String sql = "UPDATE tasks SET title = ? WHERE id = ?";
        DatabaseHelper.executeUpdate(sql, newTitle, taskId);
    }

    // タスクの期限を更新する
    public void updateTaskDueDate(int taskId, LocalDate newDueDate) {
        String sql = "UPDATE tasks SET dueDate = ? WHERE id = ?";
        DatabaseHelper.executeUpdate(sql, newDueDate != null ? newDueDate.toString() : null, taskId);
    }

    // タスクの優先度を更新する
    public void updateTaskPriority(int taskId, Priority newPriority) {
        String sql = "UPDATE tasks SET priority = ? WHERE id = ?";
        DatabaseHelper.executeUpdate(sql, newPriority.getLevel(), taskId);
    }

    // タスクの完了状態を更新する
    public void updateTaskStatus(int taskId, boolean isDone) {
        String sql = "UPDATE tasks SET isDone = ? WHERE id = ?";
        DatabaseHelper.executeUpdate(sql, isDone, taskId);
    }

    // タスクの削除
    public void deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        DatabaseHelper.executeUpdate(sql, taskId);
    }

    // 完了済みタスクの一括削除
    public void deleteCompletedTasks() {
        String sql = "DELETE FROM tasks WHERE isDone = 1";
        DatabaseHelper.executeUpdate(sql);
    }

}
