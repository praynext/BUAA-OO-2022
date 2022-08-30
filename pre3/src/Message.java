import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Message {
    private int year;
    private int month;
    private int day;
    private String sendname = "";
    private String recvname = "";
    private String content = "";
    private boolean a1 = false;
    private boolean a2 = false;
    private boolean a3 = false;
    private boolean a4 = false;
    private boolean b1 = false;
    private boolean b2 = false;
    private boolean b3 = false;
    private boolean b4 = false;

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getSendname() {
        return sendname;
    }

    public String getRecvname() {
        return recvname;
    }

    public boolean isA1() {
        return a1;
    }

    public boolean isA2() {
        return a2;
    }

    public boolean isA3() {
        return a3;
    }

    public boolean isA4() {
        return a4;
    }

    public boolean isB1() {
        return b1;
    }

    public boolean isB2() {
        return b2;
    }

    public boolean isB3() {
        return b3;
    }

    public boolean isB4() {
        return b4;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setSendname(String sendname) {
        this.sendname = sendname;
    }

    public void setRecvname(String recvname) {
        this.recvname = recvname;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void init(String record) {
    }

    public String toString() {
        return content + ";";
    }

    public void initAB(String message) {
        Matcher matcher;
        Pattern pa1 = Pattern.compile("a{2,3}b{2,4}c{2,4}.*");
        matcher = pa1.matcher(message);
        if (matcher.matches()) {
            this.a1 = true;
        }
        Pattern pa2 = Pattern.compile(".*a{2,3}b{2,4}c{2,4}");
        matcher = pa2.matcher(message);
        if (matcher.matches()) {
            this.a2 = true;
        }
        Pattern pa3 = Pattern.compile(".*a{2,3}b{2,4}c{2,4}.*");
        matcher = pa3.matcher(message);
        if (matcher.matches()) {
            this.a3 = true;
        }
        Pattern pa4 = Pattern.compile(".*(a.*){2,}(b.*){2,}(c.*){2,}");
        matcher = pa4.matcher(message);
        if (matcher.matches()) {
            this.a4 = true;
        }
        Pattern pb1 = Pattern.compile("a{2,3}b{2,1000000}c{2,4}.*");
        matcher = pb1.matcher(message);
        if (matcher.matches()) {
            this.b1 = true;
        }
        Pattern pb2 = Pattern.compile(".*a{2,3}b{2,1000000}c{2,4}");
        matcher = pb2.matcher(message);
        if (matcher.matches()) {
            this.b2 = true;
        }
        Pattern pb3 = Pattern.compile(".*a{2,3}b{2,1000000}c{2,4}.*");
        matcher = pb3.matcher(message);
        if (matcher.matches()) {
            this.b3 = true;
        }
        Pattern pb4 = Pattern.compile(".*(a.*){2,}(b.*){2,}(c.*){2,}");
        matcher = pb4.matcher(message);
        if (matcher.matches()) {
            this.b4 = true;
        }
    }
}
