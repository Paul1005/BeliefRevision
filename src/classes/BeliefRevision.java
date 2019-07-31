package classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BeliefRevision {
    public ArrayList<Map<Character, Boolean>> solution = new ArrayList<>();
    private Map<Character, Boolean> termDictionary = new HashMap<>();
    private ArrayList<Character> terms = new ArrayList<>();
    private Map<Character, Boolean> contradictions = new HashMap<>();

    /*
   Verify whether the equation or set of equations is satisfiable
    */
    public void findSolutions(String[] formulas) {
        // gather all terms
        for (String formula : formulas) { // for each formula
            String[] splitFormula = formula.split(" "); // split the formula up by space
            for (String term : splitFormula) { // for each of these parts
                char[] chars = term.toCharArray(); // turn each term into a character array
                for (char character : chars) { // for each character, terms should either be a single letter or a letter with !
                    if (Character.isLetter(character)) { //if it's a letter
                        if (termDictionary.get(character) == null) { // add it to our dictionary and list of terms if it's not already there
                            termDictionary.put(character, false);
                            terms.add(character);
                        }
                    }
                }
            }
        }

        // generate number or rows in our truth table
        int numRows = (int) Math.pow(2, terms.size());

        // map for translating ints to booleans
        Map<Integer, Boolean> booleanMap = new HashMap<>();
        booleanMap.put(0, false);
        booleanMap.put(1, true);

        for (int i = 0; i < numRows; i++) {
            boolean isFormulaSatisfiable = true;

            for (int j = 0; j < terms.size(); j++) {
                termDictionary.replace(terms.get(j), booleanMap.get((i / (int) Math.pow(2, j)) % 2)); //set the row values for our terms
            }

            //do equations with current terms
            for (String formula : formulas) {
                String[] splitFormula = breakUpTerms(formula); // separate out the terms first
                isFormulaSatisfiable = isFormulaSatisfiable && evaluateFormula(splitFormula); // update whether the solution is correct for all formulas
            }

            if (isFormulaSatisfiable) {
                Map<Character, Boolean> temp = new HashMap<>();
                for (int j = 0; j < terms.size(); j++) {
                    temp.put(terms.get(j), termDictionary.get(terms.get(j)));
                }
                solution.add(temp); // add this to our solution if it works
            }
        }
    }

    public String findSolution(String k, String phi) {
        String[] splitK = k.split(", ");

        findSolutions(splitK);
        ArrayList<Map<Character, Boolean>> kSolution = solution;
        ArrayList<Character> kTerms = terms;
        System.out.println(kSolution.toString());
        termDictionary.clear();
        solution.clear();
        terms.clear();

        String[] phiArray = new String[1];

        phiArray[0] = phi;
        findSolutions(phiArray);

        ArrayList<Map<Character, Boolean>> phiSolution = solution;
        ArrayList<Character> phiTerms = terms;
        System.out.println(phiSolution.toString());

        checkIfContradiction(kSolution, kTerms, phiSolution, phiTerms);

        if (contradictions.size() > 0) {
            String solution = reviseBelief(splitK, phiArray, phiTerms);

            //check if it satisfies postulates?
            return solution;
        } else {
            return k + ", " + phi;
        }
    }

    private void checkIfContradiction(ArrayList<Map<Character, Boolean>> kSolutions, ArrayList<Character> kTerms, ArrayList<Map<Character, Boolean>> phiSolutions, ArrayList<Character> phiTerms) {
        boolean isContradiction = false;
        for (Map<Character, Boolean> kSolution : kSolutions) {
            for (Map<Character, Boolean> phiSolution : phiSolutions) {
                for (Character kTerm : kTerms) {
                    for (Character phiTerm : phiTerms) {
                        if (kTerm == phiTerm) {
                            if (kSolution.get(kTerm) != phiSolution.get(phiTerm)) {
                                contradictions.replace(phiTerm, phiSolution.get(phiTerm));
                            } else {
                                if (contradictions.get(phiTerm)) {
                                    contradictions.remove(phiTerm);
                                }
                            }
                        }
                    }
                }

            }
        }

    }

    private String reviseBelief(String[] k, String[] phi, ArrayList<Character> phiTerms) {

        String possibleSolution = "";
        // find a new set of beliefs
        ArrayList<String> markedSets = new ArrayList<>();
        for (String set : k) {
            boolean isMarked = false;
            String[] splitFormula = set.split(" "); // split the formula up by space
            for (String term : splitFormula) { // for each of these parts
                char[] chars = term.toCharArray(); // turn each term into a character array
                for (char character : chars) { // for each character, terms should either be a single letter or a letter with !
                    if (Character.isLetter(character)) { //if it's a letter
                        for (Character phiTerm : phiTerms) {
                            if (character == phiTerm) {
                                isMarked = true;
                            }
                        }
                    }
                }
            }
            if(isMarked) {
                markedSets.add(set);
            }
        }

        for(String markedSet: markedSets){
            String[] temp = new String[1];
            temp[0] = markedSet;
            findSolutions(temp);


            contradictions
        }

        //int distance = distance(possibleSolution, k);// check distance between new set of beliefs and the old one

        return possibleSolution;
    }

    private int distance(String possibleSolution, String k) {

        return 0;
    }

    /*
    Breaks up the formula into individual terms and operations by spaces and by brackets, so the following formula
    "((p || q) && r) ? t"
    will be turned into
    "((p || q) && r)" + "?" + "t"
     */
    private String[] breakUpTerms(String formula) {
        if (formula.contains("(") && formula.contains(")")) {
            ArrayList<Integer> startSeparations = new ArrayList<>();
            ArrayList<Integer> endSeparations = new ArrayList<>();
            int bracketCount = 0; // keeps track of how many open brackets we've got
            for (int i = 0; i < formula.length(); i++) { // go through the formula char by char
                if (formula.charAt(i) == '(') {
                    if (bracketCount == 0) { // if this is the first left bracket
                        if (i > 0) {
                            if (formula.charAt(i - 1) == '!') { // check if there's a ! before the bracket
                                startSeparations.add(i - 1); // add start separator at the !
                            } else {
                                startSeparations.add(i); // add start separator at bracket
                            }
                        } else {
                            startSeparations.add(i); // add start separator at bracket
                        }
                    }
                    bracketCount++; // increment to indicate we have one more open bracket
                } else if (formula.charAt(i) == ')') {
                    bracketCount--; // decrement to indicate we've closed the bracket
                    if (bracketCount == 0) { // if we've closed all the brackets
                        endSeparations.add(i); // add end separator at bracket
                    }
                }
            }

            // Below we use the separators to create our arrayList of formulas
            ArrayList<String> formulaArray = new ArrayList<>();
            for (int i = 0; i < startSeparations.size(); i++) {
                if (i == 0 && startSeparations.get(i) != 0) { // brackets not at the beginning
                    formulaArray.addAll(Arrays.asList(formula.substring(0, startSeparations.get(i) - 1).split(" "))); // separate and add first non-bracketed section
                }
                formulaArray.add(formula.substring(startSeparations.get(i), endSeparations.get(i) + 1)); // separate and add bracketed sections
                if (i + 1 < startSeparations.size()) { // if there are non bracketed sections after, separate and add them
                    formulaArray.addAll(Arrays.asList(formula.substring(endSeparations.get(i) + 2, startSeparations.get(i + 1) - 1).split(" ")));
                }
                if (i == startSeparations.size() - 1 && endSeparations.get(i) != formula.length() - 1) { // add last non-bracketed terms if there are any
                    formulaArray.addAll(Arrays.asList(formula.substring(endSeparations.get(i) + 2).split(" ")));
                }
            }

            String[] splitFormula = new String[formulaArray.size()];
            formulaArray.toArray(splitFormula); // copy array list to array

            return splitFormula;
        } else {
            return formula.split(" "); // if no brackets, just split it based on spaces
        }
    }

    /*
    Returns whether or not the formula resolves with the current inputs
     */
    private boolean evaluateFormula(String[] splitFormula) {
        boolean term1 = false;

        // see if the formula is only 1 term
        if (splitFormula.length == 1) {
            if (splitFormula[0].toCharArray()[0] == '!') {
                term1 = !termDictionary.get(splitFormula[0].toCharArray()[1]);
                //System.out.println(termDictionary.get(splitFormula[0].toCharArray()[1]));
            } else {
                term1 = termDictionary.get(splitFormula[0].toCharArray()[0]);
            }
            return term1;
        }

        boolean term2 = false;

        // check if the first term is bracketed
        if (splitFormula[0].endsWith(")")) {
            if (splitFormula[0].startsWith("(")) {
                term1 = evaluateFormula(breakUpTerms(splitFormula[0].substring(1, splitFormula[0].length() - 1))); // function calls itself using only the first term with the brackets stripped out
            } else if (splitFormula[0].startsWith("!(")) {
                term1 = !evaluateFormula(breakUpTerms(splitFormula[0].substring(2, splitFormula[0].length() - 1))); // same as above but with an !
            }
        } else {
            // see if first term has already been set
            switch (splitFormula[0]) {
                case "true":
                    term1 = true;
                    break;
                case "false":
                    term1 = false;
                    break;
                default: // if it hasn't, assign it to the appropriate term in the dictionary
                    if (splitFormula[0].toCharArray()[0] == '!') {
                        term1 = !termDictionary.get(splitFormula[0].toCharArray()[1]);
                    } else {
                        term1 = termDictionary.get(splitFormula[0].toCharArray()[0]);
                    }
                    break;
            }
        }

        // check if the second term is bracketed
        if (splitFormula[2].endsWith(")")) {
            if (splitFormula[2].startsWith("(")) {
                term2 = evaluateFormula(breakUpTerms(splitFormula[2].substring(1, splitFormula[2].length() - 1))); // function calls itself using only the second term with the brackets stripped out
            } else if (splitFormula[2].startsWith("!(")) {
                term2 = !evaluateFormula(breakUpTerms(splitFormula[2].substring(2, splitFormula[2].length() - 1))); // same as above but with an !
            }
        } else { // if it isn't, assign it to the appropriate term in the dictionary
            if (splitFormula[2].toCharArray()[0] == '!') {
                term2 = !termDictionary.get(splitFormula[2].toCharArray()[1]);
            } else {
                term2 = termDictionary.get(splitFormula[2].toCharArray()[0]);
            }
        }

        boolean answer = false;

        // set result of first part of equation
        switch (splitFormula[1]) {
            case "||": // OR
                answer = term1 || term2;
                break;
            case "&&": // AND
                answer = term1 && term2;
                break;
            case "?": // CONDITIONAL
                answer = !(term1 && !term2);
                break;
            case "??": // BICONDITIONAL
                answer = term1 == term2;
                break;
        }

        // if there are more terms, function calls itself
        if (splitFormula.length > 3) {
            String[] newSplitFormula = new String[splitFormula.length - 2]; // create a new array whose length is 2 less than the original

            // set first part of equation to true or false depending on the outcome of the first 2 terms
            if (answer) {
                newSplitFormula[0] = "true";
            } else {
                newSplitFormula[0] = "false";
            }

            // copy remainder of old formula to new one
            System.arraycopy(splitFormula, 3, newSplitFormula, 1, splitFormula.length - 3);

            return evaluateFormula(newSplitFormula);
        } else {
            return answer;
        }
    }
}
