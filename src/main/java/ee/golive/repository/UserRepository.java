package ee.golive.repository;

import ee.golive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
  User findUserByEmail(String email);
}
