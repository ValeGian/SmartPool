package iot.unipi.it;

public class TemperatureCollector {

	private CollectorMqttClient mc;
	
	
	public TemperatureCollector(CollectorMqttClient mc) {
		this.mc = mc;
	}
	
	public double getAverageTemperature() {
		return this.mc.getAverageTemperature();
	}
	
	public void setTemperatureBounds(final double lowerBound, final double upperBound, final String unit) {
		mc.setTemperatureBounds(lowerBound, upperBound, unit);
	}
}
