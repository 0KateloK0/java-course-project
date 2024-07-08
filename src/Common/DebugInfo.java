package Common;

public class DebugInfo {
    public static boolean isDebug;

    public static void print(Object debugInfo) {
        if (isDebug)
            System.err.println(debugInfo);
    }
}
