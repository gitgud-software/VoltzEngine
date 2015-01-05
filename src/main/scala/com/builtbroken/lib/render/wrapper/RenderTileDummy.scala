package com.builtbroken.lib.render.wrapper

import com.builtbroken.lib.prefab.tile.spatial.Tile
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import com.builtbroken.lib.transform.vector.Vector3

/**
 * @author Calclavia
 */
@SideOnly(Side.CLIENT)
class RenderTileDummy extends TileEntitySpecialRenderer
{
  def renderTileEntityAt(tile: TileEntity, x: Double, y: Double, z: Double, f: Float)
  {
    if (tile.isInstanceOf[Tile])
    {
      val spatial = tile.asInstanceOf[Tile]
      spatial.renderDynamic(new Vector3(x, y, z), f, 0)
    }
  }
}