package board;

import items.Item;
import items.Food;
import items.Grass;
import items.Robot;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Board {
    private final int height;
    private final int width;
    private Item[][] board;
    private Robot robot;
    private Point robotLocation;

    public Board(int height, int width) {
        this.height = height;
        this.width = width;

        board = new Item[height][width];
        Arrays.stream(board).forEach(row -> Arrays.fill(row, new Grass()));

        populateWithFood(board);
        placeRobot(board);
    }

    private void populateWithFood(Item[][] board) {
        var S = width * height;
        var amountOfFood = S / 5;
        BoardExt.populate(board, new Food(), amountOfFood);
    }

    private void placeRobot(Item[][] board) {
        robot = new Robot();
        BoardExt.populate(board, robot,1);
        robotLocation = BoardExt.getLocationOfRobot(board);
    }

    public void move(Direction direction) {
        System.out.println("Energy BEFORE: " + robot.getEnergy());
        if (robot.getEnergy() == 0) {
            System.out.println("Run out of energy");
            return;
        }

        var initLocation = robotLocation;
        var newLocation = calculateNextMove(robotLocation, direction);

        if (board[newLocation.x()][newLocation.y()] instanceof Food) {
            robot.increaseEnergy(1);
        } else {
            robot.decreaseEnergy(1);
        }

        board[initLocation.x()][initLocation.y()] = new Grass();
        board[newLocation.x()][newLocation.y()] = robot;

        robotLocation = newLocation;

        System.out.println("Moving " + direction);
        System.out.println("Energy left: " + robot.getEnergy());
        System.out.println(this);
    }

    private Point calculateNextMove(Point location, Direction direction) {
        return switch (direction) {
            case UP -> moveVertically(location, -1);
            case DOWN -> moveVertically(location, 1);
            case LEFT -> moveHorizontally(location, -1);
            case RIGHT -> moveHorizontally(location, 1);
        };
    }

    private Point moveVertically(Point location, int steps) {
        return new Point(location.x() == 0 ? height + steps : (location.x() - 1) % height, location.y());
    }

    private Point moveHorizontally(Point location, int steps) {
        return new Point(location.x(), location.y() == 0 ? width + steps : (location.y() - 1) % width);
    }

    @Override
    public String toString() {
        return Arrays
                .stream(board)
                .map(row -> Arrays.stream(row)
                        .map(Object::toString)
                        .collect(Collectors.joining( "" )))
                .collect(Collectors.joining("\n"));
    }
}
