
public class Util
{
	public static void disp(String s)
	{
		System.out.println("[" + Clock.time + "] " + s);
	}
	
	public static void error(String s)
	{
		System.err.println("[" + Clock.time + "]> " + s);
	}
}
