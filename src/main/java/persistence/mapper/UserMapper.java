package persistence.mapper;

import domain.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.UserJpaEntity;

@ApplicationScoped
public class UserMapper {

    public UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail()
        );
    }
}
