import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ToAll extends Message {
    public void init(String record) {
        Pattern p = Pattern.compile(
                "(\\d{1,4})/(\\d{1,2})/(\\d{1,2})-(\\w+):\"([a-z0-9A-Z?!,. ]*)\"");
        Matcher matcher = p.matcher(record);
        if (matcher.matches()) {
            String content = matcher.group(0);
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            String sendname = matcher.group(4);
            this.setContent(content);
            this.setYear(year);
            this.setMonth(month);
            this.setDay(day);
            this.setSendname(sendname);
            this.initAB(matcher.group(5));
        }
    }
}
