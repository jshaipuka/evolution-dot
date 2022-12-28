package board;

import items.Food;
import items.Grass;
import items.Item;
import items.Robot;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.Math.floorMod;

public class Board {
    private final int height;
    private final int width;
    private Item[][] board;
    private Robot robot;
    private Point robotLocation;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;

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
        BoardExt.populate(board, robot, 1);
        robotLocation = BoardExt.getLocationOfRobot(board);
    }

    public void move(Direction direction) {
//        System.out.println("Energy BEFORE: " + robot.getEnergy());
        if (robot.getEnergy() == 0) {
//            System.out.println("Run out of energy");
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

    public boolean isAlive() {
        return this.robot.getEnergy() > 0;
    }

    private Point calculateNextMove(Point location, Direction direction) {
        return switch (direction) {
            case UP -> calculateNextMove(location, new Point(-1, 0));
            case DOWN -> calculateNextMove(location, new Point(1, 0));
            case LEFT -> calculateNextMove(location, new Point(0, -1));
            case RIGHT -> calculateNextMove(location, new Point(0, 1));
        };
    }

    private Point calculateNextMove(Point location, Point diff) {
        return new Point(floorMod(location.x() + diff.x(), height), floorMod(location.y() + diff.y(), width));
    }

    public Item item(int i, int j) {
        return board[i][j];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return Arrays
                .stream(board)
                .map(row -> Arrays.stream(row)
                        .map(Object::toString)
                        .collect(Collectors.joining("")))
                .collect(Collectors.joining("\n"));
    }
}
