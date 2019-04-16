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
    public Videos(){
        random_generator = new Random();
        readInVideos();
    }

    public static Videos getInstance(){
        if(null == video_retriever)
            video_retriever = new Videos();

        return video_retriever;
    }

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

    public String getRandomVideo(){
        //get a random number from 0 to the size of the array
        random_number = random_generator.nextInt(videos_array.size());

        return videos_array.get(random_number);
    }
}
