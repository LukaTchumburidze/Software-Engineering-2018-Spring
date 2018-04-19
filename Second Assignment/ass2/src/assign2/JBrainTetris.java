package assign2;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class JBrainTetris extends JTetris {
    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    private DefaultBrain myBrain;
    private JCheckBox brainMode;
    private JSlider slider;
    private JLabel status;

    Brain.Move mv = null;
    int myCnt = -1;

    JBrainTetris(int pixels) {
        super(pixels);
        myBrain = new DefaultBrain();
    }


    public static void main(String[] args) {
        // Set GUI Look And Feel Boilerplate.
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JBrainTetris tetris = new JBrainTetris(16);
        JFrame frame = JBrainTetris.createFrame(tetris);
        frame.setVisible(true);
    }

    @Override
    public JComponent createControlPanel () {
        JComponent panel = super.createControlPanel();
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        panel.add(brainMode);


        panel.add(new JLabel("Adversary:"));
        slider = new JSlider(0, 100, 0);
        slider.setPreferredSize(new Dimension(100,15));
        panel.add(slider);
        status = new JLabel("OK");
        panel.add(status);

        return panel;
    }

    private Piece getWorstPiece () {
        double maxVal = -1;
        Piece worstPiece = null;
        for (int i = 0; i < pieces.length; i ++) {
            if (myBrain.bestMove(board, pieces[i], HEIGHT, null) == null) {
                worstPiece = pieces[i];
                break;
            }
            double curScore = myBrain.bestMove(board, pieces[i], HEIGHT, null).score;
            if (curScore > maxVal) {
                worstPiece = pieces[i];
                maxVal = curScore;
            }
        }
        status.setText("*OK*");
        return worstPiece;
    }

    @Override
    public Piece pickNextPiece () {
        if (Math.abs(random.nextInt())%99+1 >= slider.getValue()) {
            status.setText("OK");
            return super.pickNextPiece();
        } else {
            Piece worstPiece = getWorstPiece ();
            return worstPiece;
        }
    }

    @Override
    public void tick (int verb) {
        if (brainMode.isSelected()) {
            if (verb != DOWN) {
                super.tick(verb);
            } else {
                if (count != myCnt) {
                    board.undo();
                    myCnt = count;
                    mv = myBrain.bestMove(super.board, super.currentPiece, HEIGHT, mv);
                }


                if (mv == null) {
                    super.tick(verb);
                    return;
                }
                if (!mv.piece.equals(super.currentPiece)) {
                    super.tick(ROTATE);
                }


                int xDiff = (int)Math.signum(currentX - mv.x);
                switch (xDiff) {
                    case 1:
                        super.tick(LEFT);
                        break;
                    case -1:
                        super.tick(RIGHT);
                        break;
                }
            }
        }
        super.tick(verb);
    }
}
