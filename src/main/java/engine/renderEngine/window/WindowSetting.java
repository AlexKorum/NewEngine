package engine.renderEngine.window;

public final class WindowSetting {
    private static long windowId;
    private static String title;
    private static int width;
    private static int height;

    static {
        windowId = -1;
        title = "GameEngine";
        width = 1280;
        height = 720;
    }

    public static long getWindowId() {
        return windowId;
    }

    public static void setWindowId(long windowId) {
        WindowSetting.windowId = windowId;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        WindowSetting.title = title;
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        WindowSetting.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        WindowSetting.height = height;
    }
}
