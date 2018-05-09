package assign4.WebLoader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Following class with help of WebWorker class
 * downloads data from websites indicated in
 * txt file either concurrently or with
 * single thread
 */

public class WebFrame extends JFrame {

    private static final String FILE_NAME = "links.txt";
    /*
        My first computer's screen resolution :(
        Requiescat in Pace Old Buddy :*
     */
    private static final int DEFAULT_WIDTH = 1024;
    private static final int DEFAULT_HEIGHT = 768;

    /*
        Just bunch of graphical objects
        for GUI
     */
    private static WebFrame window;
    private DefaultTableModel model;
    private JTable table;
    private JButton singleThreadedFetch;
    private JButton concurrentFetch;
    private JTextField workingThreadsField;
    public JLabel runningLabel;
    public JLabel completedLabel;
    private JLabel elapsedLabel;
    private JProgressBar progressBar;
    private JButton stopButton;

    /*
        Semaphore for running limited
        number of concurrent threads
     */
    private Semaphore bound;
    private Launcher launcher;
    private Date webFrameStartTime;

    /*
     * Counters of threads and done
     * works alongside with simple
     * object which works as lock
     */
    private Object countersLock;
    private int completedCnt;
    public int threadCnt;


    WebFrame(String title) {
        super(title);

        Box outerBox = new Box(BoxLayout.Y_AXIS);
        Box innerBox = new Box(BoxLayout.Y_AXIS);
        innerBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerBox.setMaximumSize(new Dimension(400, 300));

        model = new DefaultTableModel(new String[]{"url", "status"}, 0);
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);

        singleThreadedFetch = new JButton("Single Threaded Fetch");
        concurrentFetch = new JButton("Concurrent Fetch");
        workingThreadsField = new JTextField("4");
        runningLabel = new JLabel("Running: 0");
        completedLabel = new JLabel("Completed: 0");
        elapsedLabel = new JLabel("Elapsed: 0");
        progressBar = new JProgressBar();
        stopButton = new JButton("Stop");
        stopButton.setMaximumSize(new Dimension(200, 40));
        stopButton.setEnabled(false);

        innerBox.add(singleThreadedFetch);
        innerBox.add(Box.createRigidArea(new Dimension(0, 10)));
        innerBox.add(concurrentFetch);
        innerBox.add(Box.createRigidArea(new Dimension(0, 20)));
        innerBox.add(workingThreadsField);
        innerBox.add(Box.createRigidArea(new Dimension(0, 10)));
        innerBox.add(runningLabel);
        innerBox.add(completedLabel);
        innerBox.add(elapsedLabel);

        outerBox.add(scrollPane);
        outerBox.add(innerBox);
        outerBox.add(progressBar);
        outerBox.add(stopButton);


        this.add(outerBox);
        getInput ();
        addListenersToButtons ();
    }

    private void getInput () {
        try {
            Scanner input = new Scanner(new File(FILE_NAME));
            String line;
            for (int i = 0; input.hasNextLine(); i ++) {
                line = input.nextLine();
                model.addRow(new String[]{line, ""});
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Cant read file from " + FILE_NAME);
        }
    }

    private static void createGUI() {
        window = new WebFrame("WebLoader");
        window.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     * Toggle start buttons making them
     * either enabled or disabled
     */
    private void toggleStartButtons () {
        singleThreadedFetch.setEnabled(!singleThreadedFetch.isEnabled());
        concurrentFetch.setEnabled(!concurrentFetch.isEnabled());
    }

    /**
     * Toggle stop button making it
     * either enabled or disabled
     */
    private void toggleStopButton () {
        stopButton.setEnabled(!stopButton.isEnabled());
    }

    /**
     * Toggles all the buttons
     */
    private void toggleButtons () {
        toggleStartButtons ();
        toggleStopButton ();
    }

    /**
     * Resets everything on the gui. Except
     * list of links in the table
     */
    private void resetGUI () {
        toggleButtons();

        for (int i = 0; i < model.getRowCount(); i ++) {
            model.setValueAt("", i, 1);
        }
        completedLabel.setText("Completed: 0");
        elapsedLabel.setText("Elapsed: 0");
        progressBar.setValue(0);
        progressBar.setMaximum(model.getRowCount());
    }

    /**
     * Adding listeners to the fetch and stop buttons
     */
    private void addListenersToButtons () {
        singleThreadedFetch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchIsPressed (1);
            }
        });

        concurrentFetch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nOfThreads = Integer.parseInt(workingThreadsField.getText());
                if (nOfThreads <= 0) {
                    System.out.println("Please enter positive number of threads!");
                    return;
                }

                fetchIsPressed (nOfThreads);
            }
        });

        /**
         * Structure is created in a such way that
         * stop button only needs to interrupt
         * launcher thread. Rest threads will be
         * interrupted like dominoes ;)
         */
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launcher.interrupt();
            }
        });
    }

    /**
     * When single fetch or concurrent fetch
     * is pressed following function starts
     * launcher thread
     * @param threadCnt
     */
    private void fetchIsPressed (int threadCnt) {
        resetGUI();
        webFrameStartTime = new Date();
        launcher = new Launcher(threadCnt);
        launcher.start();
    }

    /**
     * Launcher class which operates on
     * creating limited concurrent WebWorkers
     */
    private class Launcher extends Thread{
        /*
         * Total number of websites to download
         * and array of WebWorker threads.
         * I need to store this array in order
         * to interrupt WebWorkers
         */
        private int numberOfWebsites;
        private WebWorker[] webWorkers;

        /**
         * Constructor stores passed variables into
         * this class's instance variables and creates
         * new lock and semaphore.
         * @param availableThreads
         */
        Launcher (int availableThreads) {
            this.numberOfWebsites = model.getRowCount();
            this.webWorkers = new WebWorker[numberOfWebsites];

            bound = new Semaphore(availableThreads);
            /*
                There is no need to create this lock
                every time new launcher is created, but
                FUCK IT!
             */
            countersLock = new Object();
            threadCnt = 0;
            completedCnt = 0;
        }

        @Override
        public void run() {
            //Update only thread counter
            updateCounters(1, 0);
            //Update GUI
            SwingUtilities.invokeLater(new LoaderMessage());
            int i;
            for (i = 0; i < numberOfWebsites && !isInterrupted(); i ++) {
                try {
                    bound.acquire();
                    updateCounters (1, 0);
                    webWorkers[i] = new WebWorker((String)model.getValueAt(i, 0), i, window);

                } catch (InterruptedException e) {
                    System.out.println("Launcher thread was interrupted!");
                    /*
                        When InterruptedException is thrown this means
                        that thread's flag is still not changed
                        (at least by that interrupt), so I use interrupt
                        on this thread set it's flag to interrupted
                     */
                    interrupt();
                    break;

                }
            }
            /*
                If thread's flag is set to
                interrupted we need to interrupt
                all produced workers
             */
            if (isInterrupted()) {
                interruptAllWorkers (i);
            }
            //Decrease only thread counter
            updateCounters(-1, 0);
            //Update GUI
            SwingUtilities.invokeLater(new LoaderMessage());
        }

        /**
         * Interrupt all of the produced threads.
         * Some of the threads might not be alive
         * but interrupting dead thread does noting but good ;)
         * @param ind
         */
        private void interruptAllWorkers (int ind) {
            for (int i = ind-1; i > -1; i --) {
                webWorkers[i].interrupt();
            }
        }
    }

    /**
     * Update guy with invokeLater.
     * Following function is called by WebWorker objects
     * passing appropriate status and rowNum arguments.
     * status sting might been
     * @param status value of corresponding status row
     * @param rowNum number of corresponding row
     */
    public void updateGUI (String status, int rowNum) {
        SwingUtilities.invokeLater(new WebWorkerUpdate(status, rowNum));
    }

    /**
     * Calling this function means that current fetch is completed
     * (either interrupted or downloaded all the websites).
     * This function updates elapsedLabel with appropriate content
     */
    private void completed () {
        Date webFrameEndTime = new Date();
        elapsedLabel.setText("Elapsed: " + ((float) (webFrameEndTime.getTime() - webFrameStartTime.getTime()) / 1000));
        toggleButtons();
    }

    /**
     * Atomically updates counters of the threads and
     * counters of the completed works
     * @param threadCntDiff
     * @param completedCntDiff
     */
    public void updateCounters (int threadCntDiff, int completedCntDiff) {
        synchronized (countersLock) {
            threadCnt += threadCntDiff;
            completedCnt += completedCntDiff;
            if (threadCnt == 0) {
                completed ();
            }
        }
    }

    /**
     * Object of this class is passed to Swing's invokeLater
     * whenever WebWorker asks to update the GUI
     */
    private class WebWorkerUpdate implements Runnable {
        String status;
        int rowNum;

        /**
         * Store arguments' value in instance variables
         * @param status
         * @param rowNum
         */
        WebWorkerUpdate (String status, int rowNum) {
            this.status = status;
            this.rowNum = rowNum;
        }

        /**
         * if status string is null this means that
         * this message is sent by WebWorker when it
         * have been started,
         * in other case it means that WebWorker have
         * produced status and we need to update the
         * Table
         */
        @Override
        public void run() {
            if (status == null) {
                runThreadStarted();
            } else {
                runThreadCompleted();
            }
        }


        private void runThreadCompleted () {
            runningLabel.setText("Running: " + threadCnt);
            completedLabel.setText("Completed: " + completedCnt);
            progressBar.setValue(completedCnt);
            model.setValueAt(status, rowNum, 1);
            bound.release();
        }

        private void runThreadStarted () {
            runningLabel.setText("Running: " + threadCnt);
        }
    }

    /**
     * Object of this class is passed to
     * Swing's invokeLater by the Loader
     * Object
     */
    private class LoaderMessage implements Runnable {
        @Override
        public void run() {
            runningLabel.setText("Running: " + threadCnt);
        }
    }


    /**
     * Basic main class which creates
     * GUI with invokeLater
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
