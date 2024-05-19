package com.grizz.inventoryapp.inventory.repository;

import com.grizz.inventoryapp.inventory.repository.entity.InventoryEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.jetbrains.annotations.NotNull;

public class InventoryJpaRepositoryStub implements InventoryJpaRepository {

    // db 연결 x -> in memory를 통해 (stub)
    private final List<InventoryEntity> inventoryEntities = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public void addInventoryEntity(@NotNull String itemId, @NotNull Long stock) {
        final Long id = idGenerator.getAndIncrement();
        inventoryEntities.add(new InventoryEntity(id, itemId, stock));
    }

    @Override
    public @NotNull Optional<InventoryEntity> findByItemId(@NotNull String itemId) {
        return inventoryEntities.stream()
            .filter(entity -> entity.getItemId().equals(itemId))
            .findFirst();
    }

    @Override
    public @NotNull Integer decreaseStock(@NotNull String itemId, @NotNull Long quantity) {
        final Optional<InventoryEntity> optionalEntity = inventoryEntities.stream()
            .filter(entity -> entity.getItemId().equals(itemId))
            .findFirst();

        if (optionalEntity.isEmpty()) {
            return 0;
        }

        final InventoryEntity entity = optionalEntity.get();
        entity.setStock(entity.getStock() - quantity);

        return 1;
    }

    @Override
    public @NotNull InventoryEntity save(@NotNull InventoryEntity inventoryEntity) {
        final Long entityId = inventoryEntity.getId();
        final Optional<InventoryEntity> optionalEntity = inventoryEntities.stream()
            .filter(entity -> entity.getId() != null && entity.getId().equals(entityId))
            .findFirst();

        InventoryEntity entity;
        if (optionalEntity.isPresent()) {
            entity = optionalEntity.get();
            entity.setStock(inventoryEntity.getStock());
        } else {
            final Long id = idGenerator.getAndIncrement();
            entity = new InventoryEntity(
                id,
                inventoryEntity.getItemId(),
                inventoryEntity.getStock()
            );
            inventoryEntities.add(inventoryEntity);
        }
        return entity;
    }
}