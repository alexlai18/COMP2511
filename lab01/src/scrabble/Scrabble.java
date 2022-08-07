package scrabble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scrabble {

    private List<String> dictionary = new ArrayList<String>(Arrays.asList("ab", "abe", "able", "ad", "ae", "ae",
            "ah", "al", "ale", "at", "ate", "ba", "bad", "be", "be", "bead", "bed", "bra", "brad", "bread", "bred",
            "cabble", "cable", "ea", "ea", "eat", "eater", "ed", "ha", "hah", "hat", "hate", "hater", "hath", "he",
            "heat", "heater", "heath", "heather", "heathery", "het", "in", "io", "ion", "li", "lin", "lion", "on",
            "program", "ra", "rad", "re", "rea", "read", "red", "sa", "sat", "scabble", "scrabble", "se", "sea", "seat",
            "seathe", "set", "seth", "sh", "sha", "shat", "she", "shea", "sheat", "sheath", "sheathe", "sheather",
            "sheathery", "sheth", "st", "te"));

    private String word;

    public Scrabble (String s) {
        this.word = s;
    }

    
    public int score () {
        int score = 0;
        String input = this.word;
        List<String> visited = new ArrayList<String>();
        score = find_subwords(score, input, visited);
        return score;
    }

    public int find_subwords(int score, String word, List<String> visited) {
        if (word.length() < 2) {
            return score;
        } else {
            if (dictionary.contains(word) && !visited.contains(word)) {
                score += 1;
                visited.add(word);
                for (int i = 0; i < word.length(); i++) {
                    String new_word = (new StringBuilder(word)).deleteCharAt(i).toString();
                    score = find_subwords(score, new_word, visited);
                }
            }
            return score;
        }
    }
}