package keegan.labstuff.common.capabilities;

import java.util.ArrayList;

import keegan.labstuff.common.EnumGravity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

public abstract class LSPlayerStatsClient
{
    public static LSPlayerStatsClient get(Entity entity)
    {
        return entity.getCapability(Capabilities.LS_STATS_CLIENT_CAPABILITY, null);
    }

    public abstract void setGravity(EnumGravity newGravity);

    public abstract boolean isUsingParachute();

    public abstract void setUsingParachute(boolean usingParachute);

    public abstract boolean isLastUsingParachute();

    public abstract void setLastUsingParachute(boolean lastUsingParachute);

    public abstract boolean isUsingAdvancedGoggles();

    public abstract void setUsingAdvancedGoggles(boolean usingAdvancedGoggles);

    public abstract int getThermalLevel();

    public abstract void setThermalLevel(int thermalLevel);

    public abstract boolean isThermalLevelNormalising();

    public abstract void setThermalLevelNormalising(boolean thermalLevelNormalising);

    public abstract int getThirdPersonView();

    public abstract void setThirdPersonView(int thirdPersonView);

    public abstract long getTick();

    public abstract void setTick(long tick);

    public abstract boolean isOxygenSetupValid();

    public abstract void setOxygenSetupValid(boolean oxygenSetupValid);

    public abstract AxisAlignedBB getBoundingBoxBefore();

    public abstract void setBoundingBoxBefore(AxisAlignedBB boundingBoxBefore);

    public abstract boolean isLastOnGround();

    public abstract void setLastOnGround(boolean lastOnGround);

    public abstract double getDistanceSinceLastStep();

    public abstract void setDistanceSinceLastStep(double distanceSinceLastStep);

    public abstract int getLastStep();

    public abstract void setLastStep(int lastStep);

    public abstract boolean isInFreefall();

    public abstract void setInFreefall(boolean inFreefall);

    public abstract boolean isInFreefallLast();

    public abstract void setInFreefallLast(boolean inFreefallLast);

    public abstract boolean isInFreefallFirstCheck();

    public abstract void setInFreefallFirstCheck(boolean inFreefallFirstCheck);

    public abstract double getDownMotionLast();

    public abstract void setDownMotionLast(double downMotionLast);

    public abstract boolean isLastRidingCameraZoomEntity();

    public abstract void setLastRidingCameraZoomEntity(boolean lastRidingCameraZoomEntity);

    public abstract int getLandingTicks();

    public abstract void setLandingTicks(int landingTicks);

    public abstract EnumGravity getGdir();

    public abstract void setGdir(EnumGravity gdir);

    public abstract float getGravityTurnRate();

    public abstract void setGravityTurnRate(float gravityTurnRate);

    public abstract float getGravityTurnRatePrev();

    public abstract void setGravityTurnRatePrev(float gravityTurnRatePrev);

    public abstract float getGravityTurnVecX();

    public abstract void setGravityTurnVecX(float gravityTurnVecX);

    public abstract float getGravityTurnVecY();

    public abstract void setGravityTurnVecY(float gravityTurnVecY);

    public abstract float getGravityTurnVecZ();

    public abstract void setGravityTurnVecZ(float gravityTurnVecZ);

    public abstract float getGravityTurnYaw();

    public abstract void setGravityTurnYaw(float gravityTurnYaw);

    public abstract int getSpaceRaceInviteTeamID();

    public abstract void setSpaceRaceInviteTeamID(int spaceRaceInviteTeamID);

    public abstract boolean isLastZoomed();

    public abstract void setLastZoomed(boolean lastZoomed);

    public abstract int getBuildFlags();

    public abstract void setBuildFlags(int buildFlags);

    public abstract boolean isSsOnGroundLast();

    public abstract void setSsOnGroundLast(boolean ssOnGroundLast);

    public abstract FreefallHandler getFreefallHandler();

    public abstract void setFreefallHandler(FreefallHandler freefallHandler);

    public abstract int getMaxLandingticks();

    public abstract float[] getLandingYOffset();

    public abstract void setLandingYOffset(float[] landingYOffset);
}