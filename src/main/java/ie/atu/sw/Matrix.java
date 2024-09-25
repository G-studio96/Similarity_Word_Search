//import packages
package ie.atu.sw;


// Import utility library
import java.io.FileNotFoundException;
import java.util.*;

import java.lang.*;



public class Matrix {

    // Scanner
    private static Scanner s;
    // import arrays Module to build a matrix menu for the embedded words
    static Ngrams ngrams;

    static {
        try {
            ngrams = new Ngrams();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // 1-dimensional Array with words
    private static String[] words;

    // 2-dimensional Array with vectors corresponding to the words
    private static Double[][] vectors;

    // create indices to index random words for matrix
    private static int[] indices = new int[10];
    private  static Random random = new Random();


    // Matrix constructor
    public Matrix() throws FileNotFoundException {

        // scanner for users input
        s = new Scanner(System.in);

        // set data from n-grams
        ngrams.setData();

        // words from text file
        words = ngrams.getWords();

        // vector from text file
        vectors = ngrams.getVectors();




    }

    /*
    * flex border for styling text
     */
    public static void flexBorder(String text) {
        int length = text.length();
        String border = "+" + "-".repeat(length + 2) + "+";

        System.out.println(border);
        System.out.println("| " + text + " |");
        System.out.println(border);
    }

    /*
    * printing out words from the array at random and displaying them across
    * corresponding vectors to the words will be displayed
    *
    * while loop for the words and for loop for the vectors have been used in these methods words and vectors with nop null values
     */

    static void wordMenu() throws FileNotFoundException {
        // pull ten words from String[]
        int count = 0;

        // initialize the count loop over indices length
        while (count < indices.length) {
            // Random next word  with null
            int rand = random.nextInt(words.length);
            if (words[rand] != null) {
                indices[count] =    rand;
                count++;
            }
        }

        // Print out the random indices
        for (int index : indices) {
            System.out.print(words[index] + " | ");

        }
        System.out.println();




    }

    static void vectorMenu() throws FileNotFoundException, InterruptedException {

        // Pull matching vectors from corresponding Vectors[][]
        for (int index : indices) {

            // check to  see if null are not there
            if (index < vectors.length && vectors[index] != null) {
                flexBorder("                                                                                ");
                // vectors assigned to an array and the corresponding index to the word
                Double[] vector = vectors[index];
                // for loop to irritate through vectors
                for (int j = 0; j < vector.length; j++) {
                    // print out vector inline
                    System.out.print(vector[j] + " | ");
                    // print out new line ever 10 vectors
                    if ((j + 1) % 10 == 0) {
                        System.out.println();
                    }
                }
                System.out.println();
            }
        }

        nextStep();

    }

    private static void nextStep() throws FileNotFoundException, InterruptedException {
        flexBorder("Would you like to use the algorithms to see the most and least common words? Yes or No ");
        String key = s.next();

        if (key.equalsIgnoreCase("Yes")) {
            new Algo();
        } else if (key.equalsIgnoreCase("No")) {
            flexBorder("Do you want to use word search? " +  " : " + " Select 1 for word search " + " : " + " 2 For writing to file " + " : " + "3 Go back to Main menu");
            int keyI = s.nextInt();
            if (keyI == 1) {
                new Search();
            } else if (keyI == 2) {
                new Outputs();
            } else {
                Menu.start();
                System.out.println("Returning you to the main");
            }
        }
    }


    /*
    * Option show word array inline and corresponding vectors
     */

    public void showoptions() throws FileNotFoundException, InterruptedException {
        System.out.println();
        System.out.println("|------------------------------------------------------------------------------------|");
        System.out.println("|************************************************************************************|");
        System.out.printf("|************************ Words count %s ****************************************|%n,", words.length);
        System.out.println("|____________________________________________________________________________________|");
        System.out.println();
        wordMenu();
        System.out.println();
        System.out.println("|____________________________________________________________________________________|");
        System.out.println("|************************************************************************************|");
        System.out.printf("|************************ Double[][] of %s word embedding ***********************|%n", words.length  );
        System.out.println("|____________________________________________________________________________________|");
        System.out.println();
        vectorMenu();
        System.out.println();

    }


}
