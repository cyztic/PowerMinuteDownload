//**********************************   CLASS DESCRIPTION  **************************************
//*								        	                                                   *
//*	         This is the java controller for the HomePageFXML file. This class controls        *
//*          the home page window where users can set their reminders, view stats,             *
//           unlock rewards, and watch the library of videos.                                  *
//*                                                                                            *
//**********************************************************************************************

package PowerMinutePackage;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;

public class HomePageController implements Initializable {

    //*********************************MEMBER VARIABLES******************************************
    //get database connector singleton object for this class
    DBConnector db_connector = DBConnector.getInstance();

    //create a list to hold each executor service
    ArrayList<ScheduledExecutorService> schedule_service_list = new ArrayList<>();

    //scanner object to read from file
    private Scanner file_scanner;

    //ArrayList to store Check boxes
    List<CheckBox> check_box_list = new ArrayList<>();

    //int arrays to hold day and hour integer in unison with checkboxlist
    int[] days_of_week_array = new int[55];
    int[] hour_of_day_array = new int[55];
    //integer to track current level of user for leveling feature
    int current_level;
    //integer to track the index of the users fav quote
    int fav_quote_index;
    //remainder of total powerminutes%20
    int remainder;

    //video page
    String[] image_file_names;
    String[] video_file_names;
    private Pagination video_pagination;

//********************************GLOBAL FXML CONTROLS******************************************

    //Main Home Page tab pane and  tabs
    @FXML
    private Tab reminders_tab, progress_tab, rewards_tab, videos_tab;
    //all pie charts
    @FXML
    PieChart user_today_pie_chart, user_week_pie_chart, user_month_pie_chart,
            partner_today_pie_chart, partner_week_pie_chart, partner_month_pie_chart;
    //labels for partner stat tabs if there is no partner
    @FXML
    private Label partner_today_label, partner_week_label, partner_month_label;

    //all check boxes for reminder selection
    @FXML
    CheckBox mon_7_checkbox, mon_8_checkbox, mon_9_checkbox, mon_10_checkbox, mon_11_checkbox,
            mon_12_checkbox, mon_1_checkbox, mon_2_checkbox, mon_3_checkbox, mon_4_checkbox,
            mon_5_checkbox, tues_7_checkbox, tues_8_checkbox, tues_9_checkbox, tues_10_checkbox,
            tues_11_checkbox, tues_12_checkbox, tues_1_checkbox, tues_2_checkbox, tues_3_checkbox,
            tues_4_checkbox, tues_5_checkbox, wed_7_checkbox, wed_8_checkbox, wed_9_checkbox, wed_10_checkbox,
            wed_11_checkbox, wed_12_checkbox, wed_1_checkbox, wed_2_checkbox, wed_3_checkbox,
            wed_4_checkbox, wed_5_checkbox, thur_7_checkbox, thur_8_checkbox, thur_9_checkbox, thur_10_checkbox,
            thur_11_checkbox, thur_12_checkbox, thur_1_checkbox, thur_2_checkbox, thur_3_checkbox,
            thur_4_checkbox, thur_5_checkbox, fri_7_checkbox, fri_8_checkbox, fri_9_checkbox, fri_10_checkbox,
            fri_11_checkbox, fri_12_checkbox, fri_1_checkbox, fri_2_checkbox, fri_3_checkbox,
            fri_4_checkbox, fri_5_checkbox;
    @FXML
    private AnchorPane video_tab_pane;
    @FXML
    private ProgressBar level_bar;
    @FXML
    private Label current_level_label, fav_quote_label, recent_quote_label;
    @FXML
    private ComboBox<String> fav_quote_combobox;

    //*********************************CLASS METHODS******************************************
    // INPUT:    none
    // TASK:     Since tabs titles are vertical we must create a
    //           label and add it to the tab to get around this
    // CALLED:   in initialize()
    // OUTPUT:   none
    private void setTabTitles() {
        //set reminders title
        Label reminders = new Label("Reminders");
        StackPane stp = new StackPane(new Group(reminders));
        reminders_tab.setGraphic(stp);
        //set progress title
        Label progress = new Label("Progress");
        StackPane stp2 = new StackPane(new Group(progress));
        progress_tab.setGraphic(stp2);
        //set rewards title
        Label rewards = new Label("Rewards");
        StackPane stp3 = new StackPane(new Group(rewards));
        rewards_tab.setGraphic(stp3);
        //set videos title
        Label videos = new Label("Videos");
        StackPane stp4 = new StackPane(new Group(videos));
        videos_tab.setGraphic(stp4);
    }

    // INPUT:   none
    // TASK:    sets all the pie charts up for user and partner
    // CALLED:  in initialize()
    // OUTPUT:  none
    private void setPieCharts() {
        PieChart.Data acceptedWorkout;
        PieChart.Data declineWorkout;

        //user daily pie chart stats
        if (0 < db_connector.getUserDailyAccepted() && 0 < db_connector.getUserDailyDeclined()) {
            acceptedWorkout = new PieChart.Data("Accepted Power Minutes", db_connector.getUserDailyAccepted());
            declineWorkout = new PieChart.Data("Declined Power Minutes", db_connector.getUserDailyDeclined());
        } else {
            acceptedWorkout = new PieChart.Data("Accepted Power Minutes", 0);
            declineWorkout = new PieChart.Data("Declined Power Minutes", 0);
        }
        user_today_pie_chart.getData().add(acceptedWorkout);
        user_today_pie_chart.getData().add(declineWorkout);
        user_today_pie_chart.setTitle("Your Daily Stats");

        //user week stats
        if (0 < db_connector.getUserWeeklyAccepted() && 0 < db_connector.getUserWeeklyDeclined()) {
            acceptedWorkout = new PieChart.Data("Accepted Power Minutes", db_connector.getUserWeeklyAccepted());
            declineWorkout = new PieChart.Data("Declined Power Minutes", db_connector.getUserWeeklyDeclined());

        } else {
            acceptedWorkout = new PieChart.Data("Accepted Power Minutes", 0);
            declineWorkout = new PieChart.Data("Declined Power Minutes", 0);
        }
        user_week_pie_chart.getData().add(acceptedWorkout);
        user_week_pie_chart.getData().add(declineWorkout);
        user_week_pie_chart.setTitle("Your Weekly Stats");

        //user month stats
        if (0 < db_connector.getUserMonthlyAccepted() && 0 < db_connector.getUserMonthlyDeclined()) {
            acceptedWorkout = new PieChart.Data("Accepted Power Minutes", db_connector.getUserMonthlyAccepted());
            declineWorkout = new PieChart.Data("Declined Power Minutes", db_connector.getUserMonthlyDeclined());
        } else {
            acceptedWorkout = new PieChart.Data("Accepted Power Minutes", 0);
            declineWorkout = new PieChart.Data("Declined Power Minutes", 0);
        }
        user_month_pie_chart.getData().add(acceptedWorkout);
        user_month_pie_chart.getData().add(declineWorkout);
        user_month_pie_chart.setTitle("Your Monthly Stats");


        //if user does have partner get partners stats
        //partner today pie chart stats
        acceptedWorkout = new PieChart.Data("Accepted Power Minutes", db_connector.getPartnerDailyAccepted());
        declineWorkout = new PieChart.Data("Declined Power Minutes", db_connector.getPartnerDailyDeclined());
        partner_today_pie_chart.getData().add(acceptedWorkout);
        partner_today_pie_chart.getData().add(declineWorkout);
        partner_today_pie_chart.setTitle("Partner's Daily Stats");

        //partner weekly pie chart stats
        acceptedWorkout = new PieChart.Data("Accepted Power Minutes", db_connector.getPartnerWeeklyAccepted());
        declineWorkout = new PieChart.Data("Declined Power Minutes", db_connector.getPartnerWeeklyDeclined());
        partner_week_pie_chart.getData().add(acceptedWorkout);
        partner_week_pie_chart.getData().add(declineWorkout);
        partner_week_pie_chart.setTitle("Partner's Weekly Stats");

        //partner monthly pie chart stats
        acceptedWorkout = new PieChart.Data("Accepted Power Minutes", db_connector.getPartnerMonthlyAccepted());
        declineWorkout = new PieChart.Data("Declined Power Minutes", db_connector.getPartnerMonthlyDeclined());
        partner_month_pie_chart.getData().add(acceptedWorkout);
        partner_month_pie_chart.getData().add(declineWorkout);
        partner_month_pie_chart.setTitle("Partner's Monthly Stats");

        //if user does not have a partner then set labels
        if (0 == db_connector.getPartnerID()) {
            partner_today_label.setText("Add a partner to view their stats by entering their ID");
            partner_week_label.setText("Add a partner to view their stats by entering their ID");
            partner_month_label.setText("Add a partner to view their stats by entering their ID");
            partner_month_pie_chart.setLabelsVisible(false);
            partner_today_pie_chart.setLabelsVisible(false);
            partner_week_pie_chart.setLabelsVisible(false);
            partner_month_pie_chart.setLegendVisible(false);
            partner_week_pie_chart.setLegendVisible(false);
            partner_today_pie_chart.setLegendVisible(false);

        }

    }

    // INPUT:   none
    // TASK:    populates checkbox array list
    // CALLED:  in initialize()
    // OUTPUT:  none
    private void populateCheckboxList() {
        //add all checkboxes to list
        //MONDAY CHECKBOXES:
        check_box_list.add(0, mon_7_checkbox);
        check_box_list.add(1, mon_8_checkbox);
        check_box_list.add(2, mon_9_checkbox);
        check_box_list.add(3, mon_10_checkbox);
        check_box_list.add(4, mon_11_checkbox);
        check_box_list.add(5, mon_12_checkbox);
        check_box_list.add(6, mon_1_checkbox);
        check_box_list.add(7, mon_2_checkbox);
        check_box_list.add(8, mon_3_checkbox);
        check_box_list.add(9, mon_4_checkbox);
        check_box_list.add(10, mon_5_checkbox);
        //TUESDAY CHECKBOXES:
        check_box_list.add(11, tues_7_checkbox);
        check_box_list.add(12, tues_8_checkbox);
        check_box_list.add(13, tues_9_checkbox);
        check_box_list.add(14, tues_10_checkbox);
        check_box_list.add(15, tues_11_checkbox);
        check_box_list.add(16, tues_12_checkbox);
        check_box_list.add(17, tues_1_checkbox);
        check_box_list.add(18, tues_2_checkbox);
        check_box_list.add(19, tues_3_checkbox);
        check_box_list.add(20, tues_4_checkbox);
        check_box_list.add(21, tues_5_checkbox);
        //WEDNESDAY CHECKBOXES:
        check_box_list.add(22, wed_7_checkbox);
        check_box_list.add(23, wed_8_checkbox);
        check_box_list.add(24, wed_9_checkbox);
        check_box_list.add(25, wed_10_checkbox);
        check_box_list.add(26, wed_11_checkbox);
        check_box_list.add(27, wed_12_checkbox);
        check_box_list.add(28, wed_1_checkbox);
        check_box_list.add(29, wed_2_checkbox);
        check_box_list.add(30, wed_3_checkbox);
        check_box_list.add(31, wed_4_checkbox);
        check_box_list.add(32, wed_5_checkbox);
        //THURSDAY CHECKBOXES:
        check_box_list.add(33, thur_7_checkbox);
        check_box_list.add(34, thur_8_checkbox);
        check_box_list.add(35, thur_9_checkbox);
        check_box_list.add(36, thur_10_checkbox);
        check_box_list.add(37, thur_11_checkbox);
        check_box_list.add(38, thur_12_checkbox);
        check_box_list.add(39, thur_1_checkbox);
        check_box_list.add(40, thur_2_checkbox);
        check_box_list.add(41, thur_3_checkbox);
        check_box_list.add(42, thur_4_checkbox);
        check_box_list.add(43, thur_5_checkbox);
        //FRIDAY CHECKBOXES:
        check_box_list.add(44, fri_7_checkbox);
        check_box_list.add(45, fri_8_checkbox);
        check_box_list.add(46, fri_9_checkbox);
        check_box_list.add(47, fri_10_checkbox);
        check_box_list.add(48, fri_11_checkbox);
        check_box_list.add(49, fri_12_checkbox);
        check_box_list.add(50, fri_1_checkbox);
        check_box_list.add(51, fri_2_checkbox);
        check_box_list.add(52, fri_3_checkbox);
        check_box_list.add(53, fri_4_checkbox);
        check_box_list.add(54, fri_5_checkbox);
    }

    // INPUT:   none
    // TASK:    populates day array
    // CALLED:  in initialize()
    // OUTPUT:  none
    private void populateDayArray() {
        //day int to track current day in loop
        //start at 2 because monday == 2
        int day = 2;
        //for loop to populate day array in correct positions
        //we need this array set with the right days in each
        //index so it lines up with each checkbox
        for (int i = 1; i <= days_of_week_array.length; i++) {
            //set day at current index(minus 1 bc int i starts
            //at 1 but our array starts at 0.
            days_of_week_array[i - 1] = day;
            //if the index is divisible by 11 then we know we
            //placed all 11 hours in current day and its time to
            //increment to the next day
            if (i % 11 == 0) {
                day++;
            }
        }
    }

    // INPUT:   none
    // TASK:    populates hour array
    // CALLED:  in initialize()
    // OUTPUT:  none
    private void populateHourArray() {
        //hour int to track current hour in loop
        //starts at 7 bc thats the first time slot
        //available in a day
        int hour = 7;
        //for loop to populate hour array in correct positions
        //we need to have this array set with the right times
        //in each index so it lines up with each check box
        for (int i = 0; i < hour_of_day_array.length; i++) {
            //set hour at current index
            hour_of_day_array[i] = hour;
            //test print
            //System.out.println("Index: " + i + ", Hour : " + hour);
            //every time hour hits 17 we reset it to 7 for the next day
            if (17 == hour) {
                hour = 7;
                //else just increment hour by 1
            } else {
                hour++;
            }
        }
    }

    // INPUT:   none
    // TASK:    populates combo box with all the
    //          user unlocked quotes from  the file
    // CALLED:  in initialize()
    // OUTPUT:  none
    public void populateComboBox() {
        //keeps combo box true to size
        fav_quote_combobox.setMaxWidth(150.0);
        fav_quote_combobox.setPrefWidth(fav_quote_combobox.getMinWidth());
        try {
            //Read items from txt File
            BufferedReader reader = new BufferedReader(new InputStreamReader
                    (ClassLoader.getSystemClassLoader().getResourceAsStream("resources/quotes.txt")));
            StringBuilder sb = new StringBuilder();
            String line;
            int counter = 0;
            while ((line = reader.readLine()) != null && counter < current_level) {
                //Add Item
                fav_quote_combobox.getItems().add(line);

                //set most recent quote to level
                if (counter == (current_level - 1)) {
                    recent_quote_label.setText(line);
                }

                //sb.append(line);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //set selected item to whichever the user has selected previously
        fav_quote_combobox.getSelectionModel().select(fav_quote_index);
    }

    // INPUT:   a file
    // TASK:    create a scanner to a file of users choice
    // CALLED:  in readInLevelAndXP(), readInFavQuoteIndex()
    //          readInReminders(),
    // OUTPUT:  Scanner
    private Scanner getScanner(File f) {
        try {
            file_scanner = new Scanner(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return file_scanner;
    }

    // INPUT:   none
    // TASK:    calculate level by getting total power
    //          minutes completed.
    // CALLED:  in initialize and updateProgressBar()
    // OUTPUT:  none
    private void setCurrentLevel() {
        int total = db_connector.getUserTotalAccepted();

        if (total >= 1000) {
            current_level = 50;
            remainder = 20;
        } else if (0 == total) {
            current_level = 1;
            remainder = 0;
        } else if (0 == total % 20) {
            current_level = (total / 20) + 1;
            remainder = 20;
        } else if (0 != total % 20) {
            //get the remainder from total powerminutes
            remainder = total % 20;
            //subtract the remainder from total
            total -= remainder;
            //set current level to total divided by 20 + 1
            current_level = (total / 20) + 1;
        }

        //set level label to level from file
        current_level_label.setText(Integer.toString(current_level));
    }

    // INPUT:   none
    // TASK:    read in the fav_quote_index from file
    // CALLED:  in initialize()
    // OUTPUT:  none
    private void readInFavQuoteIndex() {
        File f = new File("resources/favquote.txt");

        file_scanner = getScanner(f);

        //read in favquote index
        String str = file_scanner.nextLine();
        fav_quote_index = Integer.parseInt(str);
        //sets fav quote index to combobox
        fav_quote_combobox.getSelectionModel().select(fav_quote_index);
        //sets label to selected combo box
        fav_quote_label.setText(fav_quote_combobox.getValue());
    }

    // INPUT:   none
    // TASK:    read in the reminders from file which hold
    //          the day on first line then hour on second
    // CALLED:  in initialize(), saveReminders()
    // OUTPUT:  none
    private void readInReminders() {
        //create the date format
        final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //create calendar instance for current day
        Calendar currentDayCalendar = Calendar.getInstance();
        //get current day of week as integer
        int currDay = currentDayCalendar.get(Calendar.DAY_OF_WEEK);
        //create calendar instance for reminders being read in
        Calendar reminderCalendar = Calendar.getInstance();

        //variables for reading in each reminder
        String checkboxIndexString;
        String newDayString;
        String newHourString;
        int checkboxIndexInt;
        int newDayInt;
        int newHourInt;

        //create file with fore reminders
        File f = new File("resources/reminders.txt");
        //get scanner
        file_scanner = getScanner(f);

        //while scanner has next loop through file
        while (file_scanner.hasNext()) {
            //get first line which holds index for check box
            checkboxIndexString = file_scanner.nextLine();
            //get second line which holds day
            newDayString = file_scanner.nextLine();
            //get third line which holds hour
            newHourString = file_scanner.nextLine();
            //parse them into ints
            checkboxIndexInt = Integer.parseInt(checkboxIndexString);
            newDayInt = Integer.parseInt(newDayString);
            newHourInt = Integer.parseInt(newHourString);
            reminderCalendar.set(Calendar.HOUR, newHourInt);
            //call the set checkbox method to mark each selected
            //checkbox as selected
            setCheckboxSelected(checkboxIndexInt);
            //if calender day is equal to day of reminder
            //then set reminder calendar to current time except
            //the hour to then figure out the difference between
            //reminder time and current time
            if (currDay == newDayInt) {
                reminderCalendar.set(Calendar.SECOND, 0);
                reminderCalendar.set(Calendar.MINUTE, 0);
                reminderCalendar.set(Calendar.AM_PM, Calendar.AM);
                reminderCalendar.set(Calendar.MONTH, currentDayCalendar.get(Calendar.MONTH));
                reminderCalendar.set(Calendar.DAY_OF_MONTH, currentDayCalendar.get(Calendar.DAY_OF_MONTH));
                reminderCalendar.set(Calendar.YEAR, currentDayCalendar.get(Calendar.YEAR));
                //get difference between current time and reminder time
                Long delay = reminderCalendar.getTimeInMillis() - currentDayCalendar.getTimeInMillis();
                //if delay is greater than 0 then we know the time
                //is still gonna happen today so we can set the reminder
                if (delay > 0) {
                    createPopUpReminder(delay);
                    System.out.println("difference in millis: " + delay);
                    System.out.println("SET A REMINDER FOR: " + newHourInt);
                }
            }
        }
    }

    // INPUT:   integer holding index for check box,
    // TASK:    selects every check box that is saved as
    //          selected.
    // CALLED:  in readInReminders()
    // OUTPUT:  none
    private void setCheckboxSelected(int i) {
        check_box_list.get(i).setSelected(true);
    }

    // INPUT:   none
    // TASK:    shutdown scheduled service if the user
    //          presses the save button. This prevents
    //          duplicate power minutes at the same time
    // CALLED:  in saveReminders()
    // OUTPUT:  none
    private void cancelPopUpReminder() {
        for (ScheduledExecutorService s : schedule_service_list) {
            s.shutdownNow();
            //schedule_service_list.remove(s);
        }
    }

    // INPUT:   none
    // TASK:    use a scheduled executor service to perform
    //          pop ups on specified time
    // CALLED:  in readInReminders()
    // OUTPUT:  none
    private void createPopUpReminder(long delay) {
        //Scheduled executor service to run the pop up every hour
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        schedule_service_list.add(ses);

        ses.schedule(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    FXMLLoader loader = null;
                    Stage myStage = new Stage();
                    Scene myScene;
                    try {
                        loader = new FXMLLoader(getClass().getResource("PopUpFXML.fxml"));
                        //loader.setController(new Notification());
                        myScene = new Scene(loader.load());
                    } catch (Exception e) {
                        System.out.println("Something went wrong while building new fxml");
                        System.out.println(e);
                        return;
                    }
                    myStage.setScene(myScene);
                    //set icon image
                    Image iconImage = new Image("file:test.png");
                    myStage.getIcons().add(iconImage);
                    myStage.initStyle(StageStyle.UNDECORATED);
                    myStage.initModality(Modality.APPLICATION_MODAL);
                    myStage.setResizable(false);

                    //get visual bounds of the screen
                    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

                    //set Stage boundaries to the lower right corner of the visible bounds of the main screen
                    myStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 550);
                    myStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 200);
                    myStage.setWidth(550);
                    myStage.setHeight(200);
                    myStage.showAndWait();
                });
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

    // INPUT:   none
    // TASK:    saves fav quote index to file
    // CALLED:  in FXML method setFavoriteQuote()
    // OUTPUT:  none
    private void saveFavQuoteIndex() {
        try (FileWriter fw = new FileWriter("resources/favquote.txt", false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(fav_quote_index);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // INPUT:   event, integer holding current index
    //          of pagination
    // TASK:    opens VideoViewerFXML with video file
    //          name at current index.
    // CALLED:  in createPage()
    // OUTPUT:  none
    private void openVideo(Event e, int index) {

        FXMLLoader loader = null;
        Stage myStage = new Stage();
        Scene myScene;
        try {
            loader = new FXMLLoader(getClass().getResource("VideoViewerFXML.fxml"));
            loader.setController(new VideoViewerController(video_file_names[index]));
            myScene = new Scene(loader.load());
        } catch (Exception e1) {
            System.out.println("Something went wrong while building new fxml!!!!!!");
            System.out.println(e1);
            return;
        }
        myStage.setTitle("Power Minute");
        javafx.scene.image.Image iconImage = new javafx.scene.image.Image("resources/test.png");
        myStage.getIcons().add(iconImage);
        myStage.initStyle(StageStyle.UNDECORATED);
        myStage.setScene(myScene);
        myStage.show();
    }

    // INPUT:   integer holding current index of
    //          pagination
    // TASK:    opens creates new page for
    //          pagination with the image file
    //          name at current index.
    // CALLED:  in createPagination()
    // OUTPUT:  Pane to put in pagination
    private Pane createPage(int pageIndex) {
        File file = new File(image_file_names[pageIndex]);
        Image image = new Image(file.toURI().toString());
        ImageView iv = new ImageView(image);
        iv.setOnMouseClicked(e -> openVideo(e, pageIndex));
        //iv.setFitWidth(600);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        Pane pageBox = new Pane();
        pageBox.setMinWidth(922.0);
        pageBox.setMinHeight(490.0);
        //pageBox.
        pageBox.getChildren().add(iv);
        return pageBox;
    }

    // INPUT:   none
    // TASK:    creates the pagination control
    //          and then sets each page to a
    //          picture of video.
    // CALLED:  in initialize()
    // OUTPUT:  none
    private void createPagination() {
        image_file_names = new String[6];
        image_file_names[0] = "resources/bunny.png";
        image_file_names[1] = "resources/jellyfish.png";
        image_file_names[2] = "resources/small.png";
        image_file_names[3] = "resources/bunny.png";
        image_file_names[4] = "resources/jellyfish.png";
        image_file_names[5] = "resources/small.png";

        video_file_names = new String[6];
        video_file_names[0] = "resources/test.mp4";
        video_file_names[1] = "resources/jellyfish.mp4";
        video_file_names[2] = "resources/small.mp4";
        video_file_names[3] = "resources/test.mp4";
        video_file_names[4] = "resources/jellyfish.mp4";
        video_file_names[5] = "resources/small.mp4";

        video_pagination = new Pagination(6, 0);
        video_pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        //video_pagination.setMinWidth(922);
        //video_pagination.setMinHeight(490);
        video_pagination.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
        video_pagination.setMinWidth(922);
        video_tab_pane.getChildren().addAll(video_pagination);
        video_tab_pane.setTopAnchor(video_pagination, 10.0);
        video_tab_pane.setRightAnchor(video_pagination, 10.0);
        video_tab_pane.setBottomAnchor(video_pagination, 10.0);
        video_tab_pane.setLeftAnchor(video_pagination, 10.0);
    }

//*********************************FXML METHODS******************************************

    // INPUT:    nothing
    // TASK:     ON ACTION FOR save_button
    //           checks if each reminder check box is
    //           selected or not and then writes to the file
    //           if it is selected.
    // OUTPUT:   nothing
    @FXML
    private void saveReminders() {
        //shutdown all previous executors scheduled
        cancelPopUpReminder();
        try (FileWriter fw = new FileWriter("resources/reminders.txt", false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            //loop through all check boxes
            for (int i = 0; i < check_box_list.size(); i++) {
                //if current checkbox is selected then write the day and hour to file
                if (check_box_list.get(i).isSelected()) {
                    System.out.println("Saved a reminder for: day = " + days_of_week_array[i] +
                            " hour = " + hour_of_day_array[i]);
                    out.println(i);
                    out.println(days_of_week_array[i]);
                    out.println(hour_of_day_array[i]);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //call read in reminders to set any new reminders saved
        readInReminders();
    }

    // INPUT:    nothing
    // TASK:     ON ACTION FOR CLICKING REWARDS_TAB.
    //           sets level bar to the current xp from
    //           the singleton object. also sets the
    //           level label to current level from
    //           singleton object.
    // OUTPUT:   nothing
    @FXML
    public void updateProgressBar() {
        setCurrentLevel();
        //update progress bar
        level_bar.setProgress(remainder / 20.0);
    }

    // INPUT:    nothing
    // TASK:     ON ACTION FOR CLICKING fav_quote_combobox.
    //           sets the label and index to the newly selected
    //           combo box choice.
    // OUTPUT:   nothing
    @FXML
    private void setFavoriteQuote() {
        fav_quote_label.setText(fav_quote_combobox.getValue());
        fav_quote_index = fav_quote_combobox.getSelectionModel().getSelectedIndex();
        saveFavQuoteIndex();
    }

    // INPUT:    nothing
    // TASK:     ON ACTION FOR CLICKING Progress_Tab.
    //           sets all the pie charts stats
    // OUTPUT:   nothing
    @FXML
    private void updatePieCharts() {
        //update user daily stats
        user_today_pie_chart.getData().get(0).setPieValue(db_connector.getUserDailyAccepted());
        user_today_pie_chart.getData().get(1).setPieValue(db_connector.getUserDailyDeclined());

        //update user weekly stats
        user_week_pie_chart.getData().get(0).setPieValue(db_connector.getUserWeeklyAccepted());
        user_week_pie_chart.getData().get(1).setPieValue(db_connector.getUserWeeklyDeclined());

        //update user monthly stats
        user_month_pie_chart.getData().get(0).setPieValue(db_connector.getUserMonthlyAccepted());
        user_month_pie_chart.getData().get(1).setPieValue(db_connector.getUserMonthlyDeclined());

        if (0 < db_connector.getPartnerID()) {
            //update partner daily stats
            partner_today_pie_chart.getData().get(0).setPieValue(db_connector.getPartnerDailyAccepted());
            partner_today_pie_chart.getData().get(1).setPieValue(db_connector.getPartnerDailyDeclined());

            //update partner weekly stats
            partner_week_pie_chart.getData().get(0).setPieValue(db_connector.getPartnerWeeklyAccepted());
            partner_week_pie_chart.getData().get(1).setPieValue(db_connector.getPartnerWeeklyDeclined());

            //update partner monthly stats
            partner_month_pie_chart.getData().get(0).setPieValue(db_connector.getPartnerMonthlyAccepted());
            partner_month_pie_chart.getData().get(1).setPieValue(db_connector.getPartnerMonthlyDeclined());

            //make sure labels are blank:
            partner_today_label.setText("");
            partner_week_label.setText("");
            partner_month_label.setText("");

            //set pie chart visibility to true for partner incase it wasnt before
            //if someone adds a partner then this will work
            partner_month_pie_chart.setLabelsVisible(true);
            partner_today_pie_chart.setLabelsVisible(true);
            partner_week_pie_chart.setLabelsVisible(true);
            partner_month_pie_chart.setLegendVisible(true);
            partner_week_pie_chart.setLegendVisible(true);
            partner_today_pie_chart.setLegendVisible(true);
        }
    }


    @FXML
    private void openProfile() {
        Stage primaryStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("ProfileFXML.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Profile");
        //set icon image
        javafx.scene.image.Image iconImage = new javafx.scene.image.Image("resources/test.png");
        primaryStage.getIcons().add(iconImage);
        primaryStage.setScene(new Scene(root, 300, 260));
        primaryStage.show();
    }

    // INPUT:   url and Resource Bundle
    // TASK:    called when class is started, this makes it where we
    //          can call functions and perform actions when the controller
    //          is first called.
    // OUTPUT:  none
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCurrentLevel();

        //call population methods for arrays:
        populateCheckboxList();
        populateDayArray();
        populateHourArray();
        populateComboBox();

        //set the tab titles
        setTabTitles();

        //populate pie charts
        setPieCharts();

        //read in the reminders from file
        readInReminders();

        //read in the fav quote index
        readInFavQuoteIndex();

        //create pagination
        createPagination();

        //used for testing reminders -- delete when done
        //createPopUpReminder(10000);
        //createPopUpReminder(30000);
        //createPopUpReminder(50000);
    }
}
