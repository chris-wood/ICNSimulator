/**
 * @file sim.java
 * @brief TODO
 *
 * <long>
 *
 * @author Christopher A. Wood, woodc1@uci.edu
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.ho.yaml.Yaml;

import simulation.Config;
import simulation.Util;

public class CCNSim {

	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.err.println("Usage: java Boomerang config");
			System.exit(-1);
		}
		
		try
		{
			// Parse the config file and run the simulator
			Config config = Yaml.loadType(new File(args[0]), Config.class);
			Util.error(config.toString());
			
			// Run the simulator
			// TODO
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.err.println("File not found: " + e.getMessage());
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			System.err.println("Number parsing exception occurred: " + e.getMessage());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("IO error occurred: " + e.getMessage());
		}
	}
	
}
