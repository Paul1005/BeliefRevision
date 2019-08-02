package classes;

import java.util.ArrayList;

public class Formula {
    private String formulaString;
    private ArrayList<Character> terms;

    Formula(String formulaString, ArrayList<Character> terms) {
        this.formulaString = formulaString;
        this.terms = terms;
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
}
