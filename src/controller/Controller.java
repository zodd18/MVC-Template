package controller;

import view.Window;

import java.awt.*;
import java.awt.event.*;

public class Controller {

    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0);
    private long WAIT_TIME = 5L;
    private int PIXEL_SIZE = 1;

    private Dimension size;
    private Window window;

    private Thread updater;

    public Controller(Dimension size) {
        this.size = size;

        configureWindow();

        createUpdater(); // This creates a thread which will be updating the program each 10 ms
    }

    // -------------------- <Graphical things> --------------------

    private void fillBackground() {
        Graphics2D g = window.getImgGraphics();
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, (int) size.getWidth(), (int) size.getHeight());
    }

    private void render() {
        Graphics2D g = window.getImgGraphics();
        fillBackground();

        window.refresh();
    }

    // Used to place the pixels in a non-visible grid (set by pixel size)
    private Point getFixedPosition(Point position) {
        int x = (int) position.getX();
        int y = (int) position.getY();
        int fixedX = x - (x % PIXEL_SIZE);
        int fixedY = y - (y % PIXEL_SIZE);

        return new Point(fixedX, fixedY);
    }

    private void zoomOut() {
        PIXEL_SIZE = PIXEL_SIZE == 1 ? 1 : PIXEL_SIZE - 1;
    }

    private void zoomIn() {
        PIXEL_SIZE += 1;
    }

    // -------------------- </> --------------------

    public void createUpdater() {
        Runnable r1 = () -> {
            try {
                while (true) {
                    update();
                    render();
                    Thread.sleep(WAIT_TIME);

                }
            } catch (InterruptedException iex) {}
        };
        updater = new Thread(r1);
        updater.run();
    }

    private void update() {
        // This method will be called each 'WAIT_TIME' ms by the secondary thread
    }

    public void configureWindow() {
        window = new Window(size);

        fillBackground();

        window.getImgLabel().addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
                switch (mouseWheelEvent.getWheelRotation()) {
                    case 1: zoomOut(); break; // DOWN
                    case -1: zoomIn(); break; // UP
                    default: break;
                }
            }
        });

        window.getImgLabel().addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                Point position = mouseEvent.getPoint();
                System.out.printf("[MOUSE CLICK]: (%d, %d)\n", position.x, position.y);
            }

            @Override
            public void mouseClicked(MouseEvent mouseEvent) { }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) { }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) { }

            @Override
            public void mouseExited(MouseEvent mouseEvent) { }

        });
    }
}
