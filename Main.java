
/**
 * Main.java - Java Console ToDo App
 * 
 * このクラスはアプリのエントリーポイントであり、メインメニューを表示する。
 * 機能一覧：
 * - タスクの追加、編集、削除、完了
 * - 完了済みタスクの一括削除
 * - 並び替え（完了/未完了、期限順、優先度順）
 * - データベースのバックアップ/リストア
 */

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // データベースの初期化（テーブルが存在しない場合のみ作成）
        DatabaseHelper.initializeDatabase();

        // タスク管理クラスのインスタンス化
        TaskManager taskManager = new TaskManager(scanner);

        // 起動時のタスク一覧表示（全件表示）
        taskManager.printTasks(null, null, null, null, false);

        while (true) {
            System.out.println("""

                    ==== メニュー ====
                    1. タスクを追加
                    2. タスクを編集
                    3. タスクを完了
                    4. タスクを削除
                    5. タスクの並び替え（完了/未完了、期限順、優先度順）
                    6. 完了済タスクの一括削除
                    7. バックアップの作成
                    8. データのリストア
                    9. アプリを終了
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
                    case 6 -> taskManager.deleteCompletedTasks();
                    case 7 -> taskManager.backupData();
                    case 8 -> taskManager.restoreData();
                    case 9 -> {
                        System.out.println("\nアプリを終了します。");
                        return;
                    }
                    default -> System.out.println("\n無効な選択です。");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n数値を入力してください。");
                scanner.nextLine(); // バッファクリア
            }
        }
    }
}
