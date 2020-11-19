package outlab4;

import edu.princeton.cs.algs4.*;

import java.util.LinkedList;

public class Suggestions implements BasicCases {
    private final TST<Integer> dictionary; // dictionary of words
    private final Out output; // where the corrected document is saved

    /**
     * Creates list of correctly spelled words for the user to choose
     * @param words list of correctly spelled words
     * @param filePath path where the spell corrected document is put
     */
    public Suggestions(final In words, final String filePath) {
        dictionary = new TST<>();
        output = new Out(filePath);
        // fill up the dictionary with words
        while (!words.isEmpty()) {
            dictionary.put(words.readString(),0);
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
            // make suggestions for correct word and replace it
            if (!dictionary.contains(word)) {
                String rightWord = possibilities(word);
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
    public String possibilities(final String word) {
        // this TST will track all the potential words
        TST<Integer> tst = new TST<>();

        LinkedList<String> del = delete(word, tst); // Puts words with 1 char removed from misspelled word in tst
        LinkedList<String> add = insert(word, tst); // Put in words with 1 insertion needed
        LinkedList<String> rep = replace(word, tst); // Put in words with 1 replacement needed
        LinkedList<String> swap = swap(word, tst); // PUTS IN WORDS WITH 1 swap needed

        // combine the Linked Lists
        LinkedList<String> totalPossibilities = combinePossibilities(del, add, rep, swap);

        // preemptively try advanced cases if none where found already
        if (tst.size() == 0) {
           indefiniteCases(tst, totalPossibilities);
        }

        UI ui = new UI(this, tst);
        return ui.chooseWord(word, totalPossibilities);
    }

    @SafeVarargs
    // combine all the possible combinations into one list for convenience
    private LinkedList<String> combinePossibilities(LinkedList<String>... lists) {
        LinkedList<String> total = new LinkedList<>();
        for (LinkedList<String> list : lists) {
           total.addAll(list);
        }
        return total;
    }

    // BASIC CASES:

    // deletes one char in the word and tests if it's in the dictionary
    public LinkedList<String> delete(String word, TST<Integer> tst) {
        // Puts words with 1 char removed from misspelled word in tst
        LinkedList<String> delPossibilities = new LinkedList<>();
        for (int i = 0; i < word.length(); i++) {
            // take out the char
            StringBuilder sb = new StringBuilder();
            sb.append(word);
            sb.deleteCharAt(i);

            String current = sb.toString();
            delPossibilities.add(current);

            if (dictionary.contains(current))
                tst.put(current,0);
        }
        return delPossibilities;
    }

    // inserts one char in the word and tests if it's in the dictionary
    public LinkedList<String> insert(String word, TST<Integer> tst) {
        LinkedList<String> addPossibilities = new LinkedList<>();
        for (int i = 0; i < word.length() + 1; i++) {
            // adds each letter somewhere in the word
            StringBuilder sb = new StringBuilder();
            sb.append(word);
            // a = 97... z = 122
            for (int j = 97; j <= 122; j++) {
                // get ascii char
                char letter = (char)j;
                sb.insert(i, letter);
                String current = sb.toString();
                addPossibilities.add(current);

                if (dictionary.contains(current)) {
                    tst.put(current, 0);
                }
                // cleanup for next insert
                sb.deleteCharAt(i);
            }
        }
        return addPossibilities;
    }

    // replaces one char in the word and tests if it's in the dictionary
    public LinkedList<String> replace(String word, TST<Integer> tst) {
        LinkedList<String> replacePossibilities = new LinkedList<>();
        for (int i = 0; i < word.length(); i++) {
            // adds each letter somewhere in the word
            char[] chars = word.toCharArray();
            // a = 97... z = 122
            for (int j = 97; j <= 122; j++) {
                // get ascii char
                char letter = (char)j;
                chars[i] = letter;
                String current = String.valueOf(chars);
                replacePossibilities.add(current);

                if (dictionary.contains(current)) {
                    tst.put(current, 0);
                }
            }
        }
        return replacePossibilities;
    }

    // swaps two chars in the word and tests if it's in the dictionary
    public LinkedList<String> swap(String word, TST<Integer> tst) {
        LinkedList<String> swapPosibilities = new LinkedList<>();
        for (int i = 0; i < word.length() - 1; i++) {
            for (int j = i+1; j < word.length(); j++) {
                // swap chars
                char[] chars = word.toCharArray();
                char temp = chars[i];
                chars[i] = chars[j];
                chars[j] = temp;

                String current = String.valueOf(chars);
                swapPosibilities.add(current);

                if (dictionary.contains(current))
                    tst.put(current, 0);
            }
        }
        return swapPosibilities;
    }

    // ADVANCED CASES

    /**
     * takes care of cases where there needs to be multiple changes to find correct word
     * @param tst the Ternary Search Tree that contains all the found dictionary words
     * @param total list of all possible combinations
     */
    public void indefiniteCases(TST<Integer> tst, LinkedList<String> total) {
        // perform all cases
        for (String a : total) {
            delete(a, tst);
            insert(a, tst);
            replace(a, tst);
            swap(a, tst);
        }
    }

    private void recHelper() {

    }

}
