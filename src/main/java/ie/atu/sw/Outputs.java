// import package
package ie.atu.sw;

// import library
import java.util.*;
import java.io.*;


/*
* Output class that writes files as users prompt to type in the name on the file.
* their will be three type txt file, temp file, defualt txt.
*
 */
public class Outputs {

    // keep running for menu-driven program
    private static boolean keepRunning = true;

    // initialize Scanner tool for System input to console
    private static Scanner s;

    // Output constructor
    public Outputs() throws FileNotFoundException, InterruptedException {
        // const s
        s = new Scanner(System.in);

        // Menu start
        start();
    }

    // flex box border
    public static void flexBorder(String text) {
        int length = text.length();
        String border = "+" + "-".repeat(length + 2) + "+";
        System.out.println(border);
        System.out.println("| " + text + " |");
        System.out.println(border);
    }


    //  created Menu Stubs
    private static void showsOptions() {
        System.out.println("---------------------------------------------------");
        System.out.println("|*************************************************|");
        System.out.println("|*************** Config Output File   ************|");
        System.out.println("|*************************************************|");
        System.out.println("---------------------------------------------------");
        flexBorder("(1) Write results to {name file}.txt file ");
        flexBorder("(2) Write results to temp File");
        flexBorder("(3) Write results to ./out.txt file");

    }

    // Start menu prompting user to select write file
    static void start() throws FileNotFoundException, InterruptedException {
        while (keepRunning) {
            showsOptions();
            int choice = Integer.parseInt(s.next());
            switch (choice) {
                case 1 -> {
                    try {
                        txtFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
                case 2 -> {
                    try {
                        writeTempFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
                case 3 -> {
                    try {
                        defaultFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                default -> flexBorder("Error... Cannot write to File");
            }

                finish();

        }
    }

    // next method for menu asking for users input after choice what they want to do continue using this program or shut it down.
    private static void finish() throws FileNotFoundException, InterruptedException {
            flexBorder("The file has been created: ");
            flexBorder("Would you like to go back to go back to the main menu? Yes : No");

            String key = s.next().trim();
            if (key.equalsIgnoreCase("Yes")) {
                flexBorder("Sending you back to the main menu");
                Menu.start();
            } else if (key.equalsIgnoreCase("No")) {
                flexBorder("Exiting program... Goodbye");
                keepRunning = false;
            }

    }

    /*
    * Console Output class that read console output and stores it so the file method can write to the file users
    *  SOURCES - STACK OVERFLOW - https://stackoverflow.com/questions/8708342/redirect-console-output-to-string-in-java
    * @author - Bilesh Ganguly
    * @Contributor -  Mahnasjyoti Sharma
    *
    * I refactor the code to my specification needs
    *
    * I also used the Java API - https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/OutputStream.html#nullOutputStream()
    *
     */

    public static class ConsoleOutput {

        // initialized ByteArrayOutputStream
        private static ByteArrayOutputStream baos;

        // initialized PrintStream
        private static PrintStream printOut;

        private static PrintStream custom;

        // scanning set up for start methods
        private static boolean scanning;


        static {
                // Stream for System out
                printOut = System.out;

                // byte Array oOutput Stream
                baos = new ByteArrayOutputStream();

                // Output Steam combiner printout and Baos
                OutputStream outputStreamCombiner = new OutputStreamCombiner(printOut, baos);

                // custom to store new instance output Stream  Combiner
                custom = new PrintStream(outputStreamCombiner);

                // System set Out
                System.setOut(custom);
            }

            // method for getting Output and returning to a String
            public static String getOutput() {
                System.setOut(printOut);
                return baos.toString();
            }

            // Method start Scanning
            public static void start() {
                if (!scanning) {
                    System.setOut(custom);
                    scanning = true;
                }
            }

            // class OutputStreamCombiner extends to OutputStream
            public static class OutputStreamCombiner extends OutputStream {

            // Stream printOut
                private PrintStream printOut;
                private ByteArrayOutputStream baos;

                // Output Stream storing System output
                public OutputStreamCombiner(PrintStream printOut, ByteArrayOutputStream baos) {
                    this.printOut = printOut;
                    this.baos = baos;
                }

                /*
                * Override
                *  writes the bytes to Output stream.
                *
                 */
                @Override
                public void write(int b) throws IOException {
                    printOut.write(b);
                    baos.write(b);
                }

                /*
                * Override
                * Flushes the output  and forces the output to be written
                 */

                @Override
                public void flush() throws IOException {
                    printOut.flush();
                    baos.flush();
                }

                /*
                * Override
                * Closes this output any system resources
                 */

                @Override
                public void close() throws  IOException {
                    printOut.close();
                    baos.close();
                }

            }


    }


    // Text file method to ask for users prompt for file name using BuffferedWriter

    public static void txtFile() throws IOException {
        flexBorder("Enter the output file name: ");
        String fileName = s.next();
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".txt"));

        writer.write(ConsoleOutput.getOutput());


        writer.close();
    }


    // Temp file method to ask for users prompt for file name using BuffferedWriter
    public static void writeTempFile() throws IOException {
        flexBorder("Enter the output file name: ");
        String fileName = s.next();
        File tmpFile = File.createTempFile(fileName, ".tmp");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFile));

        writer.write(ConsoleOutput.getOutput());

        writer.close();

        BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }

    // Standard default file method to ask for users prompt for file name using BuffferedWriter

    public static void defaultFile() throws IOException {
        String fileName = "./out.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true ));

        writer.write(ConsoleOutput.getOutput());

        writer.close();
    }


}