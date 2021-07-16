package iot.unipi.it;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;

public class CoapNetworkHandler {
	
	private List<CoapClient> clientPresenceSensorList = new ArrayList<CoapClient>();
	private List<CoapObserveRelation> observePresenceList = new ArrayList<CoapObserveRelation>();
	
	private CoapClient clientHydromassageActuator;
	private String ipActuator;
	private int powerHydro = 5;
	
	private static CoapNetworkHandler instance = null;
	
	
	public static CoapNetworkHandler getInstance()
    {
        if (instance == null)
            instance = new CoapNetworkHandler();

        return instance;
    } 
	
	public boolean setHydromassageActuator(String ipAddress) {
		if(clientHydromassageActuator == null) {
			System.out.println("The hydromassage actuator: [" + ipAddress + "] + is now registered");
			clientHydromassageActuator = new CoapClient("coap://[" + ipAddress + "]/hydromassage_actuator");
			ipActuator = ipAddress;
			return true;
		}else
			return false;
	}
	
	
	public void addPresenceSensor(String ipAddress) {
		
		System.out.println("The presence sensor: [" + ipAddress + "] + is now registered");
		CoapClient newPresenceSensor = new CoapClient("coap://[" + ipAddress + "]/presence_sensor");
	
		
		CoapObserveRelation newObservePresence = newPresenceSensor.observe(
				new CoapHandler() {
					
					public void onLoad(CoapResponse response) {
						String responseString = response.getResponseText();
						
						if (responseString.equals("ON")) {
							System.out.println("Movement detected: hydromassage turned on");
							System.out.println("");
							SmartPoolDbManager.logPersonInThePool(true);
							SmartPoolDbManager.logHydromassageON(powerHydro);						
							toggleHydromassage("on");
						}else if (responseString.equals("OFF")){
							System.out.println("No movement: hydromassage turned off");
							System.out.println("");
							SmartPoolDbManager.logPersonInThePool(false);
							SmartPoolDbManager.logHydromassageOFF();
							toggleHydromassage("off");
						}
					}			
					
					public void onError() {
						System.err.println("OBSERVING FAILED");
					}
				});
		clientPresenceSensorList.add(newPresenceSensor);
		observePresenceList.add(newObservePresence);
	}
	
	
	private void toggleHydromassage(String mode) {
		if(ipActuator != null) {
			clientHydromassageActuator.put(new CoapHandler() {

                public void onLoad(CoapResponse response) {
                	if (response != null) {
        				if(!response.isSuccess())
        		        	System.out.println("Something went wrong with hydromassage");
        	        }
                }
                
                public void onError() {
                    System.err.println("[ERROR: HydromassageActuator " + ipActuator + "] ");
                }

            }, mode, MediaTypeRegistry.TEXT_PLAIN);
		}	
	}

	public void cutAllConnection() {
		for(CoapObserveRelation relationToCancel: observePresenceList)
			relationToCancel.proactiveCancel();
	}

	
	public boolean deleteHydromassageActuator(String ipAddress) {
		if(clientHydromassageActuator.getURI().contentEquals(ipAddress)) {
			clientHydromassageActuator = null;
			return true;
		}
		return false;
	}

	
	public boolean deletePresenceSensor(String ipAddress) {
		for(int i = 0; i < clientPresenceSensorList.size(); i++) 
			if(clientPresenceSensorList.get(i).getURI().equals(ipAddress)) {
				clientPresenceSensorList.remove(i);
				observePresenceList.get(i).proactiveCancel();
				observePresenceList.remove(i);
				return true;
			}
	return false;
		
	}
	
	public int getNumberOfPresenceSensors() {
		return clientPresenceSensorList.size();
	}
	
	public void stampPresenceSensors() {	
		for(CoapClient cc: clientPresenceSensorList)
			System.out.println("> " + cc.getURI() + "\n");
	}

	public void updatePower(int power) {
		if(ipActuator != null) {
			powerHydro = power;
			SmartPoolDbManager.logHydromassageON(powerHydro);
			String messageToSend = "power=" + power;
			clientHydromassageActuator.put(new CoapHandler() {

                public void onLoad(CoapResponse response) {
                	if (response != null) {
        				if(!response.isSuccess())
        		        	System.out.println("Something went wrong with hydromassage");
        	        }
                }
                
                public void onError() {
                    System.err.println("[ERROR: HydromassageActuator " + ipActuator + "] ");
                }

            }, messageToSend, MediaTypeRegistry.TEXT_PLAIN);
		}	

		
	}
	
	
}
