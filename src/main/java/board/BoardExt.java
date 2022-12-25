package board;

import items.Item;
import items.Grass;
import items.Robot;

import java.util.Random;

public class BoardExt {
    private static final Random random = new Random();

    public static <T> T[][] populate(T[][] board, T item, int amount){
        var i = 0;
        while (i < amount) {
            var point = getRandomCoordinate(board.length, board[0].length);
            if (board[point.x()][point.y()] instanceof Grass) {
                board[point.x()][point.y()] = item;
                i++;
            }
        }

        return board;
    }

    public static Point getRandomCoordinate(int height, int width) {
        int x = random.nextInt(height);
        int y = random.nextInt(width);
        return new Point(x, y);
    }

    public static Point getLocationOfRobot(Item[][] board){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] instanceof Robot) {
                    return new Point(i, j);
                }
            }
        }
        return new Point(0,0);
    }
}
