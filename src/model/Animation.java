package model;

public class Animation {
	
	private int startFrame, endFrame, delay;
	private boolean loop;
	
	public Animation(int s, int e, int d, boolean l) {
		startFrame = s;
		endFrame = e;
		delay = d;
		loop = l;		
	}
	
	int getStartFrame() {
		return startFrame;
	}
	
	int getEndFrame() {
		return endFrame;
	}
	
	int getDelay() {
		return delay;
	}
	
	void setLooping(boolean l) {
		loop = l;
	}
	
	boolean isLooping() {
		return loop;
	}
}
