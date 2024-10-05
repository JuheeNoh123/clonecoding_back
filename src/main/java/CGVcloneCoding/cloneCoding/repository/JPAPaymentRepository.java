package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.Payment;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JPAPaymentRepository implements PaymentRepository {
    private final EntityManager em;
    @Override
    public void save(Payment payment) {
        em.persist(payment);
    }
}
