package outlab4;

import edu.princeton.cs.algs4.*;

public class SpellCheck {
    public static void main(String[] args) {
        final In WORDS = new In(args[0]);
        final In DOCUMENT = new In(args[1]);
        final String OUT_FILEPATH = "src/outlab4/correct.txt"; // where the corrected document is saved

        Suggestions suggestions = new Suggestions(WORDS, OUT_FILEPATH);
        suggestions.checkDocument(DOCUMENT);
        //suggestions.possibilities("batnederrs");
    }
}
