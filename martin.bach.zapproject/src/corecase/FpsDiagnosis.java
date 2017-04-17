package corecase;

public class FpsDiagnosis {

	private static final int FRAMES_PER_MESSURE = 10;

	private int fps;
	private long lastTime;
	private int messureFrame;

	public FpsDiagnosis() {

	}

	public int update() {
		
		messureFrame++;
		
		if (messureFrame == FRAMES_PER_MESSURE) {
			messureFrame = 0;
			long dt = System.currentTimeMillis() - lastTime;
			fps = (int) (10.0f / (dt / 1000.0f));
			lastTime = System.currentTimeMillis();
		}
		
		return fps;
	}

}
