// ==============================
// Java Console ToDo App
// - 作成者: sui
// - 今後の展望：
//     ・タスクの保存／読み込み機能（ファイル or データベース）
//     ・完了済みのフィルタ表示
//     ・タスクの編集機能（名前変更）
//     ・締切日・優先度の追加
// ==============================


import java.util.InputMismatchException;
import java.util.Scanner;

// コンソールベースのToDoリストアプリの起動クラス
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        String fileName = "tasks.csv";
        taskManager.loadFromFile(fileName); // アプリ起動時にファイルからタスクを読み込む


        while(true){
            //タスク一覧表示
            taskManager.printTasks();
            // メニュー表示と操作受付
            System.out.println("☆何をしますか？");
            System.out.println("1.追加　2.完了　3.削除　4.終了");
            try{
                int numSelection = scanner.nextInt();
                scanner.nextLine();  // 改行を吸収

                if(numSelection == 1){
                    taskManager.addTask(scanner);

                }else if(numSelection == 2){
                    taskManager.completeTask(scanner);

                }else if(numSelection == 3){
                    taskManager.deleteTask(scanner);

                } else if(numSelection == 4){
                    taskManager.saveToFile(fileName);//  終了時にタスクをファイルに保存
                    System.out.println("終了します。");
                    break;

                }else{
                    System.out.println("入力番号が不正です。もう一度入力してください。");
                }
            } catch (InputMismatchException e) {
                scanner.nextLine(); // 数値以外の入力の例外処理
                System.out.println("※数字で入力してください！");
            }
        }   
    }
}
