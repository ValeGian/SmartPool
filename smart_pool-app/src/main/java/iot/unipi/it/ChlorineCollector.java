package iot.unipi.it;

public class ChlorineCollector {

	private CollectorMqttClient mc;
	
	
	public ChlorineCollector(CollectorMqttClient mc) {
		this.mc = mc;
	}
	
	public double getAverageChlorine() {
		return this.mc.getAverageChlorine();
	}
	
	public void setChlorineBounds(final double lowerBound, final double upperBound) {
		mc.setChlorineBounds(lowerBound, upperBound);
	}
}
