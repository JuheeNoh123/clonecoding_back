package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JPAUserRepository implements UserRepository {
    private final EntityManager em;

    public User findByUserId(String userId){
        try{
            return em.createQuery("select u from User u where u.userId=:userId",User.class)
                    .setParameter("userId",userId).getSingleResult();
        }
        catch(NoResultException e){
            return null;
        }
    }

    @Override
    public User save(User user) {
        em.persist(user);
        return user;
    }


}
