package QuizQuestion;

import java.util.*;

/**
 * a class to handle and store questions and answers
 */
public class MultipleChoiceQuestion {
    String question;
    Map<String, Boolean> choicesText;   //a map that contains choices in their text forms and whether they are correct or not
    List<Boolean> choicesNum;   //a list that contains choices in number forms(index) and whether they are correct or not

    public MultipleChoiceQuestion() {
        question = "";
        choicesNum = new ArrayList<>();
        choicesText = new HashMap<>();
    }

    /**
     * set the text for the question
     *
     * @param text: the text to use for the question
     **/
    public void setQuestionText(String text) {
        question = text;
    }

    /**
     * method to fetch the question text
     *
     * @return the question in String
     */
    public String getQuestion() {
        return question;
    }

    /**
     * method to fetch the the list containing the choices in number form
     * and whether each number is correct or not
     *
     * @return the list of boolean values where its indexes are the choice numbers
     */
    public List<Boolean> getChoicesNum() {
        return choicesNum;
    }


    /**
     * Add a choice for this multiple choice question
     *
     * @param choice  the text associated with this choice (the answer text)
     * @param correct whether this answer is correct or not
     **/
    public void addChoice(String choice, boolean correct) {
        if (choicesNum.size() < 4) {
            choicesText.put(choice, correct);
            choicesNum.add(correct);
        }
    }


    /**
     * Return the text of the correct answer
     *
     * @return the Test of the correct answer
     */
    public String correctText() {
        for (String str : choicesText.keySet()) {
            if (choicesText.getOrDefault(str, false)) {
                return str;
            }
        }
        return "";
    }

    /**
     * method to fetch the number of the correct answer
     *
     * @return the number of the correct answer
     */
    public int correctNumber() {
        for (int i = 0; i < choicesNum.size(); i++) {
            if (choicesNum.get(i) == true) {
                return i;
            }
        }
        return 0;
    }


    /**
     * Determine if the given guess is correct or note
     *
     * @param guess the guess to evaluate
     * @return true if guess matches to the solution
     * false otherwise
     **/
    public boolean evaluate(int guess) {
        if (choicesNum.get(guess)) {
            return true;
        }
        return false;
    }


    /**
     * Convert this multiple choice question into large
     * string containing the question and all possible solutions
     *
     * @return a String representation of the multiple choice question
     */
    public String toString() {
        if (question.isEmpty())
            return "No questions are loaded";
        String str = question;
        List<String> strList = new ArrayList<>(choicesText.keySet());
        for (int i = 0; i < 4; i++) {
            str += "\n" + i + ": " + strList.get(i);
        }
        return str;
    }

    public static void main(String[] args) {
        MultipleChoiceQuestion mcq = new MultipleChoiceQuestion();
        Scanner in = new Scanner(System.in);
        System.out.println("please enter the question you want to set");
        mcq.setQuestionText(in.nextLine());
        System.out.println("Please enter the 4 choices 1 line at a time");
        for (int i = 0; i < 4; i++) {
            System.out.println("please enter the choice");
            String choice = in.nextLine();
            System.out.println("please enter whether its true or not meaning whether its correct or not");
            boolean correct = Boolean.parseBoolean(in.nextLine());
            mcq.addChoice(choice, correct);
        }
        System.out.println(mcq);
        if (mcq.evaluate(in.nextInt()))
            System.out.println("correct!!");
        else
            System.out.println("wrong");
    }
}
