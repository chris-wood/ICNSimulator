public class Clock
{
	public static long time;
	public boolean tick = true;
	
	public Clock()
	{
		time = 0L;
	}
	
	public void tick()
	{
		time++;
		if (tick) Util.error("tick...");
		else Util.error("tock...");
		tick = !tick;
	}
}
