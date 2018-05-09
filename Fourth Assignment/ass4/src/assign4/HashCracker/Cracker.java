package assign4.HashCracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Following class can encode given password
 * or decode given hash (with given max
 * length of the password) with using indicated
 * number of threads.
 */

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();	
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}

	/*
	    In following ArrayList I merge
	    all the passwords found by created threads
	    and after completing that by class's
	    toString method I print every entry of this
	    array
	 */
	private ArrayList <String> passwords;
	/*
	    Here's all the threads stored,
	    I need this array to join threads
	    and accumulate collected result from them.
	 */
	private Worker workers[];
	/*
	    Passed hash
	 */
	private byte[] hash;

    /**
     * Hash decoder thread
     */
	private class Worker extends Thread {
        /*
         * Here is stored passwords which have
         * hash value equal to given hash value
         */
		private ArrayList <String> threadPasswords;
		//Maximum length of password
		private int length;
		/*
		    Interval of first char given
		    by CHARS array's indices
		 */
		private int startInd;
		private int endInd;

        /**
         * Just stores passed arguments in the
         * instance variables
         * @param length
         * @param start
         * @param end
         */
		Worker (int length, int start, int end) {
			this.threadPasswords = new ArrayList<>();
			this.length = length;
			this.startInd = start;
			this.endInd = end;
		}

        /**
         * For each starting character from this interval
         * I call recursive function which checks all possible
         * values
         */
		@Override
		public void run() {
			for (int i = startInd; i < endInd; i ++) {
				recAns ("" + CHARS[i], length-1);
			}
		}


        /**
         * Recursively check all possible combinations of
         * password
         * @param curString
         * @param curLength
         */
		void recAns (String curString, int curLength) {
			byte bytes[] = generator(curString).getBytes();

			/*
			    Voalá, this hash (stored above)
			    is equal to the given hash.
			 */
			if (Arrays.equals(hash, bytes)) {
				threadPasswords.add (curString);
			}

			/*
			    Floor of this function, be wary,
			    flapjack octopus dwells here!
			 */
			if (curLength == 0) {
				return;
			}

            /*
                Recursive calls with shortened length
             */
			for (int i = 0; i < CHARS.length; i ++) {
				recAns(curString + CHARS[i], curLength-1);
			}
		}

	}

    /**
     *  This method initialises all the threads, giving them
     *  first character intervals and passing length variable
     *
     * @param length maximum length of the predicted password
     * @param nofThreads number of threads to run
     */
    private void initWorkers (int length, int nofThreads) {
        //Create an array of Worker class
        workers = new Worker[nofThreads];
        /*
            I decrease following variable's value
            in the for loop by 1 until
            "nChars%nofThreads" reaches 0.
            This lets me create proper Auswählen for the
            thread's intervals.
         */
        int nChars = CHARS.length;
        for (int i = 0; i < workers.length; i ++){
            workers[i] = new Worker(length, i*(nChars/nofThreads), (i+1)*(nChars/nofThreads) + (int)Math.signum(nChars%nofThreads));
            nChars -= (int)Math.signum(nChars%nofThreads);
        }
    }

    /**
     * Just starts all the threads
     */
	private void startWorkers () {
		for (int i = 0; i < workers.length; i ++) {
			workers[i].start();
		}
	}

    /**
     * Following method joins main thread with all
     * the other threads and merges their decoding output
     * with the current object's ArrayList
     */
	private void joinWorkers () {
		for (int i = 0; i < workers.length; i ++) {
			try {
				workers[i].join();
				passwords.addAll(workers[i].threadPasswords);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
				System.out.println("There was an InterruptedException in joinWorkers!");
			}
		}
	}

    /**
     * Constructor stores passed arguments into
     * instance variables or passed them to the other methods
     * @param hex hash to be decoded
     * @param length maximum length of predicted password
     * @param nofThreads
     */
	Cracker (String hex, int length, int nofThreads) {
		this.passwords = new ArrayList<>();
		this.hash = hex.getBytes();

		initWorkers (length, nofThreads);
		startWorkers ();
		joinWorkers ();
	}

    private static void crackerMode (String[] args) {
        Cracker cracker = new Cracker(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        System.out.println (cracker.toString());
        System.out.println("All done");

    }

    /**
     * Static generator method for generating
     * hash values for given input
     * @param input
     * @return
     */

    private static String generator (String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(input.getBytes());
            return hexToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    /**
     * Basic main method, nothing much,
     * exits immediatly if there passed
     * are incorrect number of arguments
     * @param args
     */
	public static void main(String[] args) {
		if (args.length == 1) {
			System.out.println(generator(args[0]));
			return;
		}
		if (args.length == 3) {
			crackerMode (args);
			return;
		}

		System.out.println("Please input correct number of arguments!");
	}

    /**
     * This toString method generates string (with stringBuilder)
     * from every entry of this object's passwords ArrayList
     * @return
     */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (String el:passwords) {
			stringBuilder.append(el + '\n');
		}
		return stringBuilder.toString();
	}

}
