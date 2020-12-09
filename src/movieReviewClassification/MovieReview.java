package movieReviewClassification;

import java.io.*;

/**
 * CS3354 Fall 2020 Review Handler MovieReview class
 *
 * @author garza
 */

class MovieReview implements Serializable {
    private String txt;
    private Integer realClass;
    private Integer predictedClass;
    private Integer id;

    /**
     * MovieReview Constructor
     *
     * @param movieReviewTxt  String containing the Movie Review text.
     * @param realClass The real class of the review (0 = Negative, 1 = Positive
     *                  2 = Unknown).
     */
    public MovieReview(String movieReviewTxt, Integer realClass)throws IOException {
        this.txt = movieReviewTxt;
        this.realClass = realClass;
    }

    /**
     * MovieReview Constructor
     *
     * @param movieReviewTxt  String containing the Movie Review text.
     */
    public MovieReview(String movieReviewTxt)throws IOException {
        this.txt = movieReviewTxt;
    }

    /**
     * Displays MovieReview Object.
     *
     * @return MovieReview displayed.
     */
   /* public void displayMovieReview (){
        System.out.println("Review ID:       " + this.id);
        System.out.println("Review text:     " + this.getText().substring(0, 50));
        System.out.println("Predicted Class: " + this.predictedClass);
        System.out.println("Real Class:      " + this.realClass);
        System.out.println("------------------------------------------------------------------------");
    }
    */



    /**
     * @return MovieReview text.
     */
    public String getText(){
        return this.txt.substring(0, 50);
    }

    /**
     * @return MovieReview text.
     */
    public Integer getPolarity(){
        return this.predictedClass;
    }

    /**
     * @return MovieReview text.
     */
    public Integer getRealClass(){
        return this.realClass;
    }

    /**
     * @return MovieReview text.
     */
    public Integer getID(){return this.id;}

    /**
     * @param movieReviewTxt sets text of the movie review.
     */
    public void setText(String movieReviewTxt){
        this.txt = movieReviewTxt;
    }

    /**
     * @param predictedClass sets predictedClass of the movie review.
     */
    public void setPredictedPolarity(int predictedClass) {
        this.predictedClass = predictedClass;
    }

    /**
     * @param realClass sets realClass of the movie review.
     */
    public void setRealClass(int realClass) {
        this.realClass = realClass;
    }

    /**
     * @param ID sets ID of the movie review.
     */
    public void setID(Integer ID){this.id = ID;}

}
