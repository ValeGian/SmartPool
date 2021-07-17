package iot.unipi.it;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;

public class Collector {
	
	public static void main(String[] args) throws SocketException, InterruptedException{
		
		CollectorMqttClient mc = new CollectorMqttClient();
		TemperatureCollector tc = new TemperatureCollector(mc);
		ChlorineCollector cc = new ChlorineCollector(mc);
		
		RegistrationServer rs = new RegistrationServer();
		rs.start();
		
		// to read inputs from terminal
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String command = "";
		String[] tokens;
		
		System.out.println("\nCommand list:");
		System.out.println("!exit: exit the program");
		System.out.println("!commands: list possible commands");					
		System.out.println("!checkTemp: get current average temperature");
		System.out.println("!setTemp <lower temperature> <upper temperature> <unit[C or F]>: set desired temperature bounds");
		System.out.println("!checkCl: get current average chlorine level");
		System.out.println("!setCl <lower level> <upper level>: set desired chlorine level bounds");
		System.out.println("!setPowerHydro <new power>: set power between 1 and 10");
		System.out.println("!getSensorsList: show the list of all sensors available");
		System.out.println("\n");
		
		while(true) {
			try {
				command = reader.readLine();
				tokens = command.split(" ");
				
				if (tokens[0].equals("!exit")) 
				{
					System.exit(1);
				} else if (tokens[0].equals("!commands")) 
				{
					System.out.println("Command list:");
					System.out.println("!exit: exit the program");
					System.out.println("!commands: list possible commands");					
					System.out.println("!checkTemp: get current average temperature");
					System.out.println("!setTemp <lower temperature> <upper temperature> <unit[C or F]>: set desired temperature bounds");
					System.out.println("!checkCl: get current average chlorine level");
					System.out.println("!setCl <lower level> <upper level>: set desired chlorine level bounds");
					System.out.println("!setPowerHydro <new power>: set power between 1 and 10");
					System.out.println("!getSensorsList: show the list of all sensors available");
					
					
				} else if (tokens[0].equals("!checkTemp")) 
				{
					System.out.format("The temperature in the pool is of %f °C", tc.getAverageTemperature());
					
				} else if (tokens[0].equals("!setTemp"))
				{
					tc.setTemperatureBounds(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), tokens[3]);
					
				} else if (tokens[0].equals("!checkCl")) 
				{
					System.out.format("The chlorine level in the pool is of %f ppb", cc.getAverageChlorine());
					
				} else if (tokens[0].equals("!setCl"))
				{
					cc.setChlorineBounds(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
					
				} else if (tokens[0].equals("!setPowerHydro")) 
				{
					int power;

					try {
						power = Integer.parseInt(tokens[1]);
					}catch (Exception ex) {
						System.out.println("Insert a correct power");
						continue;
					}
					
					if(power > 10 || power < 1)
						System.out.println("Not valid range of power");
					else {
						System.out.println("Power update");	
						rs.updatePower(power);
					}
				} else if (tokens[0].equals("!getSensorsList"))
				{
					System.out.println("There are " + rs.getNumberOfPresenceSensors() + " sensor:\n");
					rs.stampPresenceSensors();
					
					System.out.println("Temperature sensors:\n");
					mc.stampTemperatureSensors();
					
					System.out.println("Ph sensors:\n");
					mc.stampChlorineSensors();
					
				} 

				System.out.println("\n");
				
			} catch (IOException e) {
				System.out.println("Error while reading the command; please retry!");
			}
		}
	}
}
	
