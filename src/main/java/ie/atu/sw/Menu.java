// package imported
package ie.atu.sw;

// import library
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

// public class for menu
public class Menu {

    //  scanner creates the object of scanner class
    private static Scanner s;
    //
    private static boolean keepRunning = true;

    // public constructor for Menu
    public Menu() throws FileNotFoundException, InterruptedException {
        // initialize scanner in  constructor
        s = new Scanner(System.in);

        // Start initialized
        start();

    }

    /*
     * Flex Border for text
     */
    public static void flexBorder(String text) {
        int length = text.length();
        String border = "+" + "-".repeat(length + 2) + "+";

        System.out.println(border);
        System.out.println("| " + text + " |");
        System.out.println(border);
    }

    /*
     * Option showing list of features users can choose
     * - Embedded File
     * - Algorithms
     * - Enter word for Similarity Search
     * - Output File
     */


    private static void showOptions() {
        System.out.println("---------------------------------------------------");
        System.out.println("|*************************************************|");
        System.out.println("|***** Similarity Search with Word Embedding *****|");
        System.out.println("|*************************************************|");
        System.out.println("---------------------------------------------------");
        flexBorder("(1) Embedded File");
        flexBorder("(2) Algorithms ");
        flexBorder("(3) Enter word for Similarity Search");
        flexBorder("(4) Output File (defualt ./out.text)");
        flexBorder("(5) Exiting ");
    }

    /*
     * start Menu
     *  1 -> choice is Matrix will display words and vectors in the program.
     *  2 -> choice  is a number of  algorithms to perform on words.
     *  3 -> choice searches for the word and uses the algo to perform similiarity search.
     *  4 -> choice writing new file and save the results for end users
     */


    static void start() throws FileNotFoundException, InterruptedException {

        while (keepRunning) {
            showOptions();
            flexBorder("Enter your choice");
            int choice = s.nextInt();
            if (choice == 0) {
                keepRunning = false;
                flexBorder("Exiting..");
            }
            switch (choice) {
                case 1:
                    Matrix matrix = new Matrix();
                    matrix.showoptions();
                    break;
                case 2:
                    Algo algo = new Algo();
                    algo.start();
                    break;
                case 3:
                    Search search = new Search();
                    search.userKey();
                    break;
                case 4:
                    Outputs outputs = new Outputs();
                    outputs.start();
                    break;
                case 5:
                    keepRunning= false;



                default:
                    System.out.println("ERROR[]... Please try again");

            }



        }

    }

    /*
     * Display bar class for making progress on output from console
     */

    public static class DisplayBar {

        /* SOURCE - https://stackoverflow.com/questions/1001290/console-based-progress-in-java
         * @author - Mike Shauneu
         * Contributor - Alexander Shush
         *
         * I try to set up a progress bar on console but was not able to get this working.
         * I have searched for solution and never managed to get the answer.
         * I have left this in the sourced code I am curious to what I need to do to get this to work
         */


        static long startTime = System.currentTimeMillis();
        static int total = 100;
        static int current = 0;

        private static void progressBar() throws InterruptedException {
            while (current <= total) {
                long eta = current == 0 ? 0 :
                        (total - current) * (System.currentTimeMillis() - startTime) / current;

                String etaHms = current == 0 ? "N/A" :
                        String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(eta),
                                TimeUnit.MILLISECONDS.toMinutes(eta) % TimeUnit.HOURS.toMinutes(1), TimeUnit.MILLISECONDS.toSeconds(eta) % TimeUnit.MINUTES.toSeconds(1));

                StringBuilder string = new StringBuilder(140);
                int percent = (current * 100 / total);

                string.append('\r')
                        .append(String.format("%3d%% [", percent))
                        .append("=".repeat(Math.max(0, percent)))
                        .append('>')
                        .append(" ".repeat(Math.max(0, 100 - percent)))
                        .append(']')
                        .append(String.format(" %d/%d, ETA: %s", current, total, etaHms));

                System.out.print(string);


                current++;
                Thread.sleep(100);

                System.out.println();

            }

            /*
             * Like the output class I try to create class that listen to the console on runtime and displayed progress on task executed
             *
             */

            class CustomOutputStream extends OutputStream {
                final PrintStream originalStream;
                final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                public CustomOutputStream(PrintStream originalStream) {
                    this.originalStream = originalStream;
                }

                @Override
                public void write(int b) {
                    if (b == '\n') {
                        // Process the entire buffer content as a line
                        String line = buffer.toString().trim();
                        if (line.contains("completed")) {
                            current++;
                            try {
                                progressBar();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        buffer.reset();
                    } else {
                        buffer.write(b);
                    }
                    // Always write the output to the original stream as well
                    originalStream.write(b);
                }
            }
        }
    }

}
