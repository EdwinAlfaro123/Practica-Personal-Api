package PracticaPersonal2.PracticaPersonal2.Repository;

import PracticaPersonal2.PracticaPersonal2.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
