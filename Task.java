//タスクの状態を管理するクラス
public class Task {
    private int id; // タスクのID
    private String title; // タスクの内容
    private boolean isDone; // タスクが完了しているかどうか

    // ID付きのコンストラクタ
    public Task(int id, String title) {
        this.id = id;
        this.title = title;
        this.isDone = false;
    }

    // タスク完了マーク
    public void markDone() {
        this.isDone = true;
    }

    // タスクの表示方法
    @Override
    public String toString() {
        return (isDone ? "〔×〕" : "〔 〕") + title;
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
}
