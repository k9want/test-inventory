package com.grizz.inventoryapp.inventory.repository;

import com.grizz.inventoryapp.inventory.repository.entity.InventoryEntity;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Long> {

    @NotNull Optional<InventoryEntity> findByItemId(@NotNull String itemId);

    @Modifying
    @Query("update InventoryEntity i set i.stock = i.stock - :quantity, i.updatedAt = current timestamp " +
            "where i.itemId = :itemId")
    @NotNull Integer decreaseStock(@NotNull String itemId, @NotNull Long quantity);
}
