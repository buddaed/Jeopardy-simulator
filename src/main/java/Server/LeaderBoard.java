package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * maintains a leader board of all active players currently taking the quiz
 * and are connected to the server
 */
public class LeaderBoard {
    private List<String> names;
    private List<Integer> streaks;
    private ReentrantLock streakChangeLock;

    public LeaderBoard(List<String> nameList, List<Integer> streakList) {
        names = new ArrayList<>(nameList);
        streaks = new ArrayList<>(streakList);
        streakChangeLock = new ReentrantLock();
    }

    /**
     * update the current correct question answered streak for
     * user: name to be streaks
     *
     * @param name:   the user to update the streak for
     * @param streak: the amount of the user's current streak
     */
    public void update(String name, int streak) {
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals(name)) {
                streaks.set(i, streak);
            }
        }
    }

    /**
     * increments the streak of a player by 1
     *
     * @param name the name of the player you want to increment the streak for
     */
    public void incrementStreak(String name) {
        streakChangeLock.lock();
        try {
            int streak = streaks.get(names.indexOf(name));
            streaks.set(names.indexOf(name), streak + 1);
        }
        finally {
            streakChangeLock.unlock();
        }
    }

    /**
     * decrements the streak of a player by 1
     *
     * @param name the name of the player you want to decrement the streak for
     */
    public void decrementStreak(String name) {
        streakChangeLock.lock();
        try {
            int streak = streaks.get(names.indexOf(name));
            if (streak > 0) {
                streaks.set(names.indexOf(name), streak - 1);
            } else {
                System.out.println("player's streak cannot go below 0");
            }
        }
        finally {
            streakChangeLock.unlock();
        }
    }

    /**
     * adds a player to the leader board
     *
     * @param name the name of the player you want to add
     */
    public void addPlayer(String name) {
        names.add(name);
        streaks.add(0);
    }

    /**
     * Remove a user from the LeaderBoard
     *
     * @param name: the user to remove
     **/
    public void delete(String name) {
        streakChangeLock.lock();
        try {
            for (int i = 0; i < names.size(); i++) {
                if (names.get(i).equals(name)) {
                    streaks.remove(i);
                    names.remove(i);
                }
            }
        }
        finally {
            streakChangeLock.unlock();
        }
    }

    /**
     * Retrieve the active streak for a give user
     *
     * @param name: the user for which to retrieve the streak
     *              or 0 if no such user exists
     * @return the current active streak for the given user or 0 if
     * no such user exists
     **/
    public int get(String name) {
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals(name)) {
                return streaks.get(i);
            }
        }
        return 0;
    }

    /**
     * Convert the Leaderboard into an aesthetically pleasing (as per your own taste) String containing the Top 3 users with their active streaks
     *
     * @return a text version of the top 3 streaks
     **/
    public String prettyPrintTop3() {
        String text = "Top 3 active streak holders \n";
        List<String> nameList = new ArrayList<>(names);
        List<Integer> streakList = new ArrayList<>(streaks);
        int max;
        String maxName;

        for (int i = 0; i < 3; i++) {
            max = -1;
            maxName = "";
            for (int j = 0; j < streakList.size(); j++) {
                if (max < streakList.get(j)) {
                    max = streakList.get(j);
                    maxName = nameList.get(j);
                }
            }
            if (i < streaks.size() - i) {
                nameList.remove(nameList.indexOf(maxName));
                streakList.remove(streakList.indexOf(max));
            }
            if (!text.contains(maxName)) {
                text += i + 1 + "- " + maxName + ": " + max + "\n";
            }
        }
        return text;
    }
}
