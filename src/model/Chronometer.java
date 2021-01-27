package model;

import java.util.HashMap;

public class Chronometer {

    private int minutes;
    private int seconds;
    private boolean onPause;
    long currentTime;


    public Chronometer(){
        minutes = 0;
        seconds = 0;
        onPause = false;
        currentTime = 0;
    }

    public void updateTime(){

        if(System.currentTimeMillis() - currentTime > 1000 && !onPause) {
            currentTime = System.currentTimeMillis();
            seconds++;
            if (seconds >= 60) {
                seconds = 0;
                minutes++;
            }
        }

    }

    public void updatePause(){
        onPause = !onPause;
    }



    public void resetTime(){

        seconds = 0;
        minutes = 0;
        currentTime = 0;
        onPause = false;
    }




    public HashMap<String, Integer> getTime(){


        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("minutes", minutes);
        map.put("seconds", seconds);

        return map;
    }


}
