package simulation;

public class Clock
{
	public static long time;
	public static long endTime;
	public boolean tick = true;
	
	public static void initialize(long endTime)
	{
		time = 0L;
		endTime = 0L;
	}
	
	public static boolean isTimeLeft() {
		return (time < endTime);
	}
	
	public static void tick()
	{
		time++;
	}
}
