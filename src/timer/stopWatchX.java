package timer;

/* Timer for animation... not for timing game time or longer things like that. 
 * Seems to be accurate to no worse than 1.5uSecs off 
*/
 
public class stopWatchX{
	// Fields
	private long start;
	private long duration;
	
	// Constructors
	public stopWatchX(long duration){
		this.duration = (duration * 1000000);
		resetWatch();
	}
	
	public stopWatchX(long duration, long elapsed){
		this.duration = (duration * 1000000);
		elapsed = (elapsed * 1000000);
		start = System.nanoTime() - elapsed;
	}
	
	// Methods
	public long getElapsed(){
		long current = System.nanoTime();
		long currentMillis = (current / 1000000);
		long startMillis = (start / 1000000);
		return (currentMillis - startMillis);
	}
	
	public long getRemaining(){
		long dur = (duration / 1000000);
		return dur - getElapsed();
	}
	
	public void resetWatch(){
		start = System.nanoTime();
	}
	
	public boolean isTimeUp(){
		long current = System.nanoTime();
		int currentMillis = (int) (current / 1000000);
		int startMillis = (int) (start / 1000000);
		int durationMillis = (int) (duration / 1000000);
		if((currentMillis - startMillis) >= durationMillis){
			return true;
		}
		return false;
	}
}
