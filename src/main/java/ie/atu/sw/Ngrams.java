// Import package
package ie.atu.sw;

// Import library io
import java.io.*;



// Array class
public class Ngrams {


    // File Path
     File filePath =  new File("ie/atu/sw", "word-embeddings.txt");

    // Words 1-dimensional array
    private static String[] words;
    // Vectors 2-dimensional array
    private static Double[][] vectors;

    // private variable for storing rows INITIAL_CAPACITY
    private static int rows;

    // private variable for storing columns INITIAL_CAPACITY
    private static int columns;


    // Constructor for N-grams

    public Ngrams() throws FileNotFoundException {

        // Set data
        setData();

        words = getWords();
        vectors = getVectors();

    }

    /*
    * set Data method to separate words and vectors in text file.
    *  First separate words from the vector through a while loop on each row in a try catch block
    *  set words to a String[] Array
    *
    *  Second while loop irritates through each row and columns in try-catch block
    *  set vectors to a Double[][] Array
    *
    *
     */
    void setData() throws FileNotFoundException {

        InputStream inputStream = Ngrams.class.getClassLoader().getResourceAsStream(String.valueOf(filePath));

        // Check if the inputStream is null
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            countRowsAndColumns(reader);
            initializeArrays();

            // Reset the stream and reader to read again
            try (InputStream inputStreamI = Ngrams.class.getClassLoader().getResourceAsStream(String.valueOf(filePath));
                 BufferedReader readerI = new BufferedReader(new InputStreamReader(inputStreamI))) {

                if (inputStreamI == null) {
                    throw new FileNotFoundException("File not found during second read: " + filePath);
                }

                populateWordsAndVectors(readerI);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void countRowsAndColumns(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (rows == 0) {
                columns = line.split(",").length - 1;
            }
            rows++;
        }
    }

    private  static void initializeArrays() {
        words = new String[rows];
        vectors = new Double[rows][columns];
    }

    private static void populateWordsAndVectors(BufferedReader reader) throws IOException {
        String line;
        int rowIndex = 0;

        while ((line = reader.readLine()) != null) {
            String[] entries = line.split(",");
           words[rowIndex] = entries[0];
            for (int colIndex = 1; colIndex < entries.length; colIndex++) {
                vectors[rowIndex][colIndex - 1] = Double.parseDouble(entries[colIndex].trim());
            }

            rowIndex++;
        }
    }


    // Get words array
    public static String[] getWords() {
        return words.clone();
    }

    // Get vectors array
    public static Double[][] getVectors() {
        return  vectors.clone();
    }




}