package infrastructure.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    public static List<Integer> extractNumbers(String string) {
        List<Integer> list = new ArrayList<>();

        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(string);
        while (m.find()) {
            int value = Integer.parseInt(m.group());
            list.add(value);
        }

        return list;
    }
}
