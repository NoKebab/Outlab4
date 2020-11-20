package outlab4;

import edu.princeton.cs.algs4.*;
import java.util.LinkedList;

/**
 * Class that groups user input functions
 * And handles the input given
 */
public class UI {
    public Suggestions suggestions;

    public UI(Suggestions s) {
       this.suggestions = s;
    }

    /**
     * Print out words for the user to choose from
     * @param word misspelled word
     * @param found found words
     * @param potential list of strings derived from the original misspelled word
     */
    public String chooseWord(String word, TST<Integer> found, LinkedList<String> potential) {
        String option = printOptions(found, word,1);
        // search for more possibilities if the user doesn't enter a string
        while (option.equals("n")) {
            StdOut.print("\nEnter word you want or 'n' for none: ");
            option = StdIn.readString();
            if (!option.equals("n"))
                return option;

            int baseSize = found.size();
            int originalDistance = suggestions.distance;

            potential = suggestions.userIndefiniteCase(found, potential);

            // return a blank string if no new options found
            if (found.size() == baseSize) {
                StdOut.println("Sorry... unable to find a reasonable suggestion.");
                return "";
            }
            StdOut.printf("\nMore Options for %s:\n", word);
            option = printOptions(found, null, originalDistance);
        }

        return option;
    }

    /**
     * Prints out all the options the user has
     * and gets input on every 5th element so the user
     * isn't overwhelmed with options.
     * @param found words close to the original
     * @param s current word, only used for printing what word the suggestions are for the first time this method is called
     * @param startingDistance guarantees the words the user already rejected don't appear again
     * @return the option the user chose if at least 5 elements where printed else returns "n" for none
     */
    private String printOptions(TST<Integer> found, String s, int startingDistance) {
        StdOut.printf("%d possibilities found\n", found.size());
        // only need to print out the word the first time this method is called
        if (s != null)
            StdOut.printf("%s: did you mean: \n", s);

        // prints smallest distance word to largest
        String option = "n";
        int n = startingDistance;
        while (n <= suggestions.distance) {
            int counter = 0;
            for (String word : found.keys()) {
                // only print words of a certain edit distance
                if (found.get(word) == n) {
                    counter++;
                    // only print this once a word of the distance was found
                    if (counter == 1) {
                        StdOut.printf("\nDistance %d:\n", n);
                    }
                    StdOut.println(word);
                }
                // stop every 5th element for user input
                if (counter % 5 == 0 && counter != 0) {
                     StdOut.print("\nEnter word you want or 'n' for none: ");
                     option = StdIn.readString();
                     // if the user selects an option, return it and end the function
                     if (!option.equals("n")) {
                         return option;
                     }
                }
            }
            n++;
        }
        StdOut.println("End of options.\n");
        return option;
    }

}
