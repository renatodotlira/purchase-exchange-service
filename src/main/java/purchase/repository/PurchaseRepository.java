package purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import purchase.domain.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
