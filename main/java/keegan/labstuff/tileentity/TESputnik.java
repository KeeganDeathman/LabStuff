package keegan.labstuff.tileentity;

import static keegan.labstuff.LabStuffMain.*;

import java.util.List;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.capabilities.ResearchUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;

public class TESputnik extends TileEntityRocket {

	public TESputnik() {
		super(new Block[][][]
				{{{Blocks.AIR, Blocks.AIR, Blocks.AIR}, {Blocks.AIR, sputnik, Blocks.AIR}, {Blocks.AIR, Blocks.AIR, Blocks.AIR}},
				{{Blocks.AIR, Blocks.AIR, Blocks.AIR}, {Blocks.AIR, r7, Blocks.AIR}, {Blocks.AIR, Blocks.AIR, Blocks.AIR}},
				{{Blocks.AIR, Blocks.AIR, Blocks.AIR},{Blocks.AIR, r7, Blocks.AIR},{Blocks.AIR, Blocks.AIR, Blocks.AIR}},
				{{Blocks.AIR, Blocks.AIR, Blocks.AIR},{Blocks.AIR, r7, Blocks.AIR},{Blocks.AIR, Blocks.AIR, Blocks.AIR}},
				{{Blocks.AIR, Blocks.AIR, Blocks.AIR},{Blocks.AIR, r7, Blocks.AIR},{Blocks.AIR, Blocks.AIR, Blocks.AIR}},
				{{Blocks.AIR, Blocks.AIR, Blocks.AIR},{Blocks.AIR, r7, Blocks.AIR},{Blocks.AIR, Blocks.AIR, Blocks.AIR}},
				{{Blocks.AIR, Blocks.AIR, Blocks.AIR},{Blocks.AIR, r7, Blocks.AIR},{Blocks.AIR, Blocks.AIR, Blocks.AIR}},
				{{Blocks.AIR, Blocks.AIR, Blocks.AIR},{Blocks.AIR, radioKit, Blocks.AIR},{Blocks.AIR, Blocks.AIR, Blocks.AIR}},
				{{Blocks.AIR, Blocks.AIR, Blocks.AIR},{Blocks.AIR, r7, Blocks.AIR},{Blocks.AIR, Blocks.AIR, Blocks.AIR}},
				{{Blocks.AIR, Blocks.AIR, Blocks.AIR},{Blocks.AIR, r7, Blocks.AIR},{Blocks.AIR, Blocks.AIR, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, r7, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, r7, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, r7, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, r7, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, r7, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, gimbalIGS, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, rocketStrap, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, r7, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, r7, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, r7, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, r7, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, fuelTank, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, fuelTank, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, fuelTank, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, fuelTank, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, fuelTank, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, fuelTank, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, fuelTank, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, block8k71p5O, Blocks.AIR},{block8k71p5O, fuelTank, block8k71p5O},{Blocks.AIR, block8k71p5O, Blocks.AIR}},
				{{Blocks.AIR, rd107, Blocks.AIR},{rd107, rd108, rd107},{Blocks.AIR, rd107, Blocks.AIR}}}, 
				5, 50000, "sputnik");
		// TODO Auto-generated constructor stub
	}
	
	public void epoch()
	{
		super.epoch();
		List<EntityPlayer> playerList = worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.pos.getX()-100,this.pos.getY()-100,this.pos.getZ()-100,this.pos.getX()+100,this.pos.getY()+100,this.pos.getZ()+100));
		for(EntityPlayer player : playerList)
			ResearchUtils.research(player,LabStuffMain.ORBIT);
	}

}
