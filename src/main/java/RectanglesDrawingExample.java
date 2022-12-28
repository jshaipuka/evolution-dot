import javax.swing.*;
import java.awt.*;

public class RectanglesDrawingExample extends JFrame {
    private static final int D_W = 1500;
    private static final int D_H = 200;
    private static final int CART_WIDTH = 50;
    private static final int CART_HEIGHT = 50;

    private final int y = 100;

    private final State state;

    DrawPanel drawPanel = new DrawPanel();

    public RectanglesDrawingExample(final State state) {
        this.state = state;
        add(drawPanel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void render() {
        drawPanel.repaint();
    }

    private class DrawPanel extends JPanel {

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect((int) (state.position * 500) + 750, y, CART_WIDTH, CART_HEIGHT);
            g.setColor(Color.GRAY);
            g.fillRect(0, y + CART_HEIGHT, D_W, 10);
        }

        public Dimension getPreferredSize() {
            return new Dimension(D_W, D_H);
        }
    }

    public static void main(String[] args) {
        final State state = new State();
        EventQueue.invokeLater(() -> {
            final RectanglesDrawingExample panel = new RectanglesDrawingExample(state);
            Thread thread = new Thread(() -> {
                while (true) {
                    panel.repaint();
                    try {
                        state.position += 0.01;
                        Thread.sleep(20);
                    } catch (Exception ignored) {

                    }
                }
            });
            thread.start();
        });
    }
}