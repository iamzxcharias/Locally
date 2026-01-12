package persistence.mapper;

import domain.model.User;
import org.junit.jupiter.api.Test;
import persistence.entity.UserJpaEntity;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void shouldMapDomainToEntity() {
        User domainUser = new User(UUID.randomUUID(), "Max Mustermann", "max@test.de");

        UserJpaEntity entity = mapper.toEntity(domainUser);

        assertEquals(domainUser.getId(), entity.getId());
        assertEquals(domainUser.getName(), entity.getName());
        assertEquals(domainUser.getEmail(), entity.getEmail());
    }

    @Test
    void shouldMapEntityToDomain() {
        UserJpaEntity entity = new UserJpaEntity(UUID.randomUUID(), "Erika Muster", "erika@test.de");

        User domainUser = mapper.toDomain(entity);

        assertEquals(entity.getId(), domainUser.getId());
        assertEquals(entity.getName(), domainUser.getName());
        assertEquals(entity.getEmail(), domainUser.getEmail());
    }
}