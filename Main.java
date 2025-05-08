// ==============================
// Java Console ToDo App
// - 作成者: sui
// - 今後の展望：
//     ・完了済みのフィルタ表示
//     ・締切日・優先度の追加
// ==============================

import java.util.InputMismatchException;
import java.util.Scanner;

// コンソールベースのToDoリストアプリの起動クラス
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        taskManager.loadFromDatabase(); // アプリ起動時にファイルからタスクを読み込む

        // タスク一覧表示
        taskManager.printTasks();

        while (true) {

            // メニュー表示と操作受付
            System.out.println("\n☆ 何をしますか？");
            System.out.println("""

                    ====メニュー====
                    1.タスクを追加
                    2.タスクを編集
                    3.タスクを完了
                    4.タスクを削除
                    5.表示モードを切り替える
                    6.アプリを終了する
                    """);
            try {
                int numSelection = scanner.nextInt();
                scanner.nextLine(); // 改行を吸収

                switch (numSelection) {
                    case 1 -> {
                        System.out.println("タスクの内容を入力してください：");
                        String title = scanner.nextLine();
                        taskManager.addTask(title);
                        taskManager.printTasks();
                    }
                    case 2 -> {
                        System.out.println("編集するタスクの番号を入力してください：");
                        int taskNumber = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("新しいタスク内容を入力してください：");
                        String newTitle = scanner.nextLine();
                        taskManager.editTask(taskNumber, newTitle);
                        taskManager.printTasks();
                    }
                    case 3 -> {
                        System.out.println("完了にするタスクの番号を入力してください：");
                        int taskNumber = scanner.nextInt();
                        scanner.nextLine();
                        taskManager.completeTask(taskNumber);
                        taskManager.printTasks();
                    }
                    case 4 -> {
                        System.out.println("削除するタスクの番号を入力してください：");
                        int taskNumber = scanner.nextInt();
                        scanner.nextLine();
                        taskManager.deleteTask(taskNumber);
                        taskManager.printTasks();
                    }
                    case 5 -> {
                        System.out.println("""
                                ==== 表示モードを選択 ====
                                1. 全て表示
                                2. 未完了のみ
                                3. 完了のみ
                                """);
                        int mode = scanner.nextInt();
                        scanner.nextLine();
                        taskManager.printTasks(mode);
                    }
                    case 6 -> {
                        System.out.println("終了します。");
                        return;
                    }
                    default -> System.out.println("入力番号が不正です。もう一度入力してください。");
                }

            } catch (InputMismatchException e) {
                scanner.nextLine(); // 数値以外の入力の例外処理
                System.out.println("※数字で入力してください！");
            }
        }
    }
}
