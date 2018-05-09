package assign4.WebLoader;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Objects of WebWorkers class download
 * data from links and inform WebFrame class
 * to update GUI or counters
 */

public class WebWorker extends Thread {


    private String URL;
    private WebFrame webFrame;
    private Date startTime;
    private Date endTime;
    private long size;
    public int rowNum;
    public String status;

    /**
     * Constructor stores passed arguments'
     * values into the instance variables and
     * Starts the thread itself.
     * @param URL
     * @param rowNum
     * @param webFrame
     */
    WebWorker (String URL, int rowNum, WebFrame webFrame) {
        this.URL = URL;
        this.webFrame = webFrame;
        this.rowNum = rowNum;
        this.status = null;
        this.start();
    }

    /**
     * Before we start downloading
     * we need to store current time
     * and let GUI know that we have started
     * working
     */
    private void adjustBefore () {
        startTime = new Date();
        webFrame.updateGUI(null, -1);
    }

    /**
     * After we download data from the website,
     * We should update counters and GUI
     */
    private void adjustAfter () {
        webFrame.updateCounters (-1, 1);
        webFrame.updateGUI(status, rowNum);
    }

    @Override
    public void run() {
        adjustBefore ();
        download ();
        adjustAfter ();
    }

    /**
     * Following function calculates
     * current time, elapsed time and size of data and
     * produces generates successful status String from it
     */
    private void generateSuccessfulStatus () {
        endTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        status = formatter.format(endTime) + " " + ((long)endTime.getTime() - startTime.getTime())  + "ms " + size + " bytes";
    }

    /**
     * Following function just downloads
     * data from the website. It creates appropriate
     * status String.
     */
    private void download () {
        InputStream input = null;
        StringBuilder contents;
        try {
            URL url = new URL(URL);
            URLConnection connection = url.openConnection();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);

            connection.connect();
            input = connection.getInputStream();

            BufferedReader reader  = new BufferedReader(new InputStreamReader(input));

            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                contents.append(array, 0, len);
                Thread.sleep(100);
            }
            /*
                It's interrupted we should throw
                interruptedException, in order to
                jump to desired catch block
             */
            if (isInterrupted()) {
                throw new InterruptedException();
            }
            size = contents.toString().getBytes().length;
            generateSuccessfulStatus();
        }
        // Otherwise control jumps to a catch...
        catch(MalformedURLException ignored) {
            status = "err";
        }
        catch(InterruptedException exception) {
            status = "interrupted";
        }
        catch(IOException ignored) {
            status = "err";
        }
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try{
                if (input != null) input.close();
            }
            catch(IOException ignored) {}
        }
    }
	
}
