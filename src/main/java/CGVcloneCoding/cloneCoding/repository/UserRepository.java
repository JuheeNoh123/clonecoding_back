package CGVcloneCoding.cloneCoding.repository;

import CGVcloneCoding.cloneCoding.domain.User;

public interface UserRepository {

    User findByUserId(String userId);

    User save(User user);
}
