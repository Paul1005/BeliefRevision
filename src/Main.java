import classes.BeliefRevision;

public class Main {

    public static void main(String[] args) {

        String k = "A, B, C";
        String phi = "!A && !B";
        BeliefRevision beliefRevision = new BeliefRevision();

        System.out.println(beliefRevision.reviseBelief(k, phi));
    }
}
