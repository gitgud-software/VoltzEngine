package universalelectricity.api.grid.electric;

import universalelectricity.api.grid.INode;

/**
 * @author Calclavia
 */
public interface IElectricNode extends INode
{
	public void applyPower(double wattage);

	public void drawPower(double wattage);

	public double getEnergy(double voltageThreshold);

	public double getVoltage();

	public double getCapacity();

	public void setCapacity(double value);

	/**
	 * Resistance in Ohms
	 */
	public double getResistance();

	public void setResistance(double value);

}
