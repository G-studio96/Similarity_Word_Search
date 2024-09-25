// import package module
package ie.atu.sw;

// import libraries
import java.io.FileNotFoundException;
import java.util.*;

import java.lang.*;



// create a class method Search design to search the arrays for similarity words to users input
public class Search {

    // load utility scanner for users input
    private static Scanner s;

    // load Arrays into search class
    static Ngrams ngrams;

    static {
        try {
            ngrams = new Ngrams();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // load Console class into

    static Outputs.ConsoleOutput console = new Outputs.ConsoleOutput();

    // HashMap to store words and their corresponding vector
    public static Map<String, Double[]> wordsVectorsMap;


    // private key create to store the users input
    static String key;


    // String[] Word  Array
    private static String[] words;

    // Double[][] of vectors
    private static Double[][] vectors;

    // Constructor Search
    public Search() throws FileNotFoundException, InterruptedException {

        // Scanner in
        s = new Scanner(System.in);

        // Delimiter
        s.useDelimiter("[\\s,]+");

        // prompt the users to enter a word
        flexBorder("Enter in a Word: ");

        // key to store user  input
        key = s.nextLine().toLowerCase().trim();

        // set data for search
        ngrams.setData();

        // Words of text file
        words = ngrams.getWords();

        // Vector of text file
        vectors = ngrams.getVectors();


        /*
         * HashMaps to initialize map of the highest - n similar to the users inputs
         * set words  and vectors into the Maps
         */

        wordsVectorsMap = new HashMap<>();

        // loop to put words and vector into hashmap
        for (int i = 0; i < words.length ; i++) {
            if (words[i] != null  && vectors[i] != null) {
                wordsVectorsMap.put(words[i], vectors[i]);
            }

        }

        /*
        * Test hashmap to see if the word and vectors are input correctly
        *
        * test successfully loop through map
        *
        * commented out as the user doesn't need to see it run in program
         */

        /*
        for (Map.Entry<String, Double[]> entry : wordsVectorsMap.entrySet()) {
            System.out.println("Word: " + entry.getKey() + " Vector: " + Arrays.toString(entry.getValue()));
            for (Double value : entry.getValue()) {
                    System.out.print( value + " | ");
            }
            System.out.println();
        }

         */

        // userKey method in constructor
        userKey();

    }

    /*
     * flex border  formatting text into box that for enhanced UI design
     */

    public static void flexBorder(String text) {
        int length = text.length();
        String border = "+" + "-".repeat(length + 2) + "+";

        System.out.println(border);
        System.out.println("| " + text + " |");
        System.out.println(border);
    }


    /*
    * User key method use to search through the hashmap.
    *
     */


    public Outputs userKey() throws FileNotFoundException, InterruptedException {

        Double[] word = wordsVectorsMap.get(key);
        Match[] matches;


        if (word != null) {
            flexBorder("Your input is: " + key);

            /*
             * SOURCE - https://stackoverflow.com/questions/30191614/best-practice-to-create-two-level-deep-selection-menu-in-java-console
             * @AUTHOR - W vd L
             *  Sub-Menu  create linked to the main menu, User choice of algorithm will execute a switch-case on which algo is chosen
             *  DotProduct, Euclidean, Cosine calculation method and display top ten matches n of the users input
             *
             */


            // matches are map to new Match[] array
            matches = new Match[wordsVectorsMap.size() - 1];
            // index set
            int index = 0;

            // Mapping entry of key word loop through each algo to find the most similar words
            for (Map.Entry<String, Double[]> entry : wordsVectorsMap.entrySet()) {
                // check that key is equal the get key and the get value is not null
                if (!key.equals(entry.getKey()) && entry.getValue() != null) {
                    // const storing each algo calucalation
                    double dotProduct = Algo.calculateDotProduct(word, entry.getValue());
                    double euclideanDistance = Algo.calculateEuclideanDistance(word, entry.getValue());
                    double cosineSimilarity = Algo.calculateCosineDistance(word, entry.getValue());
                    // writes new matches to matches[] Array and increments the index
                    matches[index++] = new Match(entry.getKey(), dotProduct, euclideanDistance, cosineSimilarity);
                }
            }

            // Sorting out matches for dot Product
            sortingMatchesWords(matches, "dotProduct");
            flexBorder("Top matches based on Dot Product");
            // display Matrix
            displayMatrix(matches, 10);

            // Sorting out matches for Euclidean Distance
            sortingMatchesWords(matches, "euclideanDistance");
            flexBorder("Top matches based on Euclidean Distance");
            // display Matrix
            displayMatrix(matches, 10);

            // Sorting out matches for cosineSimilarity
            sortingMatchesWords(matches, "cosineSimilarity");
            flexBorder("Top matches based on Cosine Similarity");

            // display Matrix
            displayMatrix(matches, 10);

        } else {
            flexBorder("Word not found");

        }

        // console start
        console.start();

        // console get Output
        console.getOutput();

        // return Outputs
        return new Outputs();
    }

    /*
    * Class of Match of results
     */

    // Public class for Match
    public static class Match {

        //  const result
        String result;

        // const dotProduct
        double dotProduct;

        // euclidean Distance
        double euclideanDistance;

        // const cosine Similarity
        double cosineSimilarity;

        //  Match constructor to store the results and algo's results
        public Match(String result, double dotProduct, double euclideanDistance, double cosineSimilarity) {
            this.result = result;
            this.dotProduct = dotProduct;
            this.euclideanDistance = euclideanDistance;
            this.cosineSimilarity = cosineSimilarity;
        }

        // get Dot Product Method
        public double getDotProduct() {
            return dotProduct;
        }

        // get Euclidean Distance
        public double getEuclideanDistance() {
            return euclideanDistance;
        }

        // get cosine Similarity
        public double getCosineSimilarity() {
            return cosineSimilarity;
        }

        // Override the toString to format results and algorithm

        @Override
        public String toString() {
            return String.format("Word: %s, Dot Product: %.4f, Euclidean Distance: %.4f, Cosine Similarity: %.4f",
                    result, dotProduct, euclideanDistance, cosineSimilarity);
        }

    }

    // Sorting matches constructor that sorts Match arrays for each algorithm
    public static void sortingMatchesWords(Match[] matches, String field) {
        switch (field) {
            case "dotProduct":
                Arrays.sort(matches, Comparator.comparingDouble(Match::getDotProduct).reversed());
                break;
            case "euclideanDistance":
                Arrays.sort(matches, Comparator.comparingDouble(Match::getEuclideanDistance));
                break;
            case "cosineSimilarity":
                Arrays.sort(matches, Comparator.comparingDouble(Match::getCosineSimilarity).reversed());
                break;
        }
    }

    // Display the top`N matches and  printed the matches
    public static void displayMatrix(Match[] matches, int n) {


        for (int i = 0; i < Math.min(n, matches.length); i++) {

            System.out.println(matches[i]);
        }
    }



}

