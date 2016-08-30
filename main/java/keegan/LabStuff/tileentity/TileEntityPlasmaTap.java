package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityPlasma;

public class TileEntityPlasmaTap extends TileEntityPlasma 
{
	private boolean running;

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void burnPlasma() 
	{
		if(running)
			subtractPlasma(250, this);
	}
}
