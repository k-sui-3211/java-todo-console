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
    private int nextId = 1;// idは初期値1から開始

    // DBから復元するメソッド
    public void loadFromDatabase() {
        tasks.clear(); // 今のリストを初期化

        String url = "jdbc:sqlite:tasks.db";
        String selectSQL = "SELECT id, title, isDone FROM tasks";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                boolean isDone = rs.getInt("isDone") == 1;

                Task task = new Task(id, title);
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

    // DBに保存するメソッド
    public void saveToDatabase(String dbFileName) {
        String url = "jdbc:sqlite:" + dbFileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            // tasksテーブルがなければ作成
            String createTableSQL = "CREATE TABLE IF NOT EXISTS tasks (" +
                    "id INTEGER PRIMARY KEY, " +
                    "title TEXT NOT NULL, " +
                    "isDone INTEGER NOT NULL)";
            conn.createStatement().execute(createTableSQL);

            // 既存のデータを一旦削除（上書き保存）
            conn.createStatement().execute("DELETE FROM tasks");

            // データを挿入
            String insertSQL = "INSERT INTO tasks (id, title, isDone) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                for (Task task : tasks) {
                    pstmt.setInt(1, task.getId());
                    pstmt.setString(2, task.getTitle());
                    pstmt.setInt(3, task.isDone() ? 1 : 0);
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
        Task task = new Task(nextId++, title);
        tasks.add(task);

        String url = "jdbc:sqlite:tasks.db";
        String insertSQL = "INSERT INTO tasks (id, title, isDone) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setInt(1, task.getId());
            pstmt.setString(2, task.getTitle());
            pstmt.setInt(3, task.isDone() ? 1 : 0);
            pstmt.executeUpdate();

            System.out.println("タスクを追加しました。");

        } catch (SQLException e) {
            System.out.println("SQLiteへの追加に失敗しました。");
            e.printStackTrace();
        }
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
                String newTitle = scanner.nextLine();
                task.setTitle(newTitle);

                String url = "jdbc:sqlite:tasks.db";
                String updateSQL = "UPDATE tasks SET title = ? WHERE id = ?";

                try (Connection conn = DriverManager.getConnection(url);
                        PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

                    pstmt.setString(1, newTitle);
                    pstmt.setInt(2, task.getId());
                    pstmt.executeUpdate();

                } catch (SQLException e) {
                    System.out.println("SQLiteの更新に失敗しました。");
                    e.printStackTrace();
                }
            } else {
                System.out.println("その番号のタスクは存在しません。");
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();// 数値以外の入力の例外処理
            System.out.println("※タスク番号は数字で入力してください。");
        }
    }

    // 入力された番号のタスクを完了状態にする
    public void completeTask(Scanner scanner) {
        System.out.println("完了とするタスクの番号を入力してください。");
        try {
            int taskNumber = scanner.nextInt();
            if (taskNumber >= 1 && taskNumber <= tasks.size()) {
                Task task = tasks.get(taskNumber - 1);
                task.markDone();

                String url = "jdbc:sqlite:tasks.db";
                String updateSQL = "UPDATE tasks SET isDone = 1 WHERE id = ?";

                try (Connection conn = DriverManager.getConnection(url);
                        PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                    pstmt.setInt(1, task.getId());
                    pstmt.executeUpdate();

                } catch (SQLException e) {
                    System.out.println("SQLiteの更新に失敗しました。");
                    e.printStackTrace();
                }
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
                Task task = tasks.get(taskNumber - 1);
                int id = task.getId();
                tasks.remove(taskNumber - 1);

                String url = "jdbc:sqlite:tasks.db";
                String deleteSQL = "DELETE FROM tasks WHERE id = ?";

                try (Connection conn = DriverManager.getConnection(url);
                        PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();

                } catch (SQLException e) {
                    System.out.println("SQLiteの更新に失敗しました。");

                }
            } else {
                System.out.println("その番号のタスクは存在しません！");
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();// 数値以外の入力の例外処理
            System.out.println("※タスク番号は数字で入力してください。");
        }
    }
}
