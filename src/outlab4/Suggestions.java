package outlab4;

import edu.princeton.cs.algs4.*;
import java.util.LinkedList;

public class Suggestions {
    private final TST<Integer> dictionary; // dictionary of words
    private final Out output; // where the corrected document is saved
    public int distance = 1; // how many changes are needed to reach the current string or edit distance
    /**
     * Creates list of correctly spelled words for the user to choose
     *
     * @param words list of correctly spelled words
     * @param filePath path where the spell corrected document is put
     */
    public Suggestions(final In words, final String filePath) {
        dictionary = new TST<>();
        output = new Out(filePath);
        // fill up the dictionary with words
        while (!words.isEmpty()) {
            dictionary.put(words.readString(), 0);
        }
    }

    /**
     * Spell checks the given document and writes the corrected version to the output
     * Also provides suggestions for each misspelled word
     * @param doc text document to be spell checked
     */
    public void checkDocument(final In doc) {
        String[] words = doc.readAllStrings();
        for (String word : words) {
            distance = 1; // reset distance

            // make suggestions for correct word and replace it
            if (!dictionary.contains(word)) {
                String rightWord = potential(word);
                output.print(rightWord + "\n");
            } else {
                output.print(word + "\n");
            }
        }
    }

    /**
     * provides suggestions for the misspelled word
     * @param word misspelled word
     * @return correctly spelled word chosen by user
     */
    public String potential(final String word) {
        // this TST will track all the potential words
        // The Integer value tracks the distance from the original string
        TST<Integer> found = new TST<>();

        // combine the Linked Lists
        LinkedList<String> totalPossibilities = combinePossibilities(
                delete(word, found),
                insert(word, found),
                replace(word, found),
                swap(word, found));

        // each operation was performed so distance needs to be incremented
        distance++;

        // preemptively try advanced cases if no dictionary words where found already
        if (found.size() == 0) {
            indefiniteCases(found, totalPossibilities);
        }

        // Get and process user input
        UI ui = new UI(this);
        return ui.chooseWord(word, found, totalPossibilities);
    }

    // combine all the possible combinations into one found for convenience
    @SafeVarargs
    public final LinkedList<String> combinePossibilities(LinkedList<String>... potential) {
        LinkedList<String> total = new LinkedList<>();
        for (LinkedList<String> list : potential) {
            for (String string : list) {
                // make sure there are no duplicates of the same word
                if (!total.contains(string))
                    total.add(string);
            }
        }
        return total;
    }

    // BASIC CASES:

    /**
     * These functions modify the string one char at a time
     * and then find if its in the dictionary.
     * They also return a linked list of all the strings they examined
     * including those not contained in the dictionary.
     * This allows multiple permutations to be made to the strings
     * in the hope of finding one in the dictionary.
     */

    // deletes one char in the word and tests if it's in the dictionary
    private LinkedList<String> delete(String word, TST<Integer> found) {
        // Puts words with 1 char removed from misspelled word in found
        LinkedList<String> delPossibilities = new LinkedList<>();
        for (int i = 0; i < word.length(); i++) {
            // take out the char
            StringBuilder sb = new StringBuilder();
            sb.append(word);
            sb.deleteCharAt(i);

            String current = sb.toString();
            delPossibilities.add(current);

            // add to dictionary if it isn't already there
            // because we don't want to update the edit distance
            if (dictionary.contains(current) && !found.contains(current))
                found.put(current, distance);
        }
        return delPossibilities;
    }

    // inserts one char in the word and tests if it's in the dictionary
    private LinkedList<String> insert(String word, TST<Integer> found) {
        LinkedList<String> addPossibilities = new LinkedList<>();
        for (int i = 0; i < word.length() + 1; i++) {
            // adds each letter somewhere in the word
            StringBuilder sb = new StringBuilder();
            sb.append(word);
            // a = 97... z = 122
            for (int j = 97; j <= 122; j++) {
                // get ascii char
                char letter = (char) j;
                sb.insert(i, letter);
                String current = sb.toString();
                addPossibilities.add(current);

                // add to dictionary if it isn't already there
                // because we don't want to update the edit distance
                if (dictionary.contains(current) && !found.contains(current)) {
                    found.put(current, distance);
                }
                // cleanup for next insert
                sb.deleteCharAt(i);
            }
        }
        return addPossibilities;
    }

    // replaces one char in the word and tests if it's in the dictionary
    private LinkedList<String> replace(String word, TST<Integer> found) {
        LinkedList<String> replacePossibilities = new LinkedList<>();
        for (int i = 0; i < word.length(); i++) {
            // adds each letter somewhere in the word
            char[] chars = word.toCharArray();

            // a = 97... z = 122
            for (int j = 97; j <= 122; j++) {
                // get ascii char
                char letter = (char) j;
                chars[i] = letter;
                String current = String.valueOf(chars);
                replacePossibilities.add(current);

                // add to dictionary if it isn't already there
                // because we don't want to update the edit distance
                if (dictionary.contains(current)  && !found.contains(current)) {
                    found.put(current, distance);
                }
            }
        }
        return replacePossibilities;
    }

    // swaps two chars in the word and tests if it's in the dictionary
    private LinkedList<String> swap(String word, TST<Integer> found) {
        LinkedList<String> swapPossibilities = new LinkedList<>();
        for (int i = 0; i < word.length() - 1; i++) {
            for (int j = i + 1; j < word.length(); j++) {
                // swap chars
                char[] chars = word.toCharArray();
                char temp = chars[i];
                chars[i] = chars[j];
                chars[j] = temp;

                String current = String.valueOf(chars);
                swapPossibilities.add(current);

                // add to dictionary if it isn't already there
                // because we don't want to update the edit distance
                if (dictionary.contains(current) && !found.contains(current))
                    found.put(current, distance);
            }
        }
        return swapPossibilities;
    }

    // ADVANCED CASES i.e. multiple operations are needed

    /**
     * Takes care of cases where there needs to be multiple changes to find correct word.
     * Guarantees that at least one option is shown to the user.
     * @param found  the TST that contains all the found dictionary words
     * @param potential list of all possible combinations
     */
    public void indefiniteCases(TST<Integer> found, LinkedList<String> potential) {
        // base case: stop when a word is found
        if (found.size() > 0) {
            return;
        }

        // update distance
        distance++;

        LinkedList<String> p = new LinkedList<>();

        for (String a : potential) {
            // perform all cases and combine the possibilities
            LinkedList<String> dc = delete(a, found);
            LinkedList<String> ic = insert(a, found);
            LinkedList<String> rc = replace(a, found);
            LinkedList<String> sc = swap(a, found);
            // combine possibilities for next recursive call
            p.addAll(combinePossibilities(dc, ic, rc, sc));
        }
        // recursive call
        indefiniteCases(found, p);
    }

    /**
     * This is called only in the UI if the user wants more options.
     * Unfortunately, because of the bad time and space complexity...
     * this can take a very long time or even run out of memory
     * if the user keeps wanting more options.
     * @param found dictionary words found from changing the original misspelled word
     * @param potential every possible variation up to the current distance of the word
     * @return list of possibilities for the UI to call again if the user wants more options
     */
    public LinkedList<String> userIndefiniteCase(TST<Integer> found, LinkedList<String> potential) {
        // update distance
        distance++;
        LinkedList<String> p = new LinkedList<>();

        for (String a : potential) {
            // perform all cases and combine the possibilities
            LinkedList<String> dc = delete(a, found);
            LinkedList<String> ic = insert(a, found);
            LinkedList<String> rc = replace(a, found);
            LinkedList<String> sc = swap(a, found);
            // combine possibilities into a comprehensive list for next loop
            p.addAll(combinePossibilities(dc, ic, rc, sc));
        }

        return p;
    }
}