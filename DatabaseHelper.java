import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//DB接続・クエリ実行クラス
public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:tasks.db";

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
    public static List<Object[]> executeQuery(String sql, Object... params) {
        List<Object[]> resultList = new ArrayList<>();

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                int columnCount = rs.getMetaData().getColumnCount();

                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        row[i] = rs.getObject(i + 1);
                    }
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("DBクエリ実行に失敗しました。" + e.getMessage());
        }
        return resultList;
    }
}
