package resonant.lib.prefab.fluid

import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._
import resonant.api.ISave
import resonant.api.tile.INodeProvider
import resonant.api.tile.node.INode
import resonant.lib.grid.node.{NodeConnector, TTileConnector}

/**
 * A node that handles fluid interactions
 *
 * @param parent Parent(TileEntity or Multipart) that contains this node
 * @param volume Amount of fluid in liters * @author Calclavia
 */
class NodeFluid(parent: INodeProvider, volume: Int = FluidContainerRegistry.BUCKET_VOLUME) extends NodeConnector[IFluidHandler](parent) with ISave with TFluidHandler with TFluidTank with TTileConnector[IFluidHandler]
{
  /** Internal tank */
  private var tank = new FluidTank(volume)

  var onFluidChanged: () => Unit = () => ()

  override def getPrimaryTank: FluidTank = tank

  /**
   * Sets the primary tank (not checked)
   * @param t - The new tank
   */
  def setPrimaryTank(t: FluidTank) = tank = t

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int =
  {
    tank synchronized
    {
      if (canConnect(from))
      {
        val ret = super.fill(from, resource, doFill)
        onFluidChanged()
        return ret
      }
      return 0
    }
  }

  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack =
  {
    tank synchronized
    {
      if (canConnect(from))
      {
        val ret = super.drain(from, resource, doDrain)
        onFluidChanged()
        return ret
      }
      return null
    }
  }

  override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack =
  {
    tank synchronized
    {
      if (canConnect(from))
      {
        val ret = super.drain(from, maxDrain, doDrain)
        onFluidChanged()
        return ret
      }

      return null
    }
  }

  override def canFill(from: ForgeDirection, fluid: Fluid): Boolean = canConnect(from)

  override def canDrain(from: ForgeDirection, fluid: Fluid): Boolean = canConnect(from)

  /**
   * The class used to compare when making connections
   */
  override protected def getCompareClass: Class[_ <: NodeFluid with INode] = classOf[NodeFluid]

  override def load(nbt: NBTTagCompound)
  {
    getPrimaryTank.readFromNBT(nbt.getCompoundTag("tank"))
  }

  override def save(nbt: NBTTagCompound)
  {
    nbt.setTag("tank", getPrimaryTank.writeToNBT(new NBTTagCompound))
  }

  protected def showConnectionsFor(obj: AnyRef, dir: ForgeDirection): Boolean =
  {
    if (obj != null)
    {
      if (obj.getClass.isAssignableFrom(getParent.getClass))
      {
        return true
      }
    }
    return false
  }
}
