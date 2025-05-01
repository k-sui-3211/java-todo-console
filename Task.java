//タスクの状態を管理するクラス
public class Task {
    private String title; //タスクの内容
    private boolean isDone; //タスクが完了しているかどうか

    //コンストラクタ
    public Task(String title){
        this.title = title;
        this.isDone = false;
    }

    //タスクの完了が入力された際の処理
    public void markDone(){
        this.isDone = true;
    }

    //タスクの表示方法
    @Override
    public String toString(){
        return (isDone?"〔×〕":"〔〕")+title;
    }

    // Taskクラスに追加：
    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return isDone;
    }

    //titleの書き換え
    public void setTitle(String newTitle){
        this.title=newTitle;
    }
    
}
