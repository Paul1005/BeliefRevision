import classes.BeliefRevision;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        // Give user instructions
        System.out.println("Please enter K, separate different statements with a comma and a space, press enter when complete");
        String k = scanner.nextLine();
        System.out.println("Please enter Phi, press enter when complete");
        String phi = scanner.nextLine();
        //String k = "A, B, C || A, !C";
        //String phi = "!A";
        BeliefRevision beliefRevision = new BeliefRevision();

        System.out.println(beliefRevision.reviseBelief(k, phi));
    }
}
