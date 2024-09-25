// load package
package ie.atu.sw;

// import java libraries
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.*;




// Create Class to  config Algorithm
public class Algo {

    // import all other method in this class
    static Ngrams ngrams;

    static {
        try {
            ngrams = new Ngrams();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    // Scanner initialize
    private static Scanner s;

    // While true condition to keep running
    private static boolean keepRunning = true;


    // words and vectors variables to store Array
    private static String[] words;

    private static Double[][] vectors;



    // Constructor Method for Algo
    public Algo() throws FileNotFoundException, InterruptedException {

        // scanner to read users input
        s = new Scanner(System.in);

        // array class set data
        ngrams.setData();

        // get String[] arrays
        words = ngrams.getWords();

        // get Vectors[][] arrays
        vectors = ngrams.getVectors();


        //init the menu for users to pick each Algo
        start();


    }

    /*
     * Flex Border for formatting and creating a better Enhance UI
     */

    public static void flexBorder(String text) {
        int length = text.length();
        String border = "+" + "-".repeat(length + 2) + "+";

        System.out.println(border);
        System.out.println("| " + text + " |");
        System.out.println(border);
    }

    /*
    *
     */




    // shows Options in commandLine
    public static void showsOptions() {
        flexBorder("Please enter the Algo of your choice: ");
        flexBorder("(1) Dot Product");
        flexBorder("(2) Euclidean Distance");
        flexBorder("(3) Cosine Distance");
    }

    /*
    * When executed, the program prompts the user to choose an algorithm.
    * Based on the users choice,
    *  it calculates and displays the most and least similar words and asks whether to continue or save the results to a file.
     */


    // Menu switch-case statement
    static void start() throws FileNotFoundException, InterruptedException {
        while (keepRunning) {
            showsOptions();
            int choice = Integer.parseInt(s.next());
            switch (choice) {
                case 1 -> SimiliarityScored.similarity(words, vectors, "dot");
                case 2 -> SimiliarityScored.similarity(words, vectors, "euclidean");
                case 3 -> SimiliarityScored.similarity(words, vectors, "cosine");
                default -> System.out.println("[Error] Invalid Selection");
            }
            if(keepRunning) {
                next();
            }
        }

    }

    /*
     *  Next method to ask user to continue with program.
     * if Yes will prompt users to enter a word to search similar word.
     * using each algo to show the most common results
     */

    static void next() throws FileNotFoundException, InterruptedException {
        flexBorder(" Would you like to continue to yes? Yes or No ");
        String key = s.next();
        if (key.equalsIgnoreCase("Yes")) {
            new Search();
        } else if (key.equalsIgnoreCase("No")){
            flexBorder("Would you like to write this result to file? Yes or No");
            String nextKey = s.next();
            if (nextKey.equalsIgnoreCase("Yes")) {
             new Outputs();
            } else if (nextKey.equalsIgnoreCase("No")){
                keepRunning = false;
                flexBorder("Exiting program...");
            }

        }
    }




    /*
     * AlgoMatrix method
     *  from  the matrix I want to perform  each Algo on the words displayed at random.
     *
     * User will choose the either from DotProduct, Euclidean Distance ,  Cosine Distance.
     *
     * it will score each word  base on the user input and match they to the closest pair
     *
     */

    /*
     * SOURCE -  Mastering Java for Data Science - https://eds.p.ebscohost.com/eds/ebookviewer/ebook/bmxlYmtfXzE1ODc1MDFfX0FO0?sid=dc0b953b-592a-43c5-b529-8d6e438bc8bf@redis&vid=1&format=EB
     * @Author - Alexey Grigorev
     * Date - 2017
     * Chapter 6: Working with Text NLP
     */


    // Create Similiarity Scored
    public static class SimiliarityScored {

        static Outputs.ConsoleOutput console = new Outputs.ConsoleOutput();

        // Double of Vectors
        private final Double[][] vectors;

        // String of words
        private final String[] words;

        // Max score
        private final double maxScore = 1;

        // Min score
        private final double minScore = 0.0;

        // String[] to store  Most Similar words
        private String[] mostSimilarWords;

        // String[] to store least similar words
        private String[] leastSimilarWords;

        // Double[] to store most similar Score
        private Double[] mostSimilarScores;

        // Double[] to store least similar Score
        private Double[] leastSimilarScores;


        // Similiarity Scored results store in  this method
        public SimiliarityScored(String[] words, Double[][] vectors) {
            this.words = words;
            this.vectors = vectors;
            this.mostSimilarWords = new String[10];
            this.leastSimilarWords = new String[10];
            this.mostSimilarScores = new Double[10];
            this.leastSimilarScores = new Double[10];


            for (int i = 0; i < 10; i++) {
                mostSimilarScores[i] = maxScore;
                leastSimilarScores[i] = minScore;
            }
        }

        /*
        * SOURCE - https://stackoverflow.com/questions/30191614/best-practice-to-create-two-level-deep-selection-menu-in-java-console
        * @AUTHOR - W vd L
        *  Sub-Menu  create linked to the main menu, User choice of algorithm will execute a switch-case on which algo is chosen
        *  DotProduct, Euclidean, Cosine calculation method and display topScore n of the least and most similar words
        * Display TopWords for age to display the results
         */

        // calculate Score method looping through the words and corresponding vectors
        private void calculateScore(String algo) throws InterruptedException {


            //
            int size = words.length;
            int cores = Runtime.getRuntime().availableProcessors();
            ExecutorService ex = Executors.newFixedThreadPool(cores);

            // loop over the length words
            for (int i = 0; i < size; i++) {
                int results = i;
                ex.submit(() -> {
                    for (int j = results + 1; j < size; j++) {
                        // const Score
                        double score = 0.0;

                        int resultsII = j;
                        switch (algo) {
                            case "dot": {
                                score = calculateDotProduct(vectors[results], vectors[resultsII]);
                                break;
                            }
                            case "euclidean": {
                                score = calculateEuclideanDistance(vectors[results], vectors[resultsII]);
                                break;
                            }
                            case "cosine": {
                                score = calculateCosineDistance(vectors[results], vectors[resultsII]);
                                break;
                            }

                        }

                        topScore(words[results] + " " + words[resultsII] + " ", score);
                    }

                });


            }

            // initialize the display Top Word
            displayTopWords();
            // Listen for result ton console so users can save the results when prompt
            console.start();
            console.getOutput();

        }


        // Top Score show the most and least similar scored words

        private void topScore(String word, double scores) {

            for (int i = 0; i < 10; i++) {
                if (scores > mostSimilarScores[i]) {
                    shift(mostSimilarScores, mostSimilarWords, i);
                    mostSimilarScores[i] = scores;
                    if (word != null && !word.isEmpty()) {
                        mostSimilarWords[i] = word;
                    }
                    break;
                }
            }

            for (int i = 0; i < 10; i++) {
                if (scores < leastSimilarScores[i]) {
                    shift(leastSimilarScores, leastSimilarWords, i);
                    leastSimilarScores[i] = scores;
                    if (word != null && !word.isEmpty()) {
                        leastSimilarWords[i] = word;
                    }
                    break;
                }
            }
        }

        // Shift method Help shift the array into across

        private void shift(Double[] scores, String[] words, int index) {
            if (index < 0 || index >= scores.length) {
                throw new IllegalArgumentException("index out of bounds");
            }
            for (int i = scores.length - 1; i > index; i--) {
                scores[i] = scores[i - 1];
                words[i] = words[i - 1];
            }
        }


        // Display Top words from similar words
        private void displayTopWords() {

            flexBorder("Most Similar words (N-score closest to each other)");
            for (int i = 0; i < 10; i++) {
                System.out.println(mostSimilarWords[i] + mostSimilarScores[i]);
            }

            flexBorder("Least Similar Words (N-score is closest to each other)");
            for (int i = 0; i < 10; i++) {
                System.out.println(leastSimilarWords[i] + leastSimilarScores[i]);
            }
        }

        // Similarity method instance of new Similarity scored to the algorithm
         public static void similarity(String[] words, Double[][] vectors, String algo) throws InterruptedException {
            new SimiliarityScored(words, vectors).calculateScore(algo);


        }




    }

    /*
     * Calculation of each  Algorithm;
     *  DotProduct
     * Euclidean Distance
     * Cosine Distance
     */

    public static double calculateDotProduct(Double[] v1, Double[] v2) {
        double dotProduct = 0.0;
        for (int i = 0; i < v1.length; i++) {
            if (v1[i] != null && v2[i] != null) {
                dotProduct += v1[i] * v2[i];
            }
        }
        return dotProduct;
    }

    public static double calculateEuclideanDistance(Double[] v1, Double[] v2) {
        double sum = 0.0;
        for (int i = 0; i < v1.length; i++) {
            if(v1[i] != null && v2[i] != null){
                sum += Math.pow(v1[i] - v2[i], 2);
            }
        }
        return Math.sqrt(sum);
    }

    public static double calculateCosineDistance(Double[] v1, Double[] v2) {
        double dotProduct = 0.0;
        double magnitudeV1 = 0.0;
        double magnitudeV2 = 0.0;

        for (int i = 0; i < v1.length; i++) {
            if (v1[i] != null && v2[i] != null) {
                dotProduct += v1[i] * v2[i];
                magnitudeV1 += Math.pow(v1[i], 2);
                magnitudeV2 += Math.pow(v2[i], 2);
            }
        }

        magnitudeV1 = Math.sqrt(magnitudeV1);
        magnitudeV2 = Math.sqrt(magnitudeV2);

        if (magnitudeV1 == 0.0 || magnitudeV2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (magnitudeV1 * magnitudeV2);
    }



}