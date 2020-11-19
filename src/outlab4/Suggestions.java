package outlab4;

import edu.princeton.cs.algs4.*;

import java.util.LinkedList;

public class Suggestions {
    private final TST<Integer> dictionary; // dictionary of words
    private final Out output; // where the corrected document is saved

    /**
     * Creates list of correctly spelled words for the user to choose
     * @param words list of correctly spelled words
     * @param filePath path where the spell corrected document is put
     */
    public Suggestions(In words, String filePath) {
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
    public void checkDocument(In doc) {
        while (!doc.isEmpty()) {
            // gets the next word and make it lowercase
            String current = doc.readString().toLowerCase();
            // make suggestions for correct word and replace it
            if (!dictionary.contains(current)) {
                String rightWord = possibilities(current);
                output.print(rightWord + "\n");
            } else {
                output.print(current + "\n");
            }
        }
    }

    /**
     * provides suggestions for the misspelled word
     * @param word misspelled word
     * @return correctly spelled word chosen by user
     */
    public String possibilities(String word) {
        // this TST will track all the potential words
        TST<Integer> tst = new TST<>();
        // Puts words with 1 char removed from misspelled word in tst
        LinkedList<String> del = deleteCase(word, tst);
        // Put in words with 1 insertion needed
        LinkedList<String> add = insertCase(word, tst);
        // PUTS IN WORDS WITH 1 swap needed
        LinkedList<String> swap = swapCase(word, tst);

        // if there are no possibilities found perform more operations on before ignored strings
        if (tst.size() == 0) {
            indefiniteCases(tst, del, add, swap);
        }

        StdOut.printf("\n%s: did you mean:\n", word);
        printOptions(tst);
        String option = StdIn.readLine();
        // search for more possibilities if the user doesn't enter a string
        if (option.equals("")) {
           indefiniteCases(tst, del, add, swap);
           StdOut.println("\nMoreOptions:\n");
           printOptions(tst);
           option = StdIn.readLine();
        }
        return option;
    }

    private LinkedList<String> deleteCase(String word, TST<Integer> tst) {
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

    private LinkedList<String> insertCase(String word, TST<Integer> tst) {
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

    private LinkedList<String> swapCase(String word, TST<Integer> tst) {
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

    /**
     * takes care of cases where there needs to be multiple changes to find correct word
     * @param del list of 1 char delete possibilities
     * @param add  list of 1 char add possibilities
     * @param swap list of 1 char swap possibilities
     */
    private void indefiniteCases(TST<Integer> tst, LinkedList<String> del, LinkedList<String> add, LinkedList<String> swap) {
        // combine all the possibilities into one collection
        LinkedList<String> total = new LinkedList<>();
        total.addAll(del);
        total.addAll(add);
        total.addAll(swap);

        // perform all cases
        for (String a : total) {
            deleteCase(a, tst);
            insertCase(a, tst);
            swapCase(a, tst);
        }
    }

    /**
     * Prints what words the user can chose from
     * @param tst the list of potential words
     */
    private void printOptions(TST<Integer> tst) {
        for (String word : tst.keys()) {
            StdOut.println(word);
        }
        StdOut.print("\nEnter the word you want. Hit return if none.\nWord: ");
    }

}
