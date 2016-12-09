package keegan.labstuff.recipes;

import net.minecraft.item.Item;

public class Enrichment 
{
	private Item input;
	private Item output;
	
	public Enrichment(Item input, Item output) {
		this.input = input;
		this.output = output;
	}

	public Item getInput() {
		return input;
	}

	public void setInput(Item input) {
		this.input = input;
	}

	public Item getOutput() {
		return output;
	}

	public void setOutput(Item output) {
		this.output = output;
	}
	
	
	
}
