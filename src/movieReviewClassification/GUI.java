package movieReviewClassification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;

/**
 * GUI is constructed as gui in main.
 * Then we'll have an instance of GUI called gui, were each button corresponds to its own inner class that implement runnable (threads).
 * this implies any button creates a new thread with its own set of instructions.
 *
 * The clear button safely interrupts any active thread t.
 * Also, clicking another button should clear all other instances of threads as well.
 */


public class GUI extends JFrame implements Serializable, ActionListener {

    private static Thread t;
    private final ReviewHandler ReviewHandler;
    private final JPanel choices;
    private final JPanel everythingElse;
    private final JButton choice0;
    private final JButton choice1;
    private final JButton choice2;
    private final JButton choice3;
    private final JButton clear;
    private final JLabel instructions;

    public GUI() {
        super("Allen's Movie Review Classifier");
        this.ReviewHandler = new ReviewHandler();
        ReviewHandler.loadSerialDB();

        this.choices = new JPanel();
        this.everythingElse = new JPanel();
        this.add(BorderLayout.NORTH, choices);
        this.add(BorderLayout.SOUTH, everythingElse);

        this.choice0 = new JButton("Exit Program");
        this.choice1 = new JButton("Load new movie review collection");
        this.choice2 = new JButton("Delete movie review from database");
        this.choice3 = new JButton("Search movie review in database by ID or substring");
        this.clear = new JButton("Clear");
        this.instructions = new JLabel("INSTRUCTIONS: Choose an option, when finished choose clear.");

        choices.add(choice0);
        choices.add(choice1);
        choices.add(choice2);
        choices.add(choice3);
        everythingElse.add(clear);
        everythingElse.add(BorderLayout.WEST, instructions);

        choice0.addActionListener(this);
        choice1.addActionListener(this);
        choice2.addActionListener(this);
        choice3.addActionListener(this);
        clear.addActionListener(this);


    }

    /**
     * The case that choice0: Exit Program is called.
     *
     * Exits program, saves and serializes database.
     */

    public class choice0 extends GUI implements Runnable {
        @Override
        public void run() {
            ReviewHandler.saveSerialDB();
            System.exit(0);
        }
    }

    /**
     * The case that choice1: Load New Movie Review Collection, is called
     *
     * User is prompted to select realClass of set of movie reviews chosen, and select file location of
     * movie reviews to be loaded
     *
     * @returns Thread tr
     */
    public class choice1 extends GUI implements Runnable, ActionListener {
        Thread tr;

        private JPanel panel;
        private JButton finchoice;
        private Integer realClass;
        private JRadioButton neg;
        private JRadioButton pos;
        private JRadioButton unk;
        private JFileChooser addy;

        choice1(){
            tr = new Thread(this);
            tr.start();

            // panel to add new contents.
            this.panel = new JPanel();
            GUI.this.add(panel);

            //user decides what realclass of movieReviews should be
            JLabel polarity = new JLabel("What is the real polarity of this set of reviews?");
            ButtonGroup grp1 = new ButtonGroup();
            this.pos = new JRadioButton("Positive");
            this.neg = new JRadioButton("Negative");
            this.unk = new JRadioButton("Unknown");
            this.finchoice = new JButton("Final Choice of real polarity.");
            grp1.add(pos);
            grp1.add(neg);
            grp1.add(unk);
            grp1.add(finchoice);

            finchoice.addActionListener(this);
            panel.add(BorderLayout.WEST, polarity);
            panel.add(BorderLayout.WEST, pos);
            panel.add(BorderLayout.WEST, neg);
            panel.add(BorderLayout.WEST, unk);
            panel.add(BorderLayout.WEST, finchoice);
        }

        @Override
        public void run() {
            while(!tr.isInterrupted()) {
            }
            panel.removeAll();
            GUI.this.remove(panel);
            GUI.this.revalidate();
            GUI.this.repaint();
            GUI.this.pack();

        }

        /**
         * create a choice box of negative or positive or unknown for real class
         * once choice is made, create a filechooser gui so user can type address of folder or filepath
         * call ReviewHandler.loadSerialDB
         * calculate reviewhandler.database ratio of matching polarities of movie reviews of predicted and real and
         * output, save set of Movie Reviews into local database.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == finchoice) {

                if (neg.isSelected()) {
                    realClass = 0;
                } else if (pos.isSelected()) {
                    realClass = 1;
                } else if (unk.isSelected()) {
                    realClass = 2;
                }

                //filechooser
                this.addy = new JFileChooser();
                String curDir = System.getProperty("user.dir");
                addy.setCurrentDirectory(new File(curDir));
                addy.setDialogTitle("Choose MovieReviews");
                addy.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                panel.add(addy);

                //user chooses directory and polarity, run in reviewhandler
                JButton open = new JButton();
                if (addy.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
                    GUI.this.ReviewHandler.loadReviews(addy.getSelectedFile().getAbsolutePath(), realClass);
                    JLabel loaded = new JLabel("The movie reviews were loaded successfully!");
                    panel.add(loaded);
                } else {
                    JLabel loaded = new JLabel("The movie reviews were not loaded successfully.");
                    panel.add(loaded);
                }
                //NEED TO KILL THREAD HERE.
                GUI.this.pack();
            }
        }
    }

    /**
     * The case that choice2: Delete Movie Review from database is called.
     *
     * Deletes MovieReview by ID in database
     *
     * @returns Thread tr
     */
    public class choice2 extends GUI implements Runnable, ActionListener {
        Thread tr;


        private JPanel panel;
        private JFormattedTextField id;
        private JButton finchoice;

        choice2(){
            tr = new Thread(this);
            tr.start();

            //panel and enter button
            this.panel = new JPanel();
            this.finchoice = new JButton("Enter ID");
            finchoice.addActionListener(this);
            panel.add(finchoice);
            GUI.this.add(panel);

            //ID text box
            NumberFormat format = NumberFormat.getNumberInstance();
            this.id = new JFormattedTextField(format);
            id.setColumns(10);

            panel.add(id);
        }

        @Override
        public void run() {
            while(!tr.isInterrupted()) {
            }
            panel.removeAll();
            GUI.this.remove(panel);
            GUI.this.revalidate();
            GUI.this.repaint();
            GUI.this.pack();
        }

        /**
         * create text field gui so id number can be typed by user
         * check database to see if key and Movie Review exists,
         * iff MovieReview exists then delete, else display message.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == finchoice) {
                int key = Integer.parseInt(id.getText());
                Boolean contain = ReviewHandler.database.containsKey(key);
                JLabel deleted;
                if (contain) {
                    deleted = new JLabel("Deleted " + ReviewHandler.database.get(key).getID());
                    ReviewHandler.deleteReview(key);
                } else {
                    deleted = new JLabel("The movie review was not found.");
                }
                panel.add(deleted);
                GUI.this.pack();
            }
        }
    }

    /**
     * The case that choice3: Search Movie Review Database is called.
     *
     * Presents the user options to search for Movie Review by ID or substring,
     * then presents found Movie Reviews in a separate frame with a table.
     * @returns thread tr
     */
    public class choice3 extends GUI implements Runnable, ActionListener {
        Thread tr;

        private JPanel panel;
        private JTable table;
        private JFormattedTextField entry;
        private JButton search;
        private JRadioButton id;
        private JRadioButton subString;

        choice3(){
            tr = new Thread(this);
            tr.start();

            /**
             * create a new window where user can choose to search movie review
             * by id or by substring.
             * depending on which they choose, search reviewHandler database and
             * show a table of all movie reviews that match the search.
             */
            panel = new JPanel();
            GUI.this.add(panel);
            ButtonGroup grp1 = new ButtonGroup();

            id = new JRadioButton("Search by ID");
            subString = new JRadioButton("Search by Substring");
            entry = new JFormattedTextField();
            search = new JButton("Search");

            grp1.add(id);
            grp1.add(subString);
            grp1.add(search);
            entry.setColumns(10);

            panel.add(id);
            panel.add(subString);
            panel.add(search);
            panel.add(entry);

            subString.addActionListener(this);
            id.addActionListener(this);
            search.addActionListener(this);
        }

        @Override
        public void run() {
            while(!tr.isInterrupted()) {
            }
            panel.removeAll();
            GUI.this.remove(panel);
            GUI.this.revalidate();
            GUI.this.repaint();
            GUI.this.pack();


        }

        /**
         * Table shows ID, first 50 char substring, Predicted Polarity, Real Polarity.
         *
         * Table is constructed with necessary information and displayed in its own JFrame.
         * @param e
         */

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == search) {
                if(id.isSelected()){
                    try {
                        String id = entry.getText();
                        int ID = Integer.parseInt(id);
                        MovieReview mr = ReviewHandler.searchById(ID);
                        String mrText = mr.getText();
                        String predictedPol = Integer.toString(mr.getPolarity());
                        String realClass = Integer.toString(mr.getRealClass());
                        String[][] data = {
                                {id, mrText, predictedPol, realClass}
                        };
                        String[] column = {"ID", "Text", "Predicted Polarity", "Real Polarity"};

                        table = new JTable(data, column);
                        table.setBounds(200, 200, 400, 400);
                        JFrame tableframe = new JFrame("MovieReviews found");
                        tableframe.add(table);
                        tableframe.setSize(600, 300);
                        JScrollPane scroll = new JScrollPane(table);
                        tableframe.add(scroll);
                        tableframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        tableframe.setVisible(true);

                        GUI.this.pack();
                    }catch (NullPointerException a){
                        JLabel noMr = new JLabel("No MovieReview found.");
                        panel.add(noMr);
                        GUI.this.pack();
                    }catch (NumberFormatException n){
                        JLabel numberOnly = new JLabel("Please type a number when choosing ID option");
                        panel.add(numberOnly);
                        GUI.this.pack();
                    }
                }
                else if(subString.isSelected()){
                    String substr = entry.getText();
                    try {
                        List<MovieReview> mrs = ReviewHandler.searchBySubstring(substr);

                        String[] column = {"ID", "Text", "Predicted Polarity", "Real Class"};
                        String[][] data = new String[mrs.size()][4];
                        int counter = 0;
                        for(MovieReview mR: mrs){
                            String mrText = mR.getText();
                            String id = Integer.toString(mR.getID());
                            String predictedPol = Integer.toString(mR.getPolarity());
                            String realClass = Integer.toString(mR.getRealClass());
                            String info[] = {id, mrText, predictedPol, realClass};
                            data[counter] = info;
                            counter++;
                        }

                        JTable table = new JTable(data, column);

                        table.setBounds(200, 200, 400, 400);
                        JFrame tableframe = new JFrame("MovieReviews found");
                        tableframe.add(table);
                        tableframe.setSize(600, 300);
                        JScrollPane scroll = new JScrollPane(table);
                        tableframe.add(scroll);
                        tableframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        tableframe.setVisible(true);

                        tableframe.pack();
                        GUI.this.pack();
                    }catch (NullPointerException a){
                        JLabel noMr = new JLabel("No MovieReview found.");
                        panel.add(noMr);
                        GUI.this.pack();
                    }
                }
                else{
                    JLabel chooseAnOption = new JLabel("Please clear and choose an option when searching");
                    panel.removeAll();
                    GUI.this.revalidate();
                    GUI.this.repaint();
                    panel.add(chooseAnOption);
                    GUI.this.pack();
                }
            }
        }
    }

    public static void main(String[] args) {

        JFrame gui = new GUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.pack();
        gui.setVisible(true);
    }

    /**
     * Creates runnable class of choice* and runs a new thread. Checks if thread exists first, if so
     * interrupts the thread.
     *
     * choice0 => exit program and save database
     * choice1 => load movie review/s from address
     * choice2 => delete movie review from database by id
     * choice3 => search movie review by id or string
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == choice0){
            GUI.choice0 choice = new choice0();
            choice.run();
            GUI.this.pack();
        }

        else if(e.getSource() == choice1) {
            if (GUI.t != null) {
                GUI.t.interrupt();
            }
            GUI.choice1 choice = new choice1();
            GUI.this.pack();
            GUI.t = choice.tr;
        }

        else if(e.getSource() == choice2) {
            if (GUI.t != null) {
                GUI.t.interrupt();
            }
            GUI.choice2 choice = new choice2();
            GUI.this.pack();
            GUI.t = choice.tr;
        }

        else if(e.getSource() == choice3) {
            if (GUI.t != null) {
                GUI.t.interrupt();
            }
            GUI.choice3 choice = new choice3();
            GUI.this.pack();
            GUI.t = choice.tr;
        }

        else if(e.getSource() == clear){
            GUI.t.interrupt();
        }
    }
}

