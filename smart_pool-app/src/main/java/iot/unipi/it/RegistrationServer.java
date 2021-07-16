package iot.unipi.it;

import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
//import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class RegistrationServer extends CoapServer{
	private final static CoapNetworkHandler coapHandler = CoapNetworkHandler.getInstance();
	
	public RegistrationServer() throws SocketException {
		this.add(new RegistrationResource());
	}
	
	public int getNumberOfPresenceSensors(){
		return coapHandler.getNumberOfPresenceSensors();
	}
	
	public void stampPresenceSensors() {
		coapHandler.stampPresenceSensors();
	}
	
	
	class RegistrationResource extends CoapResource{
		
		public RegistrationResource() {
			super("registration");
		}
		
		@Override
		public void handlePOST(CoapExchange exchange) {
			String deviceType = exchange.getRequestText();
			String ipAddress = exchange.getSourceAddress().getHostAddress();
			boolean success = true;

			if(deviceType.equals("hydromassage_actuator")) 
				success = coapHandler.setHydromassageActuator(ipAddress);
			else if(deviceType.equals("presence_sensor"))
				coapHandler.addPresenceSensor(ipAddress);
			
			if(success) 
				exchange.respond(ResponseCode.CREATED, "Registration Completed!".getBytes(StandardCharsets.UTF_8));
			else
				exchange.respond(ResponseCode.NOT_ACCEPTABLE, "Registration not allowed!".getBytes(StandardCharsets.UTF_8));
		}
		
		@Override
		public void handleDELETE(CoapExchange exchange) {
			String[] request = exchange.getRequestText().split("-");
			String ipAddress = request[0];
			String deviceType = request[1];
			boolean success = true;;
			
			if(deviceType.equals("hydromassage_actuator")) 
				success = coapHandler.deleteHydromassageActuator(ipAddress);
			else if(deviceType.equals("presence_sensor"))
				coapHandler.deletePresenceSensor(ipAddress);
			
			if(success) 
				exchange.respond(ResponseCode.DELETED, "Cancellation Completed!".getBytes(StandardCharsets.UTF_8));
			else
				exchange.respond(ResponseCode.BAD_REQUEST, "Cancellation not allowed!".getBytes(StandardCharsets.UTF_8));
		}
	}


	public void updatePower(int power) {
		coapHandler.updatePower(power);
		
	}
}
