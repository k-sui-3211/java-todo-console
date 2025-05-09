import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//DB接続・クエリ実行クラス
public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:tasks.db";

    // DB作成
    public static void initializeDatabase() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS tasks (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        title TEXT NOT NULL,
                        isDone INTEGER NOT NULL,
                        dueDate TEXT,
                        priority INTEGER
                    );
                """;

        executeUpdate(sql);
    }

    // DB接続を取得する共通メソッド
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // INSERT /UPDATE / DELETE 用汎用メソッド
    public static void executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("DB接続に失敗しました。" + e.getMessage());
        }
    }

    // SELECT用の汎用メソッド
    public static List<Task> executeQuery(String sql, Object... params) {
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                boolean isDone = rs.getInt("isDone") == 1;

                // 追加された dueDate カラムの処理
                String dueDateStr = rs.getString("dueDate");
                LocalDate dueDate = (dueDateStr != null) ? LocalDate.parse(dueDateStr) : null;

                // 追加された priority カラムの処理
                int priorityLevel = rs.getInt("priority");
                Priority priority = Priority.fromLevel(priorityLevel);

                // 新しい Task コンストラクタに合わせる
                Task task = new Task(id, title, dueDate, priority);

                if (isDone)
                    task.markDone();

                tasks.add(task);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }
}
