import Server.LeaderBoard;
import Server.LeaderBoardRunnable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * some tests to ensure the basic functions of the class work as intended
 */
class LeaderBoardTest {
    LeaderBoard board;
    private List<String> nameList;
    private List<Integer> streakList;

    @BeforeEach
    public void init() {
        nameList = new ArrayList<>();
        streakList = new ArrayList<>();
        nameList.add("Ahmed");
        nameList.add("Kamo");
        nameList.add("Khalid");
        nameList.add("Samo");
        streakList.add(1);
        streakList.add(2);
        streakList.add(13);
        streakList.add(7);
        board = new LeaderBoard(nameList, streakList);
    }

    @Test
    void testUpdateAndGet() {
        //runnableBoard = new LeaderBoardRunnable(board, "update Kamo 10");
        board.update("Kamo", 10);
        assertEquals(10, board.get("Kamo"));
    }

    @Test
    void testDelete() {
        board.delete("Kamo");
        assertEquals(0, board.get("Kamo"));
    }

    @Test
    void testPrettyPrintTop3() {
        String output = "1- Khalid: 13\n2- Samo: 7\n3- Kamo: 2\n";
        assertTrue(board.prettyPrintTop3().contains(output));
    }

    @Test
    void testMultiThreadIncrementAndDecrement() {
        for (int i = 0; i < 10; i++) {
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
        assertEquals(2, board.get("Kamo"));
    }

    @Test
    void testMultiThreadDelete() {
        for (int i = 0; i < 10; i++) {
            Thread t1 = new Thread(new LeaderBoardRunnable(board, "delete Kamo"));  //does nothing if user doesn't exist
            t1.start();
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(0, board.get("Kamo"));
    }

    @Test
    void testMultiThreadUpdate() {
        for (int i = 0; i < 10; i ++) {
            Thread t1 = new Thread(new LeaderBoardRunnable(board, "update Kamo 5"));
            t1.run();
        }
        assertEquals(5, board.get("Kamo"));
    }
}