package cs411.utils;

public class Utils {
    public static String repeat(String s, int length) {
        return new String(new char[length]).replace("\0", s);
    }
}
