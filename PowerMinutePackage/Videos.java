package PowerMinutePackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Videos {

    //*********************************MEMBER VARIABLES******************************************
    private static Videos video_retriever = null;
    ArrayList<String> videos_array = new ArrayList<>();
    Random random_generator;
    int random_number;

    //*********************************CLASS METHODS******************************************

    // INPUT:   none
    // TASK:    constructor of class
    // OUTPUT:  none
    public Videos(){
        random_generator = new Random();
        readInVideos();
    }

    // INPUT:   none
    // TASK:    returns the static object
    // OUTPUT:  video class object
    public static Videos getInstance(){
        if(null == video_retriever)
            video_retriever = new Videos();

        return video_retriever;
    }

    // INPUT:   none
    // TASK:    reads in all the video links from the file
    // OUTPUT:  none
    public void readInVideos(){
        try {
            //Read items from txt File
            BufferedReader reader = new BufferedReader(new InputStreamReader
                    (ClassLoader.getSystemClassLoader().getResourceAsStream("resources/videos.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                videos_array.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // INPUT:   none
    // TASK:    uses a random number generator to select a video
    //          to display for the power minute
    // OUTPUT:  string with video url
    public String getRandomVideo(){
        //get a random number from 0 to the size of the array
        random_number = random_generator.nextInt(videos_array.size());

        return videos_array.get(random_number);
    }
}
