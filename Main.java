// ==============================
// Java Console ToDo App
// - 作成者: sui
// - 今後の展望：
//     ・締切日・優先度の追加
//     ・タイトルや日付によるソート機能の実装
//　　　・完了済みタスクの一括削除や検索機能の導入
// ==============================

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager(scanner);
        taskManager.printTasks(null, null, null, null);
        DatabaseHelper.initializeDatabase();

        while (true) {
            System.out.println("""

                    ==== メニュー ====
                    1. タスクを追加
                    2. タスクを編集
                    3. タスクを完了
                    4. タスクを削除
                    5. タスクの並び替え（完了/未完了、期限順、優先度順）
                    6. アプリを終了
                    """);

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // 改行消費

                switch (choice) {
                    case 1 -> taskManager.addTask();
                    case 2 -> taskManager.editTask();
                    case 3 -> taskManager.completeTask();
                    case 4 -> taskManager.deleteTask();
                    case 5 -> taskManager.sortTasks();
                    case 6 -> {
                        System.out.println("アプリを終了します。");
                        return;
                    }
                    default -> System.out.println("無効な選択です。");
                }
            } catch (InputMismatchException e) {
                System.out.println("数値を入力してください。");
                scanner.nextLine(); // バッファクリア
            }
        }
    }
}
