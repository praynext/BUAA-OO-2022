import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TotalAdventurers adventurers = new TotalAdventurers();
        int m = scanner.nextInt();
        int otherid;
        String name;
        int equiptype;
        int valueid;
        for (int i = 0; i < m; i++) {
            int type = scanner.nextInt();
            int advid = scanner.nextInt();
            switch (type) {
                case 1:
                    name = scanner.next();
                    adventurers.add(advid, name);
                    break;
                case 2:
                    equiptype = scanner.nextInt();
                    adventurers.equip(advid, equiptype, scanner);
                    break;
                case 3:
                    valueid = scanner.nextInt();
                    adventurers.remove(advid, valueid);
                    break;
                case 4:
                    System.out.println(adventurers.sum(advid));
                    break;
                case 5:
                    System.out.println(adventurers.max(advid));
                    break;
                case 6:
                    System.out.println(adventurers.count(advid));
                    break;
                case 7:
                    valueid = scanner.nextInt();
                    System.out.println(adventurers.toString(advid, valueid));
                    break;
                case 8:
                    adventurers.use(advid);
                    break;
                case 9:
                    System.out.println(adventurers.status(advid));
                    break;
                case 10:
                    otherid = scanner.nextInt();
                    adventurers.hire(advid, otherid);
                    break;
                default:
                    break;
            }
        }
    }
}
