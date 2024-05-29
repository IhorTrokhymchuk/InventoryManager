package project.inventorymanager.model.file;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import project.inventorymanager.model.user.User;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE statistic_files SET is_deleted = TRUE WHERE id = ?")
@SQLRestriction("is_deleted = FALSE")
@Table(name = "statistic_files")
public class StatisticFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "date_from", nullable = false)
    private LocalDate dateFrom;
    @Column(name = "date_to", nullable = false)
    private LocalDate dateTo;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "dropbox_id", nullable = false, unique = true)
    private String dropboxId;
    @Column(name = "is_deleted",nullable = false)
    private boolean isDeleted = false;
}
