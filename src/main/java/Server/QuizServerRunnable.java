package Server;

import Server.LeaderBoard;
import QuizQuestion.MultipleChoiceQuestion;
import QuizQuestion.JeopardyCategory;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.Scanner;

/**
 * Runnable for QuizServer class
 */
public class QuizServerRunnable implements Runnable {
    final String DELIMITER = "~";
    Scanner in;
    PrintWriter out;
    String name;
    LeaderBoard boardObj;

    public QuizServerRunnable(LeaderBoard object, Scanner input, PrintWriter output) {
        in = input;
        out = output;
        boardObj = object;
        in.useDelimiter(DELIMITER);
    }

    /**
     * helper method that reads json string
     *
     * @return a JeopardyCategory class object that is constructed with the Json string acquired
     */
    public static JeopardyCategory readJsonFromUrl() {
        String url = "http://jservice.io/api/category?id=" + new Random().nextInt(18410);
        String jsonText = null;
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            jsonText = readIt(rd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JeopardyCategory(jsonText);
    }

    /**
     * helper method to read the Json string char by char
     *
     * @param rd reader object
     * @return returns the string after it has been concatenated
     * @throws IOException
     */
    private static String readIt(Reader rd) throws IOException {
        String sb = "";
        int cp;
        while ((cp = rd.read()) != -1) {
            sb += (char) cp;
        }
        return sb;
    }

    /**
     * the introductory part of the server welcome message and add player message
     */
    private void Introduction() {
        System.out.println("CONNECTED");
        out.write("MSG");
        out.write(DELIMITER);
        out.flush();
        out.write("================\n" +
                "|  Welcome to  |\n" +
                "|  Quiz Master |\n" +
                "================\n");
        out.write(DELIMITER);
        out.flush();
        out.write("NAME");
        out.write(DELIMITER);
        out.flush();
        out.write("Enter your name (then press <ENTER>): \n");
        out.write(DELIMITER);
        out.flush();
        name = in.next();
        boardObj.addPlayer(name);
    }

    /**
     * method to get the question using the helper methods used to read Json strings
     *
     * @return
     */
    public static Object[] getQuestion() {
        while (true) {  //loops until return statement is reached
            JeopardyCategory jCat = readJsonFromUrl();
            MultipleChoiceQuestion mcq = new MultipleChoiceQuestion();
            if (jCat.getQuestions().size() >= 4) {  //make sure there are 4 or more questions to have multiple choices
                Random rand = new Random();
                int randNum = rand.nextInt(4);
                mcq.setQuestionText(jCat.getQuestions().get(randNum));
                for (int i = 0; i < 4; i++) {
                    if (i == randNum) {
                        mcq.addChoice(jCat.getAnswers().get(i), true);
                    } else {
                        mcq.addChoice(jCat.getAnswers().get(i), false);
                    }
                }
                Object[] objects = {jCat, mcq};
                return objects;
            }
        }
    }

    /**
     * method to evaluate whether the answer entered by the user is correct or not
     *
     * @param mcq the MultipleChoiceQuestion object that stores the answer choices
     */
    public void evaluateAnswer(MultipleChoiceQuestion mcq) {
        if (mcq.evaluate(Integer.parseInt(in.next()))) {
            out.write("MSG~");
            out.write("Great Job! You got it Right!~");
            out.flush();
            boardObj.incrementStreak(name);
        } else {
            out.write("MSG~");
            out.write("Oops, the correct answer is: " +
                    mcq.correctNumber() + ": " + mcq.correctText() + "~");
            out.flush();
            boardObj.update(name, 0);
        }
    }

    /**
     * does introduction then loops the player in the game until the player wishes to
     * not continue further
     */
    @Override
    public void run() {
        Introduction();
        boolean playAgain = true;

        while (playAgain) {
            out.write("PLAYAGAIN");
            out.write(DELIMITER);
            out.flush();
            out.write("Ready for the next question [Y (continue) /N (to quit)]?:\n");
            out.write(DELIMITER);
            out.flush();
            if (in.next().equalsIgnoreCase("y")) {
                out.write("MSG");
                out.write(DELIMITER);
                out.flush();
                out.write(boardObj.prettyPrintTop3() + "\n" +
                        "Your current streak: " + boardObj.get(name) + " correct answers in a row\n" +
                        "Please wait while the next question is gathered.");
                out.write(DELIMITER);
                out.flush();
                Object[] objects = getQuestion();
                JeopardyCategory jCat = (JeopardyCategory) objects[0];
                MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) objects[1];
                out.write("QUESTION~");
                out.write("Question from Category: " +
                        jCat.getName() +
                        "\n========================================\n" +
                        mcq.getQuestion() +
                        "\n========================================\n" +
                        "0: " + jCat.getAnswers().get(0) + "\n" +
                        "1: " + jCat.getAnswers().get(1) + "\n" +
                        "2: " + jCat.getAnswers().get(2) + "\n" +
                        "3: " + jCat.getAnswers().get(3) + "\n" +
                        "========================================~");
                out.flush();
                evaluateAnswer(mcq);
            } else {
                playAgain = false;
                boardObj.delete(name);  //make sure leaderboard only contains active players
            }
        }
    }
}
