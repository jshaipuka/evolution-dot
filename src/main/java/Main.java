import board.Board;
import robocode.Robocode;

public class Main {
    public static void main(String[] args) throws Exception {
        var board = new Board(7,15);
        System.out.println(board);

        var robocode = new Robocode(board, "(robot.moveleft robot.moveup)");
        var programResult = robocode.run();

        System.out.println("ProgramResult: " + programResult);
    }
}

