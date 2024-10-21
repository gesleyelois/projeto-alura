package br.com.alura.ProjetoAlura.user;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsById(Long id);
    User findByEmail(String email);
    User findUserById (Long id);
}
