public enum Priority {
    HIGH(1), MEDIUM(2), LOW(3);

    private final int level;

    Priority(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static Priority fromLevel(int level) {
        for (Priority p : Priority.values()) {
            if (p.getLevel() == level) {
                return p;
            }
        }
        return LOW; // デフォルトは LOW とする
    }
}
