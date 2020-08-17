package flyingperson.BetterPipes.compat.gtce;

import flyingperson.BetterPipes.Utils;
import flyingperson.BetterPipes.compat.CompatBase;
import gregtech.api.pipenet.tile.AttachmentType;
import gregtech.common.pipelike.fluidpipe.tile.TileEntityFluidPipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;

public class CompatGTCEFluid extends CompatBase {
    @Override
    public boolean isModLoaded() {
        return Loader.isModLoaded("gregtech");
    }

    @Override
    public boolean canConnect(TileEntity te, EnumFacing direction) {
        TileEntity te2 = te.getWorld().getTileEntity(te.getPos().offset(direction, 1));
        if (te2 != null) {
            return te2.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()) | te2 instanceof TileEntityFluidPipe;
        }
        return false;
    }

    @Override
    public ArrayList<EnumFacing> getConnections(TileEntity te) {
        ArrayList<EnumFacing> connections = new ArrayList<>();
        if (isAcceptable(te)) {
            TileEntityFluidPipe cable = (TileEntityFluidPipe) te;
            int mask = cable.getPipeBlock().getActualConnections(cable.getPipeBlock().getPipeTileEntity(te), te.getWorld());
            connections = Utils.fromGTCEBitmask(mask);
        }
        return connections;
    }

    @Override
    public boolean isAcceptable(TileEntity te) {
        return te instanceof TileEntityFluidPipe;
    }

    @Override
    public void connect(TileEntity te, EnumFacing direction, EntityPlayer player, float hitX, float hitY, float hitZ) {
        if (isAcceptable(te)) {
            TileEntityFluidPipe cable = (TileEntityFluidPipe) te;
            cable.setConnectionBlocked(AttachmentType.PIPE, direction, false);
        }
    }

    @Override
    public void disconnect(TileEntity te, EnumFacing direction, EntityPlayer player, float hitX, float hitY, float hitZ) {
        if (isAcceptable(te)) {
            TileEntityFluidPipe cable = (TileEntityFluidPipe) te;
            cable.setConnectionBlocked(AttachmentType.PIPE, direction, true);
        }
    }

}