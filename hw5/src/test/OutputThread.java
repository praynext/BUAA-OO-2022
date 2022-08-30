package test;

import com.oocourse.TimableOutput;

public class OutputThread {
    public static synchronized long println(String msg) {
        return TimableOutput.println(msg);
    }
}