package outlab4;

import edu.princeton.cs.algs4.*;

public class Suggestions {
    // dictionary of words
    private final TST<Integer> dictionary;
    private final Out output;

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
            // gets the next word
            String word = doc.readString();
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
    public String possibilities(String word) {
        // this TST will track all the potential words
        TST<Integer> tst = new TST<>();

        // Puts words with 1 char removed from misspelled word in tst
        for (int i = 0; i < word.length(); i++) {
            // take out the char
            StringBuilder sb = new StringBuilder();
            sb.append(word);
            sb.deleteCharAt(i);
            String currentString = sb.toString();
            if (dictionary.contains(currentString))
                tst.put(currentString,0);
        }

        // PUTS IN WORDS WITH 1 swap needed
        for (int i = 0; i < word.length() - 1; i++) {
            for (int j = i+1; j < word.length(); j++) {
                // swap chars
                char[] chars = word.toCharArray();
                char temp = chars[i];
                chars[i] = chars[j];
                chars[j] = temp;

                String currentString = String.valueOf(chars);
                if (dictionary.contains(currentString))
                    tst.put(currentString, 0);
            }
        }

        // Put in words with 1 insertion needed
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
                if (dictionary.contains(current)) {
                    tst.put(current, 0);
                }
                // cleanup for next insert
                sb.deleteCharAt(i);
            }
        }

        StdOut.println("\n" + word + ": did you mean:\n");
        printOptions(tst);
        return StdIn.readLine();

        // nothing done if the user wants more options
    }

    private void printOptions(TST<Integer> tst) {
        for (String word : tst.keys()) {
            StdOut.println(word);
        }
        StdOut.print("\nEnter the word you want. Hit return if none.\nWord: ");
    }
}
