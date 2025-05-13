import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//タスクの状態を管理するクラス
public class Task {
    private int id; // タスクのID
    private String title; // タスクの内容
    private boolean isDone; // タスクが完了しているかどうか
    private LocalDate dueDate; // 期限
    private Priority priority; // 優先度

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

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public boolean isDone() {
        return isDone;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    // タスクを完了にする
    public void markDone() {
        this.isDone = true;
    }

    private String formatColumn(String input, int width, boolean rightAlign) {
        int length = 0;
        for (char c : input.toCharArray()) {
            length += (String.valueOf(c).getBytes().length > 1) ? 2 : 1;
        }

        int padding = width - length;
        if (padding <= 0)
            return input;

        String paddingSpaces = " ".repeat(padding);
        return rightAlign ? paddingSpaces + input : input + paddingSpaces;
    }

    // タスクの表示方法
    public String toString() {
        String status = isDone ? "[x]" : "[ ]";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String dueDateStr = (dueDate != null) ? dueDate.format(formatter) : "未設定";

        // 列幅を全角対応で整列
        String formattedStatus = formatColumn(status, 4, false);
        String formattedTitle = formatColumn(title, 20, false);
        String formattedDueDate = formatColumn(dueDateStr, 15, false);
        String formattedPriority = formatColumn(priority.toString(), 10, false);

        return formattedStatus + " " + formattedTitle + " " + formattedDueDate + " " + formattedPriority;

    }

}
