import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    public static void main(String[] args) {
        ArrayList<Message> messages = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String line;
        line = scanner.nextLine();
        while (!line.equals("END_OF_MESSAGE")) {
            String[] records = line.split(";");
            for (String record : records) {
                Message message;
                switch (sortmessage(record.trim())) {
                    case 1:
                        message = new ToIndividual();
                        message.init(record.trim());
                        messages.add(message);
                        break;
                    case 2:
                        message = new ToRecipient();
                        message.init(record.trim());
                        messages.add(message);
                        break;
                    case 3:
                        message = new ToAll();
                        message.init(record.trim());
                        messages.add(message);
                        break;
                    default:
                        break;
                }
            }
            line = scanner.nextLine();
        }
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            switch (sortquestion(line)) {
                case 1:
                    answerdate(messages, line.split(" ")[1]);
                    break;
                case 2:
                    answersend(messages, line.split(" ")[1]);
                    break;
                case 3:
                    answerrecv(messages, line.split(" ")[1]);
                    break;
                case 4:
                    answerstring(messages, line.split(" ")[1], line.split(" ")[2]);
                    break;
                default:
                    break;
            }
        }
    }

    static int sortmessage(String record) {
        Pattern p1 = Pattern.compile(
                "\\d{1,4}/\\d{1,2}/\\d{1,2}-\\w+@\\w+ :\"[a-z0-9A-Z?!,. ]*\"");
        Pattern p2 = Pattern.compile(
                "\\d{1,4}/\\d{1,2}/\\d{1,2}-\\w+:\"[a-z0-9A-Z?!,. ]*@\\w+ [a-z0-9A-Z?!,. ]*\"");
        Pattern p3 = Pattern.compile(
                "\\d{1,4}/\\d{1,2}/\\d{1,2}-\\w+:\"[a-z0-9A-Z?!,. ]*\"");
        if (p1.matcher(record).matches()) {
            return 1;
        } else if (p2.matcher(record).matches()) {
            return 2;
        } else if (p3.matcher(record).matches()) {
            return 3;
        } else {
            return 0;
        }
    }

    static int sortquestion(String line) {
        if (line.startsWith("qdate")) {
            return 1;
        } else if (line.startsWith("qsend")) {
            return 2;
        } else if (line.startsWith("qrecv")) {
            return 3;
        } else if (line.startsWith("qmess")) {
            return 4;
        } else {
            return 0;
        }
    }

    static void answerdate(ArrayList<Message> messages, String date) {
        Matcher matcher;
        Pattern d1 = Pattern.compile("(\\d{1,4})/(\\d{1,2})/(\\d{1,2})");
        matcher = d1.matcher(date);
        if (matcher.matches()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            answerd1(messages, year, month, day);
            return;
        }
        Pattern d2 = Pattern.compile("(\\d{1,4})//");
        matcher = d2.matcher(date);
        if (matcher.matches()) {
            int year = Integer.parseInt(matcher.group(1));
            answerd2(messages, year);
            return;
        }
        Pattern d3 = Pattern.compile("/(\\d{1,2})/");
        matcher = d3.matcher(date);
        if (matcher.matches()) {
            int month = Integer.parseInt(matcher.group(1));
            answerd3(messages, month);
            return;
        }
        Pattern d4 = Pattern.compile("//(\\d{1,2})");
        matcher = d4.matcher(date);
        if (matcher.matches()) {
            int day = Integer.parseInt(matcher.group(1));
            answerd4(messages, day);
            return;
        }
        Pattern d5 = Pattern.compile("(\\d{1,4})/(\\d{1,2})/");
        matcher = d5.matcher(date);
        if (matcher.matches()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            answerd5(messages, year, month);
            return;
        }
        Pattern d6 = Pattern.compile("/(\\d{1,2})/(\\d{1,2})");
        matcher = d6.matcher(date);
        if (matcher.matches()) {
            int month = Integer.parseInt(matcher.group(1));
            int day = Integer.parseInt(matcher.group(2));
            answerd6(messages, month, day);
        }
    }

    static void answersend(ArrayList<Message> messages, String send) {
        for (Message message : messages) {
            if (message.getSendname().equals(send)) {
                System.out.println(message);
            }
        }
    }

    static void answerrecv(ArrayList<Message> messages, String recv) {
        for (Message message : messages) {
            if (message.getRecvname().equals(recv)) {
                System.out.println(message);
            }
        }
    }

    static void answerd1(ArrayList<Message> messages, int year, int month, int day) {
        for (Message message : messages) {
            if (message.getYear() == year &&
                    message.getMonth() == month &&
                    message.getDay() == day) {
                System.out.println(message);
            }
        }
    }

    static void answerd2(ArrayList<Message> messages, int year) {
        for (Message message : messages) {
            if (message.getYear() == year) {
                System.out.println(message);
            }
        }
    }

    static void answerd3(ArrayList<Message> messages, int month) {
        for (Message message : messages) {
            if (message.getMonth() == month) {
                System.out.println(message);
            }
        }
    }

    static void answerd4(ArrayList<Message> messages, int day) {
        for (Message message : messages) {
            if (message.getDay() == day) {
                System.out.println(message);
            }
        }
    }

    static void answerd5(ArrayList<Message> messages, int year, int month) {
        for (Message message : messages) {
            if (message.getYear() == year && message.getMonth() == month) {
                System.out.println(message);
            }
        }
    }

    static void answerd6(ArrayList<Message> messages, int month, int day) {
        for (Message message : messages) {
            if (message.getMonth() == month && message.getDay() == day) {
                System.out.println(message);
            }
        }
    }

    static void answerstring(ArrayList<Message> messages, String var1, String var2) {
        for (Message message : messages) {
            if (var1.equals("A")) {
                switch (var2) {
                    case "1":
                        if (message.isA1()) {
                            System.out.println(message);
                        }
                        break;
                    case "2":
                        if (message.isA2()) {
                            System.out.println(message);
                        }
                        break;
                    case "3":
                        if (message.isA3()) {
                            System.out.println(message);
                        }
                        break;
                    case "4":
                        if (message.isA4()) {
                            System.out.println(message);
                        }
                        break;
                    default:
                        break;
                }
            } else if (var1.equals("B")) {
                switch (var2) {
                    case "1":
                        if (message.isB1()) {
                            System.out.println(message);
                        }
                        break;
                    case "2":
                        if (message.isB2()) {
                            System.out.println(message);
                        }
                        break;
                    case "3":
                        if (message.isB3()) {
                            System.out.println(message);
                        }
                        break;
                    case "4":
                        if (message.isB4()) {
                            System.out.println(message);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
