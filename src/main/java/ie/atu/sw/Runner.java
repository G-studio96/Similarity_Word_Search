// module package
package ie.atu.sw;

// import Menu module into main Runner

import java.io.FileNotFoundException;

// Create Runner class
public class Runner {

    // main call method to execute program
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        // load Menu class for users input in command line
        Menu md = new Menu();

        // start
        md.start();
    }
}