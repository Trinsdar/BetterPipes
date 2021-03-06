package flyingperson.BetterPipes.util;

import flyingperson.BetterPipes.BetterPipes;
import flyingperson.BetterPipes.IBetterPipesWrench;
import flyingperson.BetterPipes.ModSounds;
import flyingperson.BetterPipes.compat.CompatBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

public class Utils {

    public static boolean arePosEqual(BlockPos pos1, BlockPos pos2) {
        return pos1.getX() == pos2.getX() & pos1.getY() == pos2.getY() & pos1.getZ() == pos2.getZ();
    }

    @Nullable
    public static RayTraceResult rayTraceBlocks(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock, World world, BlockPos ignore)
    {
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z))
        {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z))
            {
                int i = MathHelper.floor(vec32.x);
                int j = MathHelper.floor(vec32.y);
                int k = MathHelper.floor(vec32.z);
                int l = MathHelper.floor(vec31.x);
                int i1 = MathHelper.floor(vec31.y);
                int j1 = MathHelper.floor(vec31.z);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid) && !arePosEqual(ignore, blockpos))
                {
                    return iblockstate.collisionRayTrace(world, blockpos, vec31, vec32);
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z))
                    {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k)
                    {
                        return returnLastUncollidableBlock ? raytraceresult2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l)
                    {
                        d0 = (double)l + 1.0D;
                    }
                    else if (i < l)
                    {
                        d0 = (double)l + 0.0D;
                    }
                    else
                    {
                        flag2 = false;
                    }

                    if (j > i1)
                    {
                        d1 = (double)i1 + 1.0D;
                    }
                    else if (j < i1)
                    {
                        d1 = (double)i1 + 0.0D;
                    }
                    else
                    {
                        flag = false;
                    }

                    if (k > j1)
                    {
                        d2 = (double)j1 + 1.0D;
                    }
                    else if (k < j1)
                    {
                        d2 = (double)j1 + 0.0D;
                    }
                    else
                    {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;

                    if (flag2)
                    {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag)
                    {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1)
                    {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    }
                    else
                    {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = world.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB && !arePosEqual(blockpos, ignore))
                    {
                        if (block1.canCollideCheck(iblockstate1, stopOnLiquid))
                        {

                            return iblockstate1.collisionRayTrace(world, blockpos, vec31, vec32);
                        }
                        else
                        {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? raytraceresult2 : null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    @Nullable
    public static RayTraceResult rayTraceIgnoreBB(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock, World world, BlockPos ignore)
    {
        AxisAlignedBB bb = Block.FULL_BLOCK_AABB;
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z))
        {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z))
            {
                int i = MathHelper.floor(vec32.x);
                int j = MathHelper.floor(vec32.y);
                int k = MathHelper.floor(vec32.z);
                int l = MathHelper.floor(vec31.x);
                int i1 = MathHelper.floor(vec31.y);
                int j1 = MathHelper.floor(vec31.z);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid) && !arePosEqual(ignore, blockpos))
                {
                    return collisionRayTrace(blockpos, vec31, vec32, bb);
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z))
                    {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k)
                    {
                        return returnLastUncollidableBlock ? raytraceresult2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l)
                    {
                        d0 = (double)l + 1.0D;
                    }
                    else if (i < l)
                    {
                        d0 = (double)l + 0.0D;
                    }
                    else
                    {
                        flag2 = false;
                    }

                    if (j > i1)
                    {
                        d1 = (double)i1 + 1.0D;
                    }
                    else if (j < i1)
                    {
                        d1 = (double)i1 + 0.0D;
                    }
                    else
                    {
                        flag = false;
                    }

                    if (k > j1)
                    {
                        d2 = (double)j1 + 1.0D;
                    }
                    else if (k < j1)
                    {
                        d2 = (double)j1 + 0.0D;
                    }
                    else
                    {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;

                    if (flag2)
                    {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag)
                    {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1)
                    {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    }
                    else
                    {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = world.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB && !arePosEqual(blockpos, ignore))
                    {
                        if (block1.canCollideCheck(iblockstate1, stopOnLiquid))
                        {
                            return collisionRayTrace(blockpos, vec31, vec32, bb);
                        }
                        else
                        {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? raytraceresult2 : null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    public static RayTraceResult getBlockLookingAtIgnoreBB(EntityPlayer liv) {
        Vec3d pos2 = liv.getPositionVector().addVector(0, liv.getEyeHeight(), 0);
        RayTraceResult rayTraceResult = Utils.rayTraceIgnoreBB(pos2, pos2.add(liv.getLookVec().scale(12)), false, true, true, liv.world, new BlockPos(0, -1, 0));
        if (rayTraceResult != null) {
            if (rayTraceResult.typeOfHit != null) {
                if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                    return rayTraceResult;
                }
            }
        }
        return null;
    }

    public static RayTraceResult getBlockLookingat1(EntityPlayer liv) {
        Vec3d pos2 = liv.getPositionVector().addVector(0, liv.getEyeHeight(), 0);
        RayTraceResult rayTraceResult = liv.world.rayTraceBlocks(pos2, pos2.add(liv.getLookVec().scale(12)), false, true, true);
        if (rayTraceResult != null) {
            if (rayTraceResult.typeOfHit != null) {
                if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                    return rayTraceResult;
                }
            }
        }
        return null;
    }

    public static RayTraceResult getBlockLookingat2(EntityPlayer liv, BlockPos exclude) {
        Vec3d pos2 = liv.getPositionVector().addVector(0, liv.getEyeHeight(), 0);
        RayTraceResult rayTraceResult = Utils.rayTraceBlocks(pos2, pos2.add(liv.getLookVec().scale(12)), false, true, true, liv.world, exclude);
        if (rayTraceResult != null) {
            if (rayTraceResult.typeOfHit != null) {
                if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                    return rayTraceResult;
                }
            }
        }
        return null;
    }

    @Nullable
    protected static RayTraceResult collisionRayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox)
    {
        Vec3d vec3d = start.subtract(pos.getX(), pos.getY(), pos.getZ());
        Vec3d vec3d1 = end.subtract(pos.getX(), pos.getY(), pos.getZ());
        RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d1);
        return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector(pos.getX(), pos.getY(), pos.getZ()), raytraceresult.sideHit, pos);
    }

    public static EnumFacing getDirection(EnumFacing overlaySide, Vec3d vec) {
        double x = vec.x - Math.floor(vec.x);
        double y = vec.y - Math.floor(vec.y);
        double z = vec.z - Math.floor(vec.z);

        switch(overlaySide) {
            case DOWN:
                if (x >= 0.75 && 0.25 <= z && z <= 0.75) return EnumFacing.EAST;
                if (x <= 0.25 && 0.25 <= z && z <= 0.75) return EnumFacing.WEST;
                if (0.25 <= x && x <= 0.75 && z >= 0.75) return EnumFacing.SOUTH;
                if (0.25 <= x && x <= 0.75 && z <= 0.25) return EnumFacing.NORTH;
                if (0.25 < x && x < 0.75 && 0.25 < z && z < 0.75) return EnumFacing.DOWN;
                return EnumFacing.UP;
            case UP:
                if (x >= 0.75 && 0.25 <= z && z <= 0.75) return EnumFacing.EAST;
                if (x <= 0.25 && 0.25 <= z && z <= 0.75) return EnumFacing.WEST;
                if (0.25 <= x && x <= 0.75 && z >= 0.75) return EnumFacing.SOUTH;
                if (0.25 <= x && x <= 0.75 && z <= 0.25) return EnumFacing.NORTH;
                if (0.25 < x && x < 0.75 && 0.25 < z && z < 0.75) return EnumFacing.UP;
                return EnumFacing.DOWN;
            case NORTH:
                if (x >= 0.75 && 0.25 <= y && y <= 0.75) return EnumFacing.EAST;
                if (x <= 0.25 && 0.25 <= y && y <= 0.75) return EnumFacing.WEST;
                if (0.25 <= x && x <= 0.75 && y >= 0.75) return EnumFacing.UP;
                if (0.25 <= x && x <= 0.75 && y <= 0.25) return EnumFacing.DOWN;
                if (0.25 < x && x < 0.75 && 0.25 < y && z < 0.75) return EnumFacing.NORTH;
                return EnumFacing.SOUTH;
            case SOUTH:
                if (x >= 0.75 && 0.25 <= y && y <= 0.75) return EnumFacing.EAST;
                if (x <= 0.25 && 0.25 <= y && y <= 0.75) return EnumFacing.WEST;
                if (0.25 <= x && x <= 0.75 && y >= 0.75) return EnumFacing.UP;
                if (0.25 <= x && x <= 0.75 && y <= 0.25) return EnumFacing.DOWN;
                if (0.25 < x && x < 0.75 && 0.25 < y && z < 0.75) return EnumFacing.SOUTH;
                return EnumFacing.NORTH;
            case WEST:
                if (z <= 0.25 && 0.25 <= y && y <= 0.75) return EnumFacing.NORTH;
                if (z >= 0.75 && 0.25 <= y && y <= 0.75) return EnumFacing.SOUTH;
                if (y >= 0.75 && 0.25 <= z && z <= 0.75) return EnumFacing.UP;
                if (y <= 0.25 && 0.25 <= z && z <= 0.75) return EnumFacing.DOWN;
                if (0.25 < y && y < 0.75 && 0.25 < z && z < 0.75) return EnumFacing.WEST;
                return EnumFacing.EAST;
            case EAST:
                if (z <= 0.25 && 0.25 <= y && y <= 0.75) return EnumFacing.NORTH;
                if (z >= 0.75 && 0.25 <= y && y <= 0.75) return EnumFacing.SOUTH;
                if (y >= 0.75 && 0.25 <= z && z <= 0.75) return EnumFacing.UP;
                if (y <= 0.25 && 0.25 <= z && z <= 0.75) return EnumFacing.DOWN;
                if (0.25 < y && y < 0.75 && 0.25 < z && z < 0.75) return EnumFacing.EAST;
                return EnumFacing.WEST;
        }
        return null;
    }

    public static int[] toIntArr(ArrayList<EnumFacing> connections) {
        int[] arr = new int[6];
        for (EnumFacing e : EnumFacing.VALUES) {
            if (connections.contains(e)) arr[e.getIndex()] = 1;
            else arr[e.getIndex()] = 0;
        }
        return arr;
    }

    public static ArrayList<EnumFacing> toArrayList(int[] arr) {
        ArrayList<EnumFacing> arrayList = new ArrayList<>();
        for (EnumFacing e : EnumFacing.VALUES) {
            if (arr[e.getIndex()] == 1) arrayList.add(e);
        }
        return arrayList;
    }

    public static ArrayList<EnumFacing> fromGTCEBitmask(int mask) {
        ArrayList<EnumFacing> list = new ArrayList<>();
        for (EnumFacing facing : EnumFacing.VALUES) {
            int current = (int) Math.pow(2, facing.getIndex());
            if ((mask & current) == current) list.add(facing);
        }
        return list;
    }

    public static EnumFacing fromIndex(int index) {
        switch(index) {
            case 0:
                return EnumFacing.DOWN;
            case 1:
                return EnumFacing.UP;
            case 2:
                return EnumFacing.NORTH;
            case 3:
                return EnumFacing.SOUTH;
            case 4:
                return EnumFacing.WEST;
            case 5:
                return EnumFacing.EAST;
        }
        return null;
    }

    public static void dropItem(ItemStack stack, EntityPlayer player) {
        if (!player.addItemStackToInventory(stack)) player.dropItem(stack, true);
    }

    public static void dropItems(Collection<ItemStack> items, EntityPlayer player) {
        for (ItemStack item : items) dropItem(item, player);
    }

    public static boolean wrenchUse(PlayerInteractEvent event, CompatBase compat) {
        EntityPlayer player = event.getEntityPlayer();
        World worldIn = event.getWorld();
        boolean setConnection = false;
        RayTraceResult lookingAt = Utils.getBlockLookingAtIgnoreBB(player);
        if (lookingAt != null) {
            BlockPos pos = lookingAt.getBlockPos();
            TileEntity te = worldIn.getTileEntity(pos);
            if (te != null) {
                if (!player.isSneaking()) {
                    EnumFacing sideToggled = Utils.getDirection(lookingAt.sideHit, lookingAt.hitVec);
                    if (sideToggled != null) {
                        if (compat.isAcceptable(te)) {
                            if (compat.getConnections(te).contains(sideToggled)) {
                                compat.disconnect(te, sideToggled, player);
                                compat.disconnect(worldIn.getTileEntity(pos.offset(sideToggled, 1)), sideToggled.getOpposite(), player);
                                setConnection = true;
                            } else {
                                compat.connect(te, sideToggled, player);
                                compat.connect(worldIn.getTileEntity(pos.offset(sideToggled, 1)), sideToggled.getOpposite(), player);
                                setConnection = true;
                            }
                        }
                        worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 3);
                        te.getBlockType().onNeighborChange(worldIn, te.getPos(), te.getPos().offset(sideToggled, 1));
                        te.markDirty();
                        TileEntity connectTo = worldIn.getTileEntity(pos.offset(sideToggled, 1));
                        if (connectTo != null) {
                            worldIn.notifyBlockUpdate(connectTo.getPos(), worldIn.getBlockState(connectTo.getPos()), worldIn.getBlockState(connectTo.getPos()), 3);
                            connectTo.getBlockType().onNeighborChange(worldIn, connectTo.getPos(), connectTo.getPos().offset(sideToggled.getOpposite(), 1));
                            connectTo.markDirty();
                        }
                    }
                }
            }
        }
        if (setConnection) {
            worldIn.playSound(player, player.getPosition(), ModSounds.wrench_sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
            player.swingArm(EnumHand.MAIN_HAND);
        }
        return setConnection;
    }
}
