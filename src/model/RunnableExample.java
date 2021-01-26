package model;

public class RunnableExample implements Runnable{

	@Override
	public void run() {
		while(true)
			System.out.println("I'm running");
	}

}
