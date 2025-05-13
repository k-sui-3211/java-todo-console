//タスクの優先度（HIGH, MEDIUM, LOW）を列挙型で管理
public enum Priority {
    HIGH(1), MEDIUM(2), LOW(3);

    private final int level;

    // コンストラクタ
    Priority(int level) {
        this.level = level;
    }

    // 優先度のレベルを取得
    public int getLevel() {
        return level;
    }

    // レベルに応じた優先度を取得する
    public static Priority fromLevel(int level) {
        for (Priority p : Priority.values()) {
            if (p.getLevel() == level) {
                return p;
            }
        }
        return LOW; // デフォルトは LOW とする
    }
}
