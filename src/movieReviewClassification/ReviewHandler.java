package movieReviewClassification;

import java.io.*;
import java.util.*;


/**
 * CS3354 Fall 2020 Review Handler class extends the AbstractReviewHandler class.
 *
 * @author garza
 */
public class ReviewHandler extends AbstractReviewHandler implements Serializable{

    public ReviewHandler() {super();}

    /**
     * Loads reviews from a given path. If the given path is a .txt file, then
     * a single review is loaded. Otherwise, if the path is a folder, all reviews
     * in it are loaded.
     *
     * @param filePath  The path to the file (or folder) containing the review(sentimentModel).
     * @param realClass The real class of the review (0 = Negative, 1 = Positive
     *                  2 = Unknown).
     * @return A list of reviews as objects.
     */
    @Override
    public void loadReviews(String filePath, int realClass) {
            File dirOrFile = new File(filePath);
            if(dirOrFile.isDirectory()) {
                File[] files = new File(filePath).listFiles();
                for (File f : files) {
                    try {
                        MovieReview mR = (readReview(f.getPath(), realClass));
                        Integer key = mR.getID();
                        super.database.put(key, mR);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                try {
                    MovieReview mR = (readReview(filePath, realClass));
                    Integer key = mR.getID();
                    super.database.put(key, mR);
                } catch (IOException e) {
                    e.printStackTrace();
                    //System.out.println("Review failed to load.");
                }
            }

        }

    /**
     * Reads a single review file and returns it as a MovieReview object.
     * This method also calls the method classifyReview to predict the polarity
     * of the review.
     *
     * @param reviewFilePath A path to a .txt file containing a review.
     * @param realClass      The real class entered by the user.
     * @return a MovieReview object.
     * @throws IOException if specified file cannot be opened.
     */
    @Override
    public MovieReview readReview(String reviewFilePath, int realClass) throws IOException {

        File review = new File(reviewFilePath);
        Scanner reader = new Scanner(review);

        String idString = review.getName();
        Integer key = keyGenerator(idString);
        String movieReviewTxt = reader.nextLine();

        MovieReview movieReview = new MovieReview(movieReviewTxt, realClass);
        int polar = super.classifyReview(movieReview);
        movieReview.setPredictedPolarity(polar);
        movieReview.setID(key);

        close(reader);

        return movieReview;
    }

    /**
     * Deletes a review from the database, given its id.
     *
     * @param id The id value of the review.
     */
    public void deleteReview(int id) {
        Boolean check = super.database.containsKey(id);
        if(check) {
            super.database.remove(id);
        }
        else{
            //System.out.println("The MovieReview was not found.");
        }
    }

    /**
     * Loads review database.
     */
    public void loadSerialDB() {
        String localDir = System.getProperty("user.dir");
        File[] files = new File(localDir).listFiles();
        for (File f : files){
            try {
                String fileName = f.getName();
                if (fileName.contains(DATA_FILE_NAME)) {
                    FileInputStream stream = new FileInputStream(f);
                    ObjectInputStream obj = new ObjectInputStream(stream);
                    Map<Integer, MovieReview> database = (Map<Integer, MovieReview>)obj.readObject();
                    super.database = database;

                    close(stream);
                    close(obj);
                }
                else{
                    continue;
                }
            }catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    /**
     * Searches the review database by id.
     *
     * @param id The id to search for.
     * @return The review that matches the given id or null if the id does not
     * exist in the database.
     */
    public MovieReview searchById(int id) {
        return super.database.get(id);
    }

    /**
     * Searches the review database for reviews matching a given substring.
     *
     * @param substring The substring to search for.
     * @return A list of review objects matching the search criterion.
     */
    public List<MovieReview> searchBySubstring(String substring) {

        List<MovieReview> listOfMovies = new ArrayList<>();
        listOfMovies.addAll(super.database.values());
        List<MovieReview> listOfMatchingMovies = new ArrayList<>();
        for (MovieReview review : listOfMovies) {
            String movieReviewTxt = review.getText();
            if (movieReviewTxt.contains(substring)) {
                listOfMatchingMovies.add(review);
            }
        }
        return listOfMatchingMovies;
    }

    /**
     * Key is created using the id of the MovieReview found in the title of the original file.
     *
     * @param idString The string containing the ID of the MovieReview.
     * EXAMPLE: ("4_4.txt") implies ID: 44.
     * @return The id in the form of just an integer.
     */
    public Integer keyGenerator(String idString){
        int i = 0;
        while(i < idString.length()){
            char ch = idString.charAt(i);
            if(ch == '_' || ch == '.' || ch == 't' || ch == 'x'){
                String a = idString.substring(0,i);
                String b = idString.substring(i+1);
                idString = a + b;
            }
            else{
                i++;
            }
        }
        Integer key = Integer.parseInt(idString);
        return key;
    }
}

