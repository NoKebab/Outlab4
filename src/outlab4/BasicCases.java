package outlab4;

import edu.princeton.cs.algs4.TST;

import java.util.LinkedList;

/**
 * Groups in basic case functions
 * These functions modify the string one char at a time
 * and then find if its in the dictionary
 * They also return a linked list of all the strings they examined
 * even those not contained in the dictionary
 * This allows multiple permutations to be made to the strings
 * in the hope of finding one in the dictionary
 */
public interface BasicCases {
    LinkedList<String> delete(String word, TST<Integer> tst);
    LinkedList<String> insert(String word, TST<Integer> tst);
    LinkedList<String> replace(String word, TST<Integer> tst);
    LinkedList<String> swap(String word, TST<Integer> tst);
}
