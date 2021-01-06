package model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public class AnimatedObject extends Object{
	
	private ArrayList<Animation> animations;
	protected int currentFrame, currentAnimation;
	private long timer;
	protected boolean mirrored;

	public AnimatedObject(int _x, int _y, String filePath, int nFrames) {
		super(_x, _y, filePath);
		
		width /= nFrames;
		currentAnimation = currentFrame = 0;
		animations = new ArrayList<>();	
		timer = 0;
		mirrored = false;
	}
	
	public void addAnimation(int start, int end, int delay, boolean loop) {
		animations.add(new Animation(start, end, delay, loop));
	}
	
	public void changeAnimation(int animNum) {
		if(currentAnimation == animNum)
			return;
		
		currentAnimation = animNum;
		currentFrame = getCurrentStartFrame();
		timer = 0;
	}
	
	public int getCurrentStartFrame() {
		return animations.get(currentAnimation).getStartFrame();
	}
	
	public int getCurrentEndFrame() {
		return animations.get(currentAnimation).getEndFrame();
	}
	
	public void draw(Graphics2D g2d) {
		if(animations.isEmpty() || !visible)
			return;
		
		int portionToDraw_X = currentFrame * width;
		int portionToDraw_Width = portionToDraw_X + width;
		
		if(!mirrored)
			g2d.drawImage(image, x, y, x+width, y+height, portionToDraw_X, 0, portionToDraw_Width, height, null);
		else
			g2d.drawImage(image, x, y, x+width, y+height, portionToDraw_Width, 0, portionToDraw_X, height, null);
	}
	
	public void animate() {
		if(isEnded())
			return;
		
		if(timer == 0)
			timer = System.currentTimeMillis();
		
		if(System.currentTimeMillis() > timer + animations.get(currentAnimation).getDelay()) {
			timer = System.currentTimeMillis();
			
			if(currentFrame == getCurrentEndFrame()) {
				
				if(animations.get(currentAnimation).isLooping())
					currentFrame = getCurrentStartFrame();
			}
			else
				++currentFrame;
		}

	}
	
	public boolean isEnded() {
		return currentFrame == getCurrentEndFrame() && !animations.get(currentAnimation).isLooping();
	}

}
