package assign4.Bank;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Bank class alongside with Transaction class and
 * Account class reads transaction data from the
 * text file and runs transactions on concurrent
 * threads. It's conceived by message passing model
 * between threads which are realized by CountDownLatch
 * and BlockingQueue
 */

public class Bank {

    private static final int DEFAULT_BANK_DEPOSIT = 1000;
    private static final int DEFAULT_NUMBER_OF_ACCOUNTS = 20;

    /*
        Following variables values
        are passed as args
     */
    private String inputFile = "";
    private int nOfWorkers = 0;

    /*
        Just workers and accounts arrays
     */
    private Worker[] workers;
    private Account[] accounts;
    /*
        BlockingQueue structure for implementing
        concurrent message passing model
     */
    private ArrayBlockingQueue <Transaction> blockingQueue;
    /*
        Transaction which indicates that
        every thread should end.
    */
    private Transaction sentinelTransaction;
    /*
        CountDownLatch for joining the threads
     */
    private CountDownLatch countDownLatch;


    /**
     * This worker class starts working
     * when initialised and stops when
     * main thread informs it via BlockingQueue
     * to complete the work
     */
    private class Worker extends Thread {
        Worker (){
            this.start();
        }

        @Override
        public void run() {
            boolean ind = true;
            while (ind) {
                try {
                    Transaction curTransaction = blockingQueue.take();
                    /*
                        If current transaction is sentinelTransaction
                        we need to re-insert it into the BlockingQueue,
                        to inform other threads too, and stop work
                     */
                    if (curTransaction.equals(sentinelTransaction)) {
                        blockingQueue.put(sentinelTransaction);
                        ind = false;
                        continue;
                    }
                    /*
                        Since Account class's changeBalance method is
                        synchronised we just need to call them
                        without any external extra locks
                     */
                    accounts[curTransaction.fromID].changeBalance(-curTransaction.amount);
                    accounts[curTransaction.toID].changeBalance(curTransaction.amount);
                } catch (InterruptedException e) {
                    System.out.println("Worker was interrupted, it'll stop!");
                    return;
                }
            }
            /*
                Current thread have ended, decrease countdown
                for proper join
             */
            countDownLatch.countDown();
        }
    }

    /**
     * Just main function which does the only
     * thing, creates new Bank object and passes
     * it arguments
     * @param args
     */
    public static void main(String[] args) {
        Bank bank = new Bank(args);
        System.out.println(bank.toString());
    }

    /**
     * Following Constructor initialises
     * everything and calls readTransactions method
     * @param args
     */
    Bank (String[] args) {
        getArguments (args);

        /*
            Initialise instance variables
         */
        sentinelTransaction = new Transaction(-1, 0, 0);
        blockingQueue = new ArrayBlockingQueue<Transaction>(2*nOfWorkers);
        countDownLatch = new CountDownLatch(nOfWorkers);
        /*
            Initialise arrays and their
            elements as well
         */
        initAccounts ();
        initWorkers ();
        readTransactions ();
    }

    /**
     * Does the main work. Opens indicated text file
     * and reads transactions from it and adds these
     * transactions to the BlockingQueue along the way
     */
    private void readTransactions () {
        try {
            Scanner input = new Scanner(new File(inputFile));
            //Read lines untill EOF is reached
            while (input.hasNext()) {
                String[] ints = input.nextLine().split(" ");
                try {
                    //Insert current transaction into blockingQueue
                    Transaction curTransaction = new Transaction(ints);
                    blockingQueue.put(curTransaction);
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Couldn't read the line, skipping it!");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Couldn't read data from the file properly.");
        } catch (InterruptedException e) {
            System.out.println("Main thread was interrupted, there is no way this could happen!");
            System.out.println("Don't change my code!");
        }
        finally {
            /*
                No matter what happens we have to
                finish all the threads. Putting sentinelTransaction
                in it BlockingQueue will take care of that
             */
            try {
                blockingQueue.put (sentinelTransaction);
            } catch (InterruptedException e) {
                System.out.println("Main thread was interrupted, there is no way this could happen!");
                System.out.println("Don't change my code!");
            }
        }

        try {
            /*
                Wait for all the threads to complete
                working.
             */
            countDownLatch.await();
        } catch (InterruptedException e) {
            System.out.println("Main thread was interrupted, there is no way this could happen!");
            System.out.println("Don't change my code!");

        }
    }


    /**
     * Returns string with all account's info in it.
     * @return
     */
    @Override
    public String toString () {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < DEFAULT_NUMBER_OF_ACCOUNTS; i ++) {
            stringBuilder.append(accounts[i].toString() + "\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Initialises accounts
     */
    private void initAccounts () {
        accounts = new Account[DEFAULT_NUMBER_OF_ACCOUNTS];

        for (int i = 0; i <DEFAULT_NUMBER_OF_ACCOUNTS; i ++) {
            accounts[i] = new Account(DEFAULT_BANK_DEPOSIT, i);
        }
    }

    /**
     * Initialises worker threads
     */
    private void initWorkers () {
        workers = new Worker[nOfWorkers];

        for (int i = 0; i < nOfWorkers; i ++) {
            workers[i] = new Worker();
        }
    }

    /**
     * Extracts values from passed program arguments.
     * Exits if incorrect number of arguments have passed
     * @param args
     */
    private void getArguments (String[] args) {
        try {
            inputFile = args[0];
            nOfWorkers = Integer.parseInt(args[1]);
            if (args.length > 2) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("Please pass exactly 2 arguments, input file name (String) and number of workers (int)!");
            System.out.println("Program will exit now");
            System.exit(0);
        }
    }
}
