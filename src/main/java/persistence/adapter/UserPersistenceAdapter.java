package persistence.adapter;

import domain.model.User;
import domain.port.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import persistence.entity.UserJpaEntity;
import persistence.mapper.UserMapper;
import persistence.repository.UserJpaRepository;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserPersistenceAdapter implements UserRepository {

    @Inject
    UserJpaRepository userJpaRepository;

    @Inject
    UserMapper userMapper;

    @Override
    public void save(User user) {
        UserJpaEntity entity = userMapper.toEntity(user);

        userJpaRepository.getEntityManager().merge(entity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findByIdOptional(id)
                .map(userMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.findByEmail(email).isPresent();
    }
}
