package com.grizz.inventoryapp.inventory;

import com.grizz.inventoryapp.inventory.repository.InventoryJpaRepository;
import com.grizz.inventoryapp.inventory.repository.entity.InventoryEntity;
import com.grizz.inventoryapp.inventory.service.domain.Inventory;
import com.grizz.inventoryapp.inventory.service.exception.InsufficientStockException;
import com.grizz.inventoryapp.inventory.service.exception.InvalidDecreaseQuantityException;
import com.grizz.inventoryapp.inventory.service.exception.ItemNotFoundException;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InventoryService {
    private final InventoryJpaRepository inventoryJpaRepository;

    public InventoryService(InventoryJpaRepository inventoryJpaRepository) {
        this.inventoryJpaRepository = inventoryJpaRepository;
    }

    public @Nullable Inventory findByItemId(@NotNull String itemId) {
        return inventoryJpaRepository.findByItemId(
            itemId).map(this::mapToDomain)
            .orElse(null);
    }

    public @NotNull Inventory descreasByItemId(@NotNull String itemId, @NotNull Long quantity) {
        if (quantity < 0) {
            throw new InvalidDecreaseQuantityException();
        }
        // before
//        final Optional<InventoryEntity> optionalEntity = inventoryJpaRepository.findByItemId(itemId);
//        if (optionalEntity.isEmpty()) {
//            throw new ItemNotFoundException();
//        }
//
//        final InventoryEntity entity = optionalEntity.get();

        // after
        final InventoryEntity entity = inventoryJpaRepository.findByItemId(itemId)
            .orElseThrow(ItemNotFoundException::new);

        if (quantity > entity.getStock()) { // 재고가 부족하면?
            throw new InsufficientStockException();
        }

        return null;
    }

    private Inventory mapToDomain(InventoryEntity entity) {
        return new Inventory(entity.getItemId(), entity.getStock());
    }
}
