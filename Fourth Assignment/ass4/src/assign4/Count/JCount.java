package assign4.Count;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is the implementation of the
 * JCount class. Following class's main function
 * creates one JPanel object and adds
 * several JCount objects in it.
 *
 * Each JCount object can create only one WorkerThread
 * object which handles the counting.
 */

public class JCount extends JPanel {


    //GUI's Constants
    private static final int NUMBER_OF_COUNTERS = 3;
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 700;

    //Thread's Constants
    private static final int PAUSE = 10000;
    private static final int SLEEP_DURATION = 100;

    //Program's one and only JFrame object
    private static JFrame window;

    /*
        Counter's worker thread.
        Please have in mind that this is not a static object.
        Every JCount object has one worker thread in it.
     */
    private WorkerThread worker;

    /*
        GUI's components
        for each JCount object
     */
    private JTextField cntTextField;
    private JLabel cntLabel;
    private JButton cntStart;
    private JButton cntStop;

    /**
     * Following constructor paints
     * JCount's GUI objects
     * Since it is called from Swing's invokeLater
     * I tried to keep it as minimal as I could.
     */

    JCount () {
        /*
            outerBox is the container of all JCount's
            GUI Objects. It keeps them placed on Y axis
         */
        Box outerBox = new Box(BoxLayout.Y_AXIS);
        /*
            innerBox is the container of Start cntStart
            and cntStop buttons. It keeps them aligned
            by X axis
         */
        Box innerBox = new Box(BoxLayout.X_AXIS);

        //Just basic GUI Object initialisation
        cntTextField = new JTextField("1000000000");
        cntLabel = new JLabel("0");
        cntStart = new JButton("Start");
        cntStop = new JButton("Stop");

        addButtonListeners ();

        /*
            Add everything to the boxes alongside
            with several rigid areas to keep everything
            "fine and dandy".
            Carlin we miss you ;(
         */
        innerBox.add(cntStart);
        innerBox.add(Box.createRigidArea(new Dimension (20, 0)));
        innerBox.add(cntStop);

        outerBox.add(cntTextField);
        outerBox.add(cntLabel);
        outerBox.add(innerBox);

        /*
            Add outerBox to the JCount (JPlane)
            with rigid area to keep it nicely
            separated from the other JCounts
         */
        this.add (outerBox);
        this.add(Box.createRigidArea(new Dimension(0,40)));
    }

    /**
     * Adds listeners to the
     * cntStart and cntStop buttons.
     */
    private void addButtonListeners () {
        cntStart.addActionListener(new startButtonListener());
        cntStop.addActionListener(new stopButtonListener());
    }

    /**
     * ActionListener for start button.
     */
    private class startButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            /*
                Every time start is clicked I
                click stop button to interrupt
                running thread (if there is one).
             */
            cntStop.doClick();
            //Extract count
            int countTo = Integer.parseInt(cntTextField.getText());
            /*
                Create new worker thread. Thread can't be restarted
                once it finished (sorry Gautama). So Even if there was
                a past thread which was stopped by the above doClick
                I still need to create new worker thread.
                Have in mind that constructor itself calls thread's
                start method
             */
            worker = new WorkerThread(countTo);
        }
    }

    /**
     * ActionListener for stop button
     */
    private class stopButtonListener implements ActionListener  {
        @Override
        public void actionPerformed(ActionEvent e) {
            /*
                If stop button is clicked before start button
                worker won't be initialised, if that's the case
                I should just pass.
                Same for dead threads, leave little dead guy alone...
             */
            if (worker != null && worker.isAlive()) {
                worker.interrupt();
                cntLabel.setText("0");
            }
        }
    }

    /**
     * Counter thread
     */
    private class WorkerThread extends Thread {
        //Where to stop counting
        private int countTo;

        /**
         * Creates and starts thread
         * @param newCountTo
         */
        WorkerThread (int newCountTo) {
            this.countTo = newCountTo;
            this.start ();
        }

        @Override
        public void run() {
            /*
                We should stop thread if it's interrupted or it's maximum count limit is reached.
             */
            for (int i = 1; i <= countTo && !isInterrupted(); i ++) {
                if (i%PAUSE == 0) {
                    try {
                        /*
                            New runnable thread can't access
                            outside variable unless it's final
                         */
                        final int curCnt = i;
                        sleep (SLEEP_DURATION);
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                cntLabel.setText("" + curCnt);
                            }
                        });
                    } catch (InterruptedException e) {
                        System.out.println("WorkerThread was interrupted during sleep, ending counter");
                        break;
                    }
                }
            }
        }
    }

    /**
     * Creates whole program's GUI by creating
     * new JFrame and calling produceBoxFromJCounts
     * method
     */
    private static void createGUI () {
        window = new JFrame ("The Count");
        window.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.add(produceBoxFromJCounts ());
    }

    /**
     * Creates and adds JCounts to the Box object
     * which is added to the JFrame object in the
     * CreateGUI function
     * @return
     */
    private static Box produceBoxFromJCounts () {
        Box box = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < NUMBER_OF_COUNTERS; i ++) {
            box.add(new JCount());
        }
        return box;
    }

    /**
     * Nothing much in the main function
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
        });
    }
}
