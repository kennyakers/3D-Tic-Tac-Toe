/**
 * Kenny Akers
 * Mr. Paige
 * Homework #
 *
 */
public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        System.out.println(board.goalStates.size());
        for (Board.Goal goal : board.goalStates) {
            System.out.println("\n");
            goal.print();
        }
    }

}
