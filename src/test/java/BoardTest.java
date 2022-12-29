import board.Board;
import board.Point;
import items.Food;
import items.Grass;
import items.Item;
import items.Robot;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BoardTest {

    private final Item[][] ITEMS = new Item[][]{
        {new Grass(), new Grass(), new Grass(), new Food(), new Grass(), new Grass(), new Grass(), new Grass()},
        {new Food(), new Grass(), new Grass(), new Robot(), new Grass(), new Grass(), new Grass(), new Food()}
    };

    @Test
    public void shouldBeAbleToFindNearCellsWithFood() {
        var board = new Board(8, 2, ITEMS, new Point(1, 3));
        assertThat(board.getNeighboursWithFood(), is(Set.of(new Point(0, 3), new Point(1, 0), new Point(1, 7))));
    }
}
