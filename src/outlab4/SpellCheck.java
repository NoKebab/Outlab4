package outlab4;

import edu.princeton.cs.algs4.*;

import java.util.LinkedList;

public class SpellCheck {
    public static void createDictionary(TST<String> dict, In in) {
        while (!in.isEmpty()) {
            dict.put(in.readString(),"");
        }
    }

    public static void spellCheck(In doc, Out out, TST<String> dict) {
        while (!doc.isEmpty()) {
            // gets the next word
            String word = doc.readString();
            // make suggestions for correct word and replace it
            if (!dict.contains(word)) {
                String rightWord = suggestions(word, dict);
                out.print(rightWord + " ");
            } else {
                out.print(word + " ");
            }
        }
    }

    private static String suggestions(String word, TST<String> dict) {
        StdOut.println(word + ": did you mean:\n");
        // use prefix to get suggestions
        String prefix = word.substring(0, word.length()-1);
        int counter = 1;
        for (String w : dict.keysWithPrefix(prefix)) {
            if (counter > 3)
                break;
            StdOut.println(counter + ". " + w);
            counter++;
        }
        StdOut.println("0. Something else");
        return "";
    }

    public static void main(String[] args) {
        // gets the dictionary and document from the command line
        In words = new In(args[0]);
        In document = new In(args[1]);

        // read from the dictionary and assign it to a TST
        TST<String> dictionary = new TST<String>();
        createDictionary(dictionary, words);

        Out out = new Out("src/outlab4/correct.txt");
        spellCheck(document, out, dictionary);
    }
}
