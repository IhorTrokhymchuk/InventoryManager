package project.inventorymanager.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.inventorymanager.model.inventoryaction.InventoryAction;

public interface InventoryActionRepository extends JpaRepository<InventoryAction, Long> {
    @EntityGraph(attributePaths = {
            "product", "product.categories",
            "warehouse", "inventoryActionType"})
    Page<InventoryAction> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {
            "product", "product.categories",
            "warehouse", "inventoryActionType"})
    Optional<InventoryAction> findById(Long id);

    @EntityGraph(attributePaths = {"product", "warehouse", "inventoryActionType"})
    @Query("SELECT ia FROM InventoryAction ia "
            + "WHERE DATE(ia.createdAt) >= :fromDate "
            + "AND DATE(ia.createdAt) <= :toDate")
    List<InventoryAction> findAllByDates(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);
}
