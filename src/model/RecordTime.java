package model;

import java.sql.Time;

public class RecordTime {

    private long 	startTime;
    private long 	stopTime;
    private boolean isRunning;
    private long 	elapsed;

    public RecordTime(){
        startTime = 0;
        stopTime  = 0;
        elapsed   = 0;
        isRunning = false;
    }

    public void start(){
        startTime = System.currentTimeMillis();
        isRunning = true;
    }

    public void stop(){
        stopTime  = System.currentTimeMillis();
        isRunning = false;
    }

    public long getCurrentMinute(){
        if (isRunning){
            elapsed = (System.currentTimeMillis() - startTime);
        }else{
            elapsed = stopTime-startTime;
        }
        return ((elapsed/1000)/60);
    }

    public long getCurrentSecond(){
        if (isRunning){
            elapsed = (System.currentTimeMillis() - startTime);
        }else{
            elapsed = stopTime-startTime;
        }
        return ((int)(elapsed/1000));
    }
    
    public void getTime() {
    	Time t = new Time(elapsed);
    	System.out.println(t);
    }

}
