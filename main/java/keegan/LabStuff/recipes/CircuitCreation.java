package keegan.labstuff.recipes;

import net.minecraft.item.Item;

public class CircuitCreation 
{
	private Item drilled;
	private Item etched;
	private Item circuit;
	private String designName;

	public CircuitCreation(String design, Item drilled, Item etched, Item circuit)
	{
		this.drilled = drilled;
		this.etched = etched;
		this.circuit = circuit;
		this.designName = design;
	}

	public Item getDrilled() {
		return drilled;
	}

	public void setDrilled(Item drilled) {
		this.drilled = drilled;
	}

	public Item getEtched() {
		return etched;
	}

	public void setEtched(Item etched) {
		this.etched = etched;
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
