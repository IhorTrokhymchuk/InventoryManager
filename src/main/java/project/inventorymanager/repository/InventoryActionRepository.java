package project.inventorymanager.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.inventorymanager.model.inventoryaction.InventoryAction;

public interface InventoryActionRepository extends JpaRepository<InventoryAction, Long> {
    Optional<InventoryAction> findByIdAndUserEmail(Long id, String email);

    Page<InventoryAction> findAllByUserEmail(Pageable pageable, String email);

    @Query("SELECT ia FROM InventoryAction ia "
            + "WHERE ia.user.email = :userEmail "
            + "AND DATE(ia.createdAt) >= :fromDate "
            + "AND DATE(ia.createdAt) <= :toDate")
    List<InventoryAction> findAllByUserEmailAndCreatedAtBetween(
            @Param("userEmail") String userEmail,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);
}
