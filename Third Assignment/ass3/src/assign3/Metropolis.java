package assign3;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

/**
 * Just basic main class which runs the whole
 * Metropolis thing
 */

public class Metropolis {

    public static void main (String[] args) {
        MetropolisFrame window = new MetropolisFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}
