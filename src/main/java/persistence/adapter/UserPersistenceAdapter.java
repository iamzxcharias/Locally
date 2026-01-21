package persistence.adapter;

import domain.model.User;
import domain.port.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import persistence.entity.UserJpaEntity;
import persistence.mapper.UserMapper;
import persistence.repository.UserJpaRepository;

import java.util.*;
import java.util.stream.Collectors;

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
        return userJpaRepository.listAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public List<User> search(String q, int page, int size) {
        return userJpaRepository.search(q, page, size).stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countSearch(String q) {
        return userJpaRepository.countSearch(q);
    }
}