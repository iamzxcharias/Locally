package persistence.repository;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.entity.FriendshipJpaEntity;
import persistence.entity.UserJpaEntity;
import domain.model.FriendshipStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@QuarkusTest
public class FriendshipJpaRepositoryTest {

    @Inject
    FriendshipJpaRepository friendshipJpaRepository;

    @Inject
    UserJpaRepository userJpaRepository;

    @Test
    @TestTransaction
    @DisplayName("Sollte eine Freundschaftsanfrage speichern und über den Requester finden")
    void testCreateAndFindFriendship() {
        UUID requesterId = UUID.randomUUID();
        UUID addresseeId = UUID.randomUUID();

        UserJpaEntity requester = new UserJpaEntity(requesterId, "Alice", "alice@test.de");
        UserJpaEntity addressee = new UserJpaEntity(addresseeId, "Bob", "bob@test.de");

        userJpaRepository.persist(requester);
        userJpaRepository.persist(addressee);

        FriendshipJpaEntity friendship = new FriendshipJpaEntity(
                UUID.randomUUID(),
                requesterId,
                addresseeId,
                LocalDateTime.now(),
                FriendshipStatus.PENDING
        );

        friendshipJpaRepository.persist(friendship);

        List<FriendshipJpaEntity> foundFriendships = friendshipJpaRepository.findByRequesterId(requesterId);

        Assertions.assertFalse(foundFriendships.isEmpty());
        Assertions.assertEquals(addresseeId, foundFriendships.get(0).getAddresseeId());
        Assertions.assertEquals(FriendshipStatus.PENDING, foundFriendships.get(0).getStatus());
    }
}