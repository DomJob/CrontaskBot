package display;

public class FormattingUtils {
    public static String sanitize(String input) {
        String output = input.replaceAll("<", "&lt;");
        output = output.replaceAll(">", "&gt;");
        return output;
    }
}
