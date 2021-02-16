package Server;

import java.util.ArrayList;
import java.util.List;

/**
 * a runnable that is used to run the LeaderBoard class in multi threaded fashion
 */
public class LeaderBoardRunnable implements Runnable {
    private LeaderBoard board;
    private String[] command;
    private int streakValue;
    public LeaderBoardRunnable() {
    }

    public LeaderBoardRunnable(LeaderBoard obj, String comm) {
        board = obj;
        command = comm.split(" ");
    }

    /**
     * calls a method from the LeaderBoard class depending on the arguments received
     */
    @Override
    public void run() {
        try {
            switch (command[0]) {
                case "addplayer":
                    board.addPlayer(command[1]);
                    break;
                case "update":
                    board.update(command[1], Integer.parseInt(command[2]));
                    break;

                case "delete":
                    board.delete(command[1]);
                    break;

                case "get":
                    streakValue = board.get(command[1]);
                    break;

                case "prettyPrintTop3":
                    System.out.println(board.prettyPrintTop3());
                    break;

                case "incrementStreak":
                    board.incrementStreak(command[1]);
                    break;

                case "decrementStreak":
                    board.decrementStreak(command[1]);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<String> nameList = new ArrayList<>();
        List<Integer> streakList = new ArrayList<>();

        nameList.add("Ahmed");
        nameList.add("Kamo");   //streak value is 2
        nameList.add("Khalid");
        nameList.add("Samo");
        streakList.add(1);
        streakList.add(2);
        streakList.add(13);
        streakList.add(7);
        LeaderBoard board = new LeaderBoard(nameList, streakList);

        for (int i = 0; i < 10; i++) {  //increment and decrement the player streak the same amount of times
            Thread t1 = new Thread(new LeaderBoardRunnable(board, "incrementStreak Kamo"));
            Thread t2 = new Thread(new LeaderBoardRunnable(board, "decrementStreak Kamo"));
            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("The streak is " + board.get("Kamo"));   //should print out the value 2
    }
}
