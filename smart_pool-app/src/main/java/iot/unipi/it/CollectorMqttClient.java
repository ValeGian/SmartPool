package iot.unipi.it;

import java.util.Map;
import java.util.HashMap;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class CollectorMqttClient implements MqttCallback{
	
	private final String broker = "tcp://127.0.0.1:1883";
	private final String clientId = "JavaApp";
	
	private final String tempSubTopic = "temperature";
	private final String tempPubTopic = "temperature-actuator";
	
	private final String clSubTopic = "chlorine";
	private final String clPubTopic = "chlorine-actuator";
	
	private MqttClient mqttClient = null;
	private static final ConfigurationReader configurationReader = ConfigurationReader.getInstance();
	
	// Limit temperatures in Celsius
	private final int lowerTemp = configurationReader.getLowerTemp();
	private final int upperTemp = configurationReader.getUpperTemp();
	private boolean tempActOn = true;
	private Map<String, Double> recentTempSamples = new HashMap();
	private double desiredLowerTemp = lowerTemp;
	private double desiredUpperTemp = upperTemp;
	
	// Limit chlorine levels in ppb (parts per billion)
	private final int lowerCl = configurationReader.getLowerCl();
	private final int upperCl = configurationReader.getUpperCl();
	private boolean clActOn = true;
	private Map<String, Double> recentClSamples = new HashMap();
	private double desiredLowerCl = lowerCl;
	private double desiredUpperCl = upperCl;
	
	public CollectorMqttClient() throws InterruptedException {
		do {
			try {
				this.mqttClient = new MqttClient(this.broker,this.clientId);
		        System.out.println("Connecting to broker: "+broker);
		        
				this.mqttClient.setCallback( this );
				this.mqttClient.connect();
				
				this.mqttClient.subscribe(this.tempSubTopic);
		        System.out.println("Subscribed to topic: "+this.tempSubTopic);
		        
				this.mqttClient.subscribe(this.clSubTopic);
		        System.out.println("Subscribed to topic: "+this.clSubTopic);
			}catch(MqttException me) {
				System.out.println("I could not connect, Retrying ...");
			}
		}while(!this.mqttClient.isConnected());
	}
	
	public void publish(final String topic, final String content) throws MqttException{
		try {
			MqttMessage message = new MqttMessage(content.getBytes());
			this.mqttClient.publish(topic, message);
		} catch(MqttException me) {
			me.printStackTrace();
		}
	}
	
	public void connectionLost(Throwable cause) {
		System.out.println("Connection is broken: " + cause);
		int timeWindow = 3000;
		while (!this.mqttClient.isConnected()) {
			try {
				System.out.println("Trying to reconnect in " + timeWindow/1000 + " seconds.");
				Thread.sleep(timeWindow);
				System.out.println("Reconnecting ...");
				timeWindow *= 2;
				this.mqttClient.connect();
				
				this.mqttClient.subscribe(this.tempSubTopic);
				this.mqttClient.subscribe(this.clSubTopic);
				System.out.println("Connection is restored");
			}catch(MqttException me) {
				System.out.println("I could not connect");
			} catch (InterruptedException e) {
				System.out.println("I could not connect");
			}
		}
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		byte[] payload = message.getPayload();
		
		try {
			JSONObject sensorMessage = (JSONObject) JSONValue.parseWithException(new String(payload));
			
			if(topic.equals(this.tempSubTopic)) 
			{
				if (sensorMessage.containsKey("node")
					&& sensorMessage.containsKey("temperature")
					&& sensorMessage.containsKey("unit")
					) {
					Double numericValue = Double.parseDouble(sensorMessage.get("temperature").toString());
					String nodeId = sensorMessage.get("node").toString();
					
					String unit = sensorMessage.get("unit").toString();
					if(unit.equals("F")) {
						numericValue = convertFToC(numericValue);
					}
					
					recentTempSamples.put(nodeId, numericValue);
					SmartPoolDbManager.logTemperatureSample(nodeId, numericValue);
					
					double averageTemp = getAverageTemperature();
					if (averageTemp < desiredLowerTemp) {
						if(!tempActOn) {
							System.out.println("Water temperature is too low! Switch on actuator");
							publish(this.tempPubTopic, "ON");
							tempActOn = true;
							SmartPoolDbManager.logTemperatureActuator(tempActOn);
						} else {
							System.out.println("Water temperature is too low but the Actuator is already on: it will increase!\n");
						}
					} else if(averageTemp > (desiredLowerTemp + desiredUpperTemp) / 2) { 
						if(tempActOn) {
							System.out.println("Water temperature is good! Switch off actuator");
							publish(this.tempPubTopic, "OFF");
							tempActOn = false;
							SmartPoolDbManager.logTemperatureActuator(tempActOn);
						} else {
							if(averageTemp < desiredUpperTemp) {
								System.out.println("Water temperature is safe!\n");
							} else {
								System.out.println("Water temperature is too high but decreasing!\n");
							}
						}
					}
				} else {
					System.out.println("Garbage data from sensor");
				}	
			} else if (topic.equals(this.clSubTopic)) {
				if (sensorMessage.containsKey("node")
					&& sensorMessage.containsKey("chlorine")
					) {
					Double numericValue = Double.parseDouble(sensorMessage.get("chlorine").toString());
					String nodeId = sensorMessage.get("node").toString();
					
					recentClSamples.put(nodeId, numericValue);
					SmartPoolDbManager.logChlorineSample(nodeId, numericValue);
					
					double averageCl = getAverageChlorine();
					if (averageCl < desiredLowerCl) {
						if(!clActOn) {
							System.out.println("Chlorine level is too low! Switch on actuator");
							publish(this.clPubTopic, "ON");
							clActOn = true;
							SmartPoolDbManager.logChlorineActuator(clActOn);
						} else {
							System.out.println("Chlorine level is too low but the Actuator is already on: it will increase!\n");
						}
					} else if(averageCl > (desiredLowerCl + desiredUpperCl) / 2) { 
						if(clActOn) {
							System.out.println("Chlorine level is good! Switch off actuator");
							publish(this.clPubTopic, "OFF");
							clActOn = false;
							SmartPoolDbManager.logChlorineActuator(clActOn);
						} else {
							if(averageCl < desiredUpperCl) {
								System.out.println("Chlorine level is safe!\n");
							} else {
								System.out.println("Chlorine level is too high but decreasing!\n");
							}
						}
					}
				} else {
					System.out.println("Garbage data from sensor");
				}	
			} else {
				System.out.println(String.format("Unknown topic: [%s] %s", topic, new String(payload)));
			}
		} catch (ParseException e) {
			System.out.println(String.format("Received badly formatted message: [%s] %s", topic, new String(payload)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("Delivery Complete\n");
		
	}
	
	private double convertFToC(final double fahrenheit) {
		return (fahrenheit - 32) * 5 / 9;
	}	
	
	private double averageSampleValue(final Map<String, Double> samples) {
		int sum = 0;
		int num = 0;
		for(Map.Entry<String, Double> sample: samples.entrySet()) {
			sum += sample.getValue();
			num++;
		}
		return (double) sum / num;
	}
	
	public double getAverageTemperature() {
		return averageSampleValue(this.recentTempSamples);
	}
	
	public double getAverageChlorine() {
		return averageSampleValue(this.recentClSamples);
	}
	
	public void setChlorineBounds(double desiredLower, double desiredUpper) {
		if (desiredLower < lowerCl || desiredUpper > upperCl) {
			System.out.format("The chlorine level must stay between %d and %d ppb", lowerCl, upperCl);
		} else {
			this.desiredLowerCl = desiredLower;
			this.desiredUpperCl = desiredUpper;
			System.out.println("New chlorine bounds are ["+this.desiredLowerCl+";"+this.desiredUpperCl+"]");
		}
	}
	
	public void setTemperatureBounds(double desiredLower, double desiredUpper, final String unit) {
		if (unit.equals("F")) {
			desiredLower = convertFToC(desiredLower);
			desiredUpper = convertFToC(desiredUpper);
		}
		
		if (desiredLower < lowerTemp || desiredUpper > upperTemp) {
			System.out.format("The temperature must stay between %d and %d °C", lowerTemp, upperTemp);
		} else {
			this.desiredLowerTemp = desiredLower;
			this.desiredUpperTemp = desiredUpper;
			System.out.println("New temperature bounds are ["+this.desiredLowerTemp+";"+this.desiredUpperTemp+"]");
		}
	}
	
	public int getNumberOfTemperatureSensors() {
		return recentTempSamples.size();
	}
	
	public int getNumberOfChlorineSensors() {
		return recentClSamples.size();
	}
	
	public void stampTemperatureSensors() {
		stampSensors(recentTempSamples);
	}
	
	public void stampChlorineSensors() {
		stampSensors(recentClSamples);
	}
	
	private void stampSensors(final Map<String, Double> samples) {
		for(Map.Entry<String, Double> sample: samples.entrySet()) {
			System.out.println("> " + sample.getKey() + "\n");
		}
	}
}
