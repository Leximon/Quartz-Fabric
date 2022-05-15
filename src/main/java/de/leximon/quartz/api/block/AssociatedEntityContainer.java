package de.leximon.quartz.api.block;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AssociatedEntityContainer {

    private final BlockEntity blockEntity;
    private final HashMap<String, AssociatedEntity<?>> entities = new HashMap<>();

    public AssociatedEntityContainer(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public <E extends Entity> void add(String tag, Function<ServerWorld, E> spawnFunction, Consumer<E> onEntitySpawn) {
        entities.put(tag, new AssociatedEntity<>(spawnFunction, onEntitySpawn));
    }

    public void killAll() {
        entities.forEach((tag, associated) -> {
            if (associated.getEntity() != null) {
                associated.getEntity().remove(Entity.RemovalReason.KILLED);
                associated.setEntity(null);
            }
        });
    }

    public void spawnAll() {
        ServerWorld world = (ServerWorld) blockEntity.getWorld();
        for (Map.Entry<String, AssociatedEntity<?>> entry : entities.entrySet()) {
            AssociatedEntity<?> associated = entry.getValue();
            if(associated.getEntity() != null)
                continue;
            if(associated.entityUUID != null) {
                associated.setEntityFromUUID(world);
                continue;
            }
            associated.spawn(world);
        }
    }

    public void writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();
        for (Map.Entry<String, AssociatedEntity<?>> entry : entities.entrySet()) {
            AssociatedEntity<?> associated = entry.getValue();
            if(associated.getEntityUUID() == null)
                continue;
            NbtCompound entityCompound = new NbtCompound();
            entityCompound.putString("tag", entry.getKey());
            entityCompound.put("entity", NbtHelper.fromUuid(associated.getEntityUUID()));
            list.add(entityCompound);
        }
        nbt.put("associatedEntities", list);
    }

    public void readNbt(NbtCompound nbt) {
        NbtList list = nbt.getList("associatedEntities", NbtElement.COMPOUND_TYPE);
        for (NbtElement element : list) {
            NbtCompound entityCompound = ((NbtCompound) element);
            String tag = entityCompound.getString("tag");
            UUID uuid = NbtHelper.toUuid(entityCompound.get("entity"));

            AssociatedEntity<?> associated = entities.get(tag);
            associated.entityUUID = uuid;
        }
    }

    @Setter
    @Getter
    public static class AssociatedEntity<E extends Entity> {
        private final Function<ServerWorld, E> spawnFunction;
        private final Consumer<E> onEntitySpawn;
        private E entity;
        private UUID entityUUID;

        public AssociatedEntity(Function<ServerWorld, E> spawnFunction, Consumer<E> onEntitySpawn) {
            this.spawnFunction = spawnFunction;
            this.onEntitySpawn = onEntitySpawn;
        }

        @SuppressWarnings("unchecked")
        public void setEntityFromUUID(ServerWorld world) {
            this.entity = (E) world.getEntity(entityUUID);
            onEntitySpawn.accept(this.entity);
        }

        public void spawn(ServerWorld world) {
            E entity = spawnFunction.apply(world);
            if(world.spawnEntity(entity)) {
                this.entity = entity;
                this.entityUUID = entity.getUuid();
                onEntitySpawn.accept(this.entity);
            }
        }
    }

}
