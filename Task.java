import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//タスクの状態を管理するクラス
public class Task {
    private int id; // タスクのID
    private String title; // タスクの内容
    private boolean isDone; // タスクが完了しているかどうか
    private LocalDate dueDate;
    private Priority priority;

    // 新規タスク作成用（ID未確定）
    public Task(String title, LocalDate dueDate, Priority priority) {
        this.id = -1;
        this.title = title;
        this.isDone = false;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    // DB復元用コンストラクタ
    public Task(int id, String title, LocalDate dueDate, Priority priority) {
        this.id = id;
        this.title = title;
        this.isDone = false;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    // タスク完了マーク
    public void markDone() {
        this.isDone = true;
    }

    // タスクの表示方法
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dueDateStr = (dueDate != null) ? dueDate.format(formatter) : "なし";
        return (isDone ? "[完了] " : "[未完了] ") + title + " | 期限: " + dueDateStr + " | 優先度: " + priority;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

}
