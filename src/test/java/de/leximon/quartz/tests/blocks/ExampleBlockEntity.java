package de.leximon.quartz.tests.blocks;

import de.leximon.quartz.QuartzInitializer;
import de.leximon.quartz.tests.TestInitializer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import java.util.List;

public class ExampleBlockEntity extends LootableContainerBlockEntity {

    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    private int charge = 0;

    public ExampleBlockEntity(BlockPos pos, BlockState state) {
        super(TestInitializer.EXAMPLE_BLOCK_ENTITY, pos, state);
    }

    // read and write
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(nbt)) {
            Inventories.readNbt(nbt, this.inventory);
        }
        this.charge = nbt.getInt("charge");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!this.serializeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory);
        }
        nbt.putInt("charge", this.charge);
    }

    // inventory
    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        inventory = list;
    }

    @Override
    protected Text getContainerName() {
        return new LiteralText("Flamethrower");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new Generic3x3ContainerScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public int size() {
        return inventory.size();
    }

    // tick
    public static <T extends BlockEntity> void serverTick(World world, BlockPos blockPos, BlockState blockState, ExampleBlockEntity blockEntity) {

        if(blockEntity.charge <= 0) {
            for (ItemStack itemStack : blockEntity.inventory) {
                if(!itemStack.isOf(Items.COAL))
                    continue;
                itemStack.decrement(1);
                blockEntity.charge = 40;
                break;
            }
        } else {
            blockEntity.charge--;
        }
        if(blockEntity.charge <= 0)
            return;
        // particle and damage
        ServerWorld w = (ServerWorld) world;
        Vec3f dirVec = blockState.get(Properties.FACING).getUnitVector();
        for(int i = 0; i < 5; i++) {
            final float s = 0.075f;
            float x = dirVec.getX();
            float y = dirVec.getY();
            float z = dirVec.getZ();
            w.spawnParticles(ParticleTypes.FLAME,
                    blockPos.getX() + 0.5 + x / 2 + x * Math.random() * 0.3 - x * 0.3,
                    blockPos.getY() + 0.5 + y / 2 + y * Math.random() * 0.3 - y * 0.3,
                    blockPos.getZ() + 0.5 + z / 2 + z * Math.random() * 0.3 - z * 0.3,
                    0,
                    x == 0 ? (Math.random()*2-1)*s : x,
                    y == 0 ? (Math.random()*2-1)*s : y,
                    z == 0 ? (Math.random()*2-1)*s : z, 0.8
            );
        }
        int prevI = -1;
        for (float i = 0; i < 15; i += 0.25) {
            float distance = (i / 20f);
            float nDistance = (1 - distance);
            Vec3f p = dirVec.copy();
            p.scale(i + 0.6f);
            BlockPos pos = new BlockPos(
                    blockPos.getX() + 0.5 + p.getX(),
                    blockPos.getY() + 0.5 + p.getY(),
                    blockPos.getZ() + 0.5 + p.getZ()
            );
            if (prevI != i) {
                List<Entity> entities = w.getOtherEntities(null, new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(-0.4 + distance * 0.8));
                for (Entity entity : entities) {
                    if (entity.isFireImmune())
                        continue;
                    entity.damage(DamageSource.IN_FIRE, 4f * nDistance);
                    entity.setFireTicks((int) (20 * 5 * nDistance));
                }
                prevI = (int) i;
                if (!world.getBlockState(pos).getCollisionShape(world, pos).isEmpty())
                    break;
            }

        }
    }
}
