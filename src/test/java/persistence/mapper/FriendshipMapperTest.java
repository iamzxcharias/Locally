package persistence.mapper;

import domain.model.Friendship;
import domain.model.FriendshipStatus;
import org.junit.jupiter.api.Test;
import persistence.entity.FriendshipJpaEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FriendshipMapperTest {

    private final FriendshipMapper mapper = new FriendshipMapper();

    @Test
    void shouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();
        UUID addresseeId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Friendship domainFriendship = new Friendship(
                id, requesterId, addresseeId, FriendshipStatus.ACCEPTED, now
        );

        FriendshipJpaEntity entity = mapper.toEntity(domainFriendship);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(requesterId, entity.getRequesterId());
        assertEquals(addresseeId, entity.getAddresseeId());
        assertEquals(FriendshipStatus.ACCEPTED, entity.getStatus());
        assertEquals(now, entity.getCreatedAt());
    }

    @Test
    void shouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();
        UUID addresseeId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        FriendshipJpaEntity entity = new FriendshipJpaEntity(
                id, requesterId, addresseeId, now, FriendshipStatus.PENDING
        );

        Friendship domainFriendship = mapper.toDomain(entity);

        assertNotNull(domainFriendship);
        assertEquals(id, domainFriendship.getId());
        assertEquals(requesterId, domainFriendship.getRequesterId());
        assertEquals(addresseeId, domainFriendship.getAddresseeId());
        assertEquals(FriendshipStatus.PENDING, domainFriendship.getStatus());
        assertEquals(now, domainFriendship.getCreatedAt());
    }
}