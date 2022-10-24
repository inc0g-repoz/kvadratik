import java.awt.Canvas;

import javax.swing.JFrame;

public class Main {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        JFrame frame = new JFrame("Canvas Game");
        Canvas cvs = new ImageCanvas();
        frame.add(cvs);
        frame.pack();
        frame.setVisible(true);
    }

}
