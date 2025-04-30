import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

//タスクの処理内容を管理するクラス
public class TaskManager {

    private List<Task>tasks=new ArrayList<>();

    //タスク一覧を表示するメソッド
    public void printTasks(){
        System.out.println("\n==== タスク一覧 ====");
        for (int i = 0; i < tasks.size(); i++) {
        System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    //タスクの追加をするメソッド
    public void addTask(Scanner scanner){
        System.out.println("タスクの内容を入力してください。");
        String title = scanner.nextLine();
        Task task = new Task(title);
        tasks.add(task);
    }

    // 入力された番号のタスクを完了状態にする（完了メニュー選択時に呼ばれる）
    public void completeTask(Scanner scanner){
        System.out.println("完了とするタスクの番号を入力してください。");
        try{
            int taskNumber = scanner.nextInt();
            if (taskNumber >= 1 && taskNumber <= tasks.size()) {
                Task task = tasks.get(taskNumber - 1);
                task.markDone();
            } else {
                System.out.println("その番号のタスクは存在しません。");
            }
        }catch(InputMismatchException e){
            scanner.nextLine();//数値以外の入力の例外処理
            System.out.println("※タスク番号は数字で入力してください。");
        }   
    }
    
    //タスクを削除するメソッド
    public void deleteTask(Scanner scanner){
        System.out.println("削除するタスク番号を入力してください。");
        try{
            int taskNumber = scanner.nextInt();
            if(taskNumber >= 1 && taskNumber <= tasks.size()){
                tasks.remove(taskNumber-1);
            }else{
                System.out.println("その番号のタスクは存在しません！");
            }
        }catch(InputMismatchException e){
            scanner.nextLine();//数値以外の入力の例外処理
            System.out.println("※タスク番号は数字で入力してください。");
        }    
    }
}
