package board;

import items.Food;
import items.Grass;
import items.Item;
import items.Robot;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.floorMod;

public class Board {

    private static final int DELTA = 3;

    private final int height;
    private final int width;

    private final Item[][] board;
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

    public Board(int width, int height, Item[][] board, Point robotLocation) {
        this.width = width;
        this.height = height;
        this.board = board;
        this.robotLocation = robotLocation;
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
        if (robot.getEnergy() == 0) {
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
    }

    public boolean isAlive() {
        return this.robot.getEnergy() > 0;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Item[][] getCells() {
        return board;
    }

    public Set<Point> getNeighboursWithFood() {
        return IntStream.rangeClosed(robotLocation.x() - DELTA, robotLocation.x() + DELTA)
            .boxed()
            .flatMap(i -> IntStream.rangeClosed(robotLocation.y() - DELTA, robotLocation.y() + DELTA).mapToObj(j -> new Point(i, j)))
            .map(it -> calculateNextMove(robotLocation, it))
            .filter(it -> board[it.x()][it.y()] instanceof Food)
            .collect(Collectors.toUnmodifiableSet());
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
