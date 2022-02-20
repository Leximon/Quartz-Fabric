package de.leximon.quartz.testing;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.block.QBlock;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.command.KillCommand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

public class AmogusBlock extends QBlock {

    public AmogusBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getDisplayState(BlockState value) {
        return Blocks.REDSTONE_BLOCK.getDefaultState();
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        Quartz.adventure().player(player.getUuid()).openBook(
                Book.book(
                        Component.text("son"), Component.text("what's this?"),
                        Component.text("Look behind you...")
                )
        );
        Vec3d p1 = player.getPos();
        Vec3d p2 = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        double oppositeSide = p2.x - p1.x;
        double adjacentSide = p2.z - p1.z;
        double distance = Math.sqrt(oppositeSide * oppositeSide + adjacentSide * adjacentSide);
        Vec3f vec = Direction.getEntityFacingOrder(player)[0].getOpposite().getUnitVector();
        Vec3d result = new Vec3d(p1.x + vec.getX() * distance, p2.y, p1.z + vec.getZ() * distance);
        System.out.println(new BlockPos(result.x, result.y, result.z));
        world.setBlockState(new BlockPos(result.x, result.y, result.z), getDefaultState(), Block.NOTIFY_LISTENERS);
        super.onBreak(world, pos, state, player);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.kill();
        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        projectile.getOwner().kill();
        super.onProjectileHit(world, state, hit, projectile);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if(world.isReceivingRedstonePower(pos)) {
            world.removeBlock(pos, false);
            world.createExplosion(null, DamageSource.badRespawnPoint(), null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 30.0F, true, Explosion.DestructionType.DESTROY);
        }
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        world.removeBlock(pos, false);
        world.createExplosion(null, DamageSource.badRespawnPoint(), null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 30.0F, true, Explosion.DestructionType.DESTROY);
    }
}
