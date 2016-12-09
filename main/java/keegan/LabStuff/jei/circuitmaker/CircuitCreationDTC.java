package keegan.labstuff.jei.circuitmaker;

import net.minecraft.item.Item;

public class CircuitCreationDTC 
{
	private Item drilled;
	private Item circuit;
	private String designName;

	public CircuitCreationDTC(String design, Item drilled, Item circuit)
	{
		this.drilled = drilled;
		this.circuit = circuit;
		this.designName = design;
	}

	public Item getDrilled() {
		return drilled;
	}

	public void setDrilled(Item drilled) {
		this.drilled = drilled;
	}

	public Item getCircuit() {
		return circuit;
	}

	public void setCircuit(Item circuit) {
		this.circuit = circuit;
	}

	public String getDesignName() {
		return designName;
	}

	public void setDesignName(String designName) {
		this.designName = designName;
	}
	
}
