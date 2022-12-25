import board.Board;
import org.spiderland.Psh.GA;
import robocode.Robocode;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.spiderland.Psh.GA.GAWithParameters;
import static org.spiderland.Psh.Params.ReadFromFile;

public class Main {
    public static void main(String[] args) throws Exception {
        var board = new Board(7,15);
        System.out.println(board);

        var robocode = new Robocode(board, "(robot.moveleft robot.moveup)");
        var programResult = robocode.run();

        System.out.println("ProgramResult: " + programResult);


        final GA ga = GAWithParameters(ReadFromFile(getFileFromResource("RobocodeRobotEvolution.pushgp")));
        ga.Run();
    }

    private static File getFileFromResource(final String fileName) throws URISyntaxException {
        final ClassLoader classLoader = Main.class.getClassLoader();
        final URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }
    }
}

