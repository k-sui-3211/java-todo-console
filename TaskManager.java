import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

//タスクの処理内容を管理するクラス
public class TaskManager {

    private List<Task>tasks=new ArrayList<>();

    //タスクをファイルから復元するメソッド
    public void loadFromFile(String fileName){
        tasks.clear();
        System.out.println("復元処理を開始します...");
        try {
            FileInputStream fis = new FileInputStream(fileName);// ファイルをバイトで読む
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");// UTF-8で文字に変換
            BufferedReader reader = new BufferedReader(isr);// 効率よく1行ずつ読む            
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2); // カンマで2つに分ける
                String title = parts[0]; // タスク名
                boolean isDone = Boolean.parseBoolean(parts[1]); // true または false
                Task task = new Task(title);
                if (isDone) {
                    task.markDone(); // 完了フラグがtrueなら完了状態にする
                }   
                tasks.add(task); // リストに追加
                count++;
            }    
            reader.close(); 
            System.out.println("読み込み完了: " + count + "件");
        
        } catch (IOException e) {
            System.out.println("読み込みに失敗しました。ファイルがないか、壊れています。");
            e.printStackTrace();
        }
    }

    //ファイルに保存するメソッド
    public void saveToFile(String fileName){
        try{
            FileOutputStream fos = new FileOutputStream(fileName);// ファイルを開く
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");// UTF-8で書く準備
            BufferedWriter writer = new BufferedWriter(osw);// 効率よく書く

            for(Task task:tasks){
                writer.write(task.getTitle() + "," + task.isDone());
                writer.newLine();
            }
            writer.close();  // ★ファイルを確実に閉じる！
    
            System.out.println("保存完了: " + tasks.size() + " 件");
        }catch(IOException e){
            System.out.println("保存に失敗しました。");
            e.printStackTrace();
        }
    }

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

    //タスク編集メソッド
    public void editTask(Scanner scanner){    
        System.out.println("編集するタスクの番号を入力してください。");         
        try{    
            int taskNumber = scanner.nextInt();                         
            scanner.nextLine();                          
            if (taskNumber >= 1 && taskNumber <= tasks.size()) {     
                Task task = tasks.get(taskNumber - 1);               
                System.out.println("タスクの内容を入力してください。");              
                task.setTitle(scanner.nextLine());               
            } else {
                System.out.println("その番号のタスクは存在しません。");
            } 
        }catch(InputMismatchException e){
            scanner.nextLine();//数値以外の入力の例外処理
            System.out.println("※タスク番号は数字で入力してください。");
        }           
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
