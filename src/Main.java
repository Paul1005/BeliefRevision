import classes.BeliefRevision;

public class Main {

    public static void main(String[] args) {

        String k = "A, B, C";
        String phi = "!A";
        BeliefRevision beliefRevision = new BeliefRevision();

        System.out.println(beliefRevision.findSolution(k, phi));
    }
}
