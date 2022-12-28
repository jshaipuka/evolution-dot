package evolution;

import board.Board;
import board.Direction;

public class BoardProxy {

    private Board board;

    public boolean isAlive() {
        return board.isAlive();
    }

    public void move(Direction direction) {
        board.move(direction);
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
