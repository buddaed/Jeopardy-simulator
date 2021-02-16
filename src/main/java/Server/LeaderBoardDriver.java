package Server;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * driver for the leader board class
 */
public class LeaderBoardDriver {
    public static void main(String[] args) {
        System.out.println("Do you want to input commands? Y/N (if answer N then default commands will be run\n "
                + "which will increment and decrement the player \"Kamo\"'s streak by 10 then displasy it on screen)");
        Scanner in = new Scanner(System.in);

        if (in.next().equalsIgnoreCase("y")) {
            LeaderBoard board = new LeaderBoard(new ArrayList<String>(), new ArrayList<Integer>());
            boolean end = false;

            while (!end) {  //ends when sentinel value is received
                System.out.println("enter the number of the command u want to use\n" +
                        "1-add player\n2-update player streak\n3-delete player\n4-get player streak\n" +
                        "5-print top 3 players\n6-increment player streak\n7-decrement player streak\nenter Q or anything else to quit");
                String command = in.next();
                String name;
                switch (command) {
                    case "1":
                        System.out.println("please enter the name of the player");
                        name = in.next();
                        board.addPlayer(name);
                        break;
                    case "2":
                        System.out.println("please enter the name of the player");
                        name = in.next();
                        System.out.println("please enter the updated value of the player streak");
                        int streak = in.nextInt();
                        board.update(name, streak);
                        break;
                    case "3":
                        System.out.println("please enter the name of the player");
                        name = in.next();
                        board.delete(name);
                        break;
                    case "4":
                        System.out.println("please enter the name of the player");
                        name = in.next();
                        System.out.println("the player's streak is " + board.get(name));
                        break;
                    case "5":
                        System.out.println(board.prettyPrintTop3());
                        break;
                    case "6":
                        System.out.println("please enter the name of the player");
                        name = in.next();
                        board.incrementStreak(name);
                        break;
                    case "7":
                        System.out.println("please enter the name of the player");
                        name = in.next();
                        board.decrementStreak(name);
                        break;
                    default:
                        end = true;
                        break;
                }
            }
        } else
            new LeaderBoardRunnable().main(new String[0]);
    }
}
