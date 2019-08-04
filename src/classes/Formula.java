package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Formula {
    private String formulaString;
    private ArrayList<Character> terms;
    private ArrayList<Map<Character, Boolean>> solutions = new ArrayList<>();

    Formula(String formulaString, ArrayList<Character> terms, ArrayList<Map<Character, Boolean>> solutions) {
        this.formulaString = formulaString;
        this.terms = terms;
        this.solutions = solutions;
        Collections.sort(terms);
    }

    ArrayList<Character> getTerms() {
        return terms;
    }

    void setTerms(Character term) {
        this.terms.add(term);
    }

    String getFormulaString() {
        return formulaString;
    }

    ArrayList<Map<Character, Boolean>> getSolutions() {
        return solutions;
    }

    void setSolutions(ArrayList<Map<Character, Boolean>> termDictionary) {
        this.solutions = termDictionary;
    }
}
