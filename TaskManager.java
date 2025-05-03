import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

//タスクの処理内容を管理するクラス
public class TaskManager {

    private List<Task> tasks = new ArrayList<>();

    public void loadFromDatabase() {
        tasks.clear(); // 今のリストを初期化

        String url = "jdbc:sqlite:tasks.db";
        String selectSQL = "SELECT title, isDone FROM tasks";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                String title = rs.getString("title");
                boolean isDone = rs.getInt("isDone") == 1;

                Task task = new Task(title);
                if (isDone) {
                    task.markDone();
                }
                tasks.add(task);
            }

            System.out.println("データベースから復元完了: " + tasks.size() + " 件");

        } catch (SQLException e) {
            System.out.println("SQLite復元に失敗しました。");
            e.printStackTrace();
        }
    }

    // ファイルに保存するメソッド
    public void saveToDatabase(String dbFileName) {
        String url = "jdbc:sqlite:" + dbFileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            // tasksテーブルがなければ作成
            String createTableSQL = "CREATE TABLE IF NOT EXISTS tasks (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "isDone INTEGER NOT NULL)";
            conn.createStatement().execute(createTableSQL);

            // 既存のデータを一旦削除（上書き保存）
            conn.createStatement().execute("DELETE FROM tasks");

            // データを挿入
            String insertSQL = "INSERT INTO tasks (title, isDone) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                for (Task task : tasks) {
                    pstmt.setString(1, task.getTitle());
                    pstmt.setInt(2, task.isDone() ? 1 : 0);
                    pstmt.executeUpdate();
                }
            }
            System.out.println("SQLite保存完了: " + tasks.size() + " 件");

        } catch (SQLException e) {
            System.out.println("SQLite保存に失敗しました。");
            e.printStackTrace();
        }
    }

    // タスク一覧を表示するメソッド
    public void printTasks() {
        System.out.println("\n==== タスク一覧 ====");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    // タスクの追加をするメソッド
    public void addTask(Scanner scanner) {
        System.out.println("タスクの内容を入力してください。");
        String title = scanner.nextLine();
        Task task = new Task(title);
        tasks.add(task);
    }

    // タスク編集メソッド
    public void editTask(Scanner scanner) {
        System.out.println("編集するタスクの番号を入力してください。");
        try {
            int taskNumber = scanner.nextInt();
            scanner.nextLine();
            if (taskNumber >= 1 && taskNumber <= tasks.size()) {
                Task task = tasks.get(taskNumber - 1);
                System.out.println("タスクの内容を入力してください。");
                task.setTitle(scanner.nextLine());
            } else {
                System.out.println("その番号のタスクは存在しません。");
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();// 数値以外の入力の例外処理
            System.out.println("※タスク番号は数字で入力してください。");
        }
    }

    // 入力された番号のタスクを完了状態にする（完了メニュー選択時に呼ばれる）
    public void completeTask(Scanner scanner) {
        System.out.println("完了とするタスクの番号を入力してください。");
        try {
            int taskNumber = scanner.nextInt();
            if (taskNumber >= 1 && taskNumber <= tasks.size()) {
                Task task = tasks.get(taskNumber - 1);
                task.markDone();
            } else {
                System.out.println("その番号のタスクは存在しません。");
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();// 数値以外の入力の例外処理
            System.out.println("※タスク番号は数字で入力してください。");
        }
    }

    // タスクを削除するメソッド
    public void deleteTask(Scanner scanner) {
        System.out.println("削除するタスク番号を入力してください。");
        try {
            int taskNumber = scanner.nextInt();
            if (taskNumber >= 1 && taskNumber <= tasks.size()) {
                tasks.remove(taskNumber - 1);
            } else {
                System.out.println("その番号のタスクは存在しません！");
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();// 数値以外の入力の例外処理
            System.out.println("※タスク番号は数字で入力してください。");
        }
    }
}
