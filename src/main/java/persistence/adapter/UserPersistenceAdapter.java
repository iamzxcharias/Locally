package persistence.adapter;

import domain.model.User;
import domain.port.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import persistence.mapper.UserMapper;
import persistence.repository.UserJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserPersistenceAdapter implements UserRepository {

    @Inject
    UserJpaRepository userJpaRepository;

    @Inject
    UserMapper userMapper;

    @Override
    public void save(User user) {
        userJpaRepository.getEntityManager().merge(userMapper.toEntity(user));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.find("email", email).count() > 0;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findByIdOptional(id)
                .map(userMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        // Panache listAll() nutzen und per Mapper umwandeln
        return userJpaRepository.listAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        userJpaRepository.deleteById(id);
    }
}