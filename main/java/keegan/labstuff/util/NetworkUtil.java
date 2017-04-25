package keegan.labstuff.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import com.google.common.math.DoubleMath;

import io.netty.buffer.*;
import io.netty.handler.codec.EncoderException;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class NetworkUtil
{
	public static void encodeData(ByteBuf buffer, Collection<Object> sendData) throws IOException
    {
        for (Object dataValue : sendData)
        {
            if (dataValue instanceof Integer)
            {
                buffer.writeInt((Integer) dataValue);
            }
            else if (dataValue instanceof Float)
            {
                buffer.writeFloat((Float) dataValue);
            }
            else if (dataValue instanceof Double)
            {
                buffer.writeDouble((Double) dataValue);
            }
            else if (dataValue instanceof Byte)
            {
                buffer.writeByte((Byte) dataValue);
            }
            else if (dataValue instanceof Boolean)
            {
                buffer.writeBoolean((Boolean) dataValue);
            }
            else if (dataValue instanceof String)
            {
                ByteBufUtils.writeUTF8String(buffer, (String) dataValue);
            }
            else if (dataValue instanceof Short)
            {
                buffer.writeShort((Short) dataValue);
            }
            else if (dataValue instanceof Long)
            {
                buffer.writeLong((Long) dataValue);
            }
            else if (dataValue instanceof NBTTagCompound)
            {
                NetworkUtil.writeNBTTagCompound((NBTTagCompound) dataValue, buffer);
            }
            else if (dataValue instanceof FluidTank)
            {
                NetworkUtil.writeFluidTank((FluidTank) dataValue, buffer);
            }
            else if (dataValue instanceof Entity)
            {
                buffer.writeInt(((Entity) dataValue).getEntityId());
            }
            else if (dataValue instanceof Vector3)
            {
                buffer.writeDouble(((Vector3) dataValue).x);
                buffer.writeDouble(((Vector3) dataValue).y);
                buffer.writeDouble(((Vector3) dataValue).z);
            }
            else if (dataValue instanceof BlockVec3)
            {
                buffer.writeInt(((BlockVec3) dataValue).x);
                buffer.writeInt(((BlockVec3) dataValue).y);
                buffer.writeInt(((BlockVec3) dataValue).z);
            }
            else if (dataValue instanceof byte[])
            {
                buffer.writeInt(((byte[]) dataValue).length);
                for (int i = 0; i < ((byte[]) dataValue).length; i++)
                {
                    buffer.writeByte(((byte[]) dataValue)[i]);
                }
            }
            else if (dataValue instanceof UUID)
            {
                buffer.writeLong(((UUID) dataValue).getLeastSignificantBits());
                buffer.writeLong(((UUID) dataValue).getMostSignificantBits());
            }
            else if (dataValue instanceof Collection)
            {
                NetworkUtil.encodeData(buffer, (Collection<Object>) dataValue);
            }
            else if (dataValue instanceof Integer[])
            {
                Integer[] array = (Integer[]) dataValue;
                buffer.writeInt(array.length);

                for (int i = 0; i < array.length; i++)
                {
                    buffer.writeInt(array[i]);
                }
            }
            else if (dataValue instanceof String[])
            {
                String[] array = (String[]) dataValue;
                buffer.writeInt(array.length);

                for (int i = 0; i < array.length; i++)
                {
                    ByteBufUtils.writeUTF8String(buffer, array[i]);
                }
            }
            else if (dataValue instanceof EnumFacing)
            {
                buffer.writeInt(((EnumFacing) dataValue).getIndex());
            }
            else if (dataValue instanceof BlockPos)
            {
                BlockPos pos = (BlockPos) dataValue;
                buffer.writeInt(pos.getX());
                buffer.writeInt(pos.getY());
                buffer.writeInt(pos.getZ());
            }
            else if (dataValue instanceof EnumDyeColor)
            {
                buffer.writeInt(((EnumDyeColor) dataValue).getDyeDamage());
            }
        }
    }

    public static ArrayList<Object> decodeData(Class<?>[] types, ByteBuf buffer)
    {
        ArrayList<Object> objList = new ArrayList<Object>();

        for (Class clazz : types)
        {
            if (clazz.equals(Integer.class))
            {
                objList.add(buffer.readInt());
            }
            else if (clazz.equals(Float.class))
            {
                objList.add(buffer.readFloat());
            }
            else if (clazz.equals(Double.class))
            {
                objList.add(buffer.readDouble());
            }
            else if (clazz.equals(Byte.class))
            {
                objList.add(buffer.readByte());
            }
            else if (clazz.equals(Boolean.class))
            {
                objList.add(buffer.readBoolean());
            }
            else if (clazz.equals(String.class))
            {
                objList.add(ByteBufUtils.readUTF8String(buffer));
            }
            else if (clazz.equals(Short.class))
            {
                objList.add(buffer.readShort());
            }
            else if (clazz.equals(Long.class))
            {
                objList.add(buffer.readLong());
            }
            else if (clazz.equals(byte[].class))
            {
                byte[] bytes = new byte[buffer.readInt()];
                for (int i = 0; i < bytes.length; i++)
                {
                    bytes[i] = buffer.readByte();
                }
                objList.add(bytes);
            }
            else if (clazz.equals(NBTTagCompound.class))
            {
                try
                {
                    objList.add(NetworkUtil.readNBTTagCompound(buffer));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else if (clazz.equals(BlockVec3.class))
            {
                objList.add(new BlockVec3(buffer.readInt(), buffer.readInt(), buffer.readInt()));
            }
            else if (clazz.equals(UUID.class))
            {
                objList.add(new UUID(buffer.readLong(), buffer.readLong()));
            }
            else if (clazz.equals(Vector3.class))
            {
                objList.add(new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));
            }
            else if (clazz.equals(Integer[].class))
            {
                int size = buffer.readInt();

                for (int i = 0; i < size; i++)
                {
                    objList.add(buffer.readInt());
                }
            }
            else if (clazz.equals(String[].class))
            {
                int size = buffer.readInt();

                for (int i = 0; i < size; i++)
                {
                    objList.add(ByteBufUtils.readUTF8String(buffer));
                }
            }
            else if (clazz.equals(EnumFacing.class))
            {
                objList.add(EnumFacing.getFront(buffer.readInt()));
            }
            else if (clazz.equals(BlockPos.class))
            {
                objList.add(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
            }
            else if (clazz.equals(EnumDyeColor.class))
            {
                objList.add(EnumDyeColor.byDyeDamage(buffer.readInt()));
            }
        }

        return objList;
    }

    public static Object getFieldValueFromStream(Field field, ByteBuf buffer, World world) throws IOException
    {
        Class<?> dataValue = field.getType();

        if (dataValue.equals(int.class))
        {
            return buffer.readInt();
        }
        else if (dataValue.equals(float.class))
        {
            return buffer.readFloat();
        }
        else if (dataValue.equals(double.class))
        {
            return buffer.readDouble();
        }
        else if (dataValue.equals(byte.class))
        {
            return buffer.readByte();
        }
        else if (dataValue.equals(boolean.class))
        {
            return buffer.readBoolean();
        }
        else if (dataValue.equals(String.class))
        {
            return ByteBufUtils.readUTF8String(buffer);
        }
        else if (dataValue.equals(short.class))
        {
            return buffer.readShort();
        }
        else if (dataValue.equals(Long.class))
        {
            return buffer.readLong();
        }
        else if (dataValue.equals(NBTTagCompound.class))
        {
            return NetworkUtil.readNBTTagCompound(buffer);
        }
        else if (dataValue.equals(FluidTank.class))
        {
            return NetworkUtil.readFluidTank(buffer);
        }
        else if (dataValue.equals(Vector3.class))
        {
            return new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }
        else if (dataValue.equals(BlockVec3.class))
        {
            return new BlockVec3(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
        else if (dataValue.equals(UUID.class))
        {
            return new UUID(buffer.readLong(), buffer.readLong());
        }
        else if (dataValue.equals(byte[].class))
        {
            byte[] bytes = new byte[buffer.readInt()];
            for (int i = 0; i < bytes.length; i++)
            {
                bytes[i] = buffer.readByte();
            }
            return bytes;
        }
        else if (dataValue.equals(EnumFacing.class))
        {
            return EnumFacing.getFront(buffer.readInt());
        }
        else if (dataValue.equals(BlockPos.class))
        {
            return new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
        else if (dataValue.equals(EnumDyeColor.class))
        {
            return EnumDyeColor.byDyeDamage(buffer.readInt());
        }
        else
        {
            Class<?> c = dataValue;

            while (c != null)
            {
                if (c.equals(Entity.class))
                {
                    return world.getEntityByID(buffer.readInt());
                }

                c = c.getSuperclass();
            }
        }

        throw new NullPointerException("Field type not found: " + field.getType().getSimpleName());
    }

    public static ItemStack readItemStack(ByteBuf buffer) throws IOException
    {
        ItemStack itemstack = null;
        short itemID = buffer.readShort();

        if (itemID >= 0)
        {
            byte stackSize = buffer.readByte();
            short meta = buffer.readShort();
            itemstack = new ItemStack(Item.getItemById(itemID), stackSize, meta);
            itemstack.setTagCompound(readNBTTagCompound(buffer));
        }

        return itemstack;
    }

    public static void writeItemStack(ItemStack itemStack, ByteBuf buffer) throws IOException
    {
        if (itemStack == null)
        {
            buffer.writeShort(-1);
        }
        else
        {
            buffer.writeShort(Item.getIdFromItem(itemStack.getItem()));
            buffer.writeByte(itemStack.stackSize);
            buffer.writeShort(itemStack.getItemDamage());
            NBTTagCompound nbttagcompound = null;

            if (itemStack.getItem().isDamageable() || itemStack.getItem().getShareTag())
            {
                nbttagcompound = itemStack.getTagCompound();
            }

            NetworkUtil.writeNBTTagCompound(nbttagcompound, buffer);
        }
    }

    public static NBTTagCompound readNBTTagCompound(ByteBuf buffer) throws IOException
    {
        short dataLength = buffer.readShort();

        if (dataLength < 0)
        {
            return null;
        }
        else
        {
            return CompressedStreamTools.read(new ByteBufInputStream(buffer), new NBTSizeTracker(2097152L));
        }
    }

    public static void writeNBTTagCompound(NBTTagCompound nbt, ByteBuf buffer) throws IOException
    {
        if (nbt == null)
        {
            buffer.writeShort(-1);
        }
        else
        {
            try
            {
                CompressedStreamTools.write(nbt, new ByteBufOutputStream(buffer));
            }
            catch (IOException ioexception)
            {
                throw new EncoderException(ioexception);
            }
        }
    }

    public static void writeFluidTank(FluidTank fluidTank, ByteBuf buffer) throws IOException
    {
        if (fluidTank == null)
        {
            buffer.writeInt(0);
            ByteBufUtils.writeUTF8String(buffer, "");
            buffer.writeInt(0);
        }
        else
        {
            buffer.writeInt(fluidTank.getCapacity());
            ByteBufUtils.writeUTF8String(buffer, fluidTank.getFluid() == null ? "" : fluidTank.getFluid().getFluid().getName());
            buffer.writeInt(fluidTank.getFluidAmount());
        }
    }

    public static FluidTank readFluidTank(ByteBuf buffer) throws IOException
    {
        int capacity = buffer.readInt();
        String fluidName = ByteBufUtils.readUTF8String(buffer);
        FluidTank fluidTank = new FluidTank(capacity);
        int amount = buffer.readInt();

        if (fluidName.equals(""))
        {
            fluidTank.setFluid(null);
        }
        else
        {
            Fluid fluid = FluidRegistry.getFluid(fluidName);
            fluidTank.setFluid(new FluidStack(fluid, amount));
        }

        return fluidTank;
    }

    public static boolean fuzzyEquals(Object a, Object b)
    {
        if ((a == null) != (b == null))
        {
            return false;
        }
        else if (a == null)
        {
            return true;
        }
        else if (a instanceof Float && b instanceof Float)
        {
            return DoubleMath.fuzzyEquals((Float) a, (Float) b, 0.01);
        }
        else if (a instanceof Double && b instanceof Double)
        {
            return DoubleMath.fuzzyEquals((Double) a, (Double) b, 0.01);
        }
        else if (a instanceof Entity && b instanceof Entity)
        {
            Entity a2 = (Entity) a;
            Entity b2 = (Entity) b;
            return fuzzyEquals(a2.getEntityId(), b2.getEntityId());
        }
        else if (a instanceof Vector3 && b instanceof Vector3)
        {
            Vector3 a2 = (Vector3) a;
            Vector3 b2 = (Vector3) b;
            return fuzzyEquals(a2.x, b2.x) &&
                    fuzzyEquals(a2.y, b2.y) &&
                    fuzzyEquals(a2.z, b2.z);
        }
        else if (a instanceof FluidTank && b instanceof FluidTank)
        {
            FluidTank a2 = (FluidTank) a;
            FluidTank b2 = (FluidTank) b;
            FluidStack fluidA = a2.getFluid();
            FluidStack fluidB = b2.getFluid();
            return fuzzyEquals(a2.getCapacity(), b2.getCapacity()) &&
                    fuzzyEquals(fluidA != null ? fluidA.getFluid().getName() : "", fluidB != null ? fluidB.getFluid().getName() : "") &&
                    fuzzyEquals(a2.getFluidAmount(), b2.getFluidAmount());
        }
        else
        {
            return a.equals(b);
        }
    }

    public static Object cloneNetworkedObject(Object a)
    {
        // We only need to clone mutable objects
        if (a instanceof FluidTank)
        {
            FluidTank prevTank = (FluidTank)a;
            FluidTank tank = new FluidTank(prevTank.getFluid(), prevTank.getCapacity());
            return tank;
        }
        else
        {
            return a;
        }
    }
}