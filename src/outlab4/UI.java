package outlab4;

import edu.princeton.cs.algs4.*;

import java.util.LinkedList;

/**
 * Class that groups user input functions
 * And handles the input given
 */
public class UI {
    public Suggestions suggestions;
    public TST<Integer> tst;

    public UI(Suggestions s, TST<Integer> t) {
       this.suggestions = s;
       this.tst = t;
    }

    /**
     * Print out words for the user to choose from
     * @param word misspelled word
     */
    public String chooseWord(String word, LinkedList<String> total) {
        StdOut.printf("\n%s: did you mean:\n", word); // printf makes formatting easier
        printFirstOptions(); // give the first 5 options to the user
        String option = StdIn.readLine();

        // search for more possibilities if the user doesn't enter a string
        if (option.equals("")) {
            suggestions.indefiniteCases(tst, total);
            StdOut.println("\nMoreOptions:\n");
            printOptions();
            option = StdIn.readLine();
        }

        return option;
    }

    /**
     * Prints all the words the user can chose from
     */
    private void printOptions() {
        for (String word : tst.keys()) {
            StdOut.println(word);
        }
        StdOut.print("\nEnter the word you want.\nWord: ");
    }

    /**
     * Only print out the first n options
     */
    private void printFirstOptions() {
        final int NUM = 5;
        int count = 0;
        for (String word : tst.keys()) {
            if (count > NUM)
                break;
            StdOut.println(word);
            count++;
        }
        StdOut.print("\nEnter word you want or hit return for none.\nWord: ");
    }
}
