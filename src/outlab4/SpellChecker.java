/*
Peyton Dorsh, Bryan Rothe
CSCI 232 Outlab4
11/20/2020
 */

package outlab4;

import edu.princeton.cs.algs4.*;

/**
 * Driver Class
 * Takes in two arguments: list of words and document to be checked
 * Create suggestions class and then spell checks the document
 * and write the corrected version to "checked.txt"
 */
public class SpellChecker {
    public static void main(String[] args) {
        final In WORDS = new In(args[0]);
        final In DOCUMENT = new In(args[1]);
        final String OUT_FILEPATH = "src/outlab4/checked.txt"; // where the corrected document is saved

        Suggestions suggestions = new Suggestions(WORDS, OUT_FILEPATH);
        suggestions.checkDocument(DOCUMENT);
    }
}
