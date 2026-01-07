package domain.port;

import domain.model.Friendship;
import java.util.List;
import java.util.UUID;

public interface FriendshipRepository {

    void save(Friendship friendship);

    //Prüft für UC4 (Invite Friend), ob eine Freundschaft existiert.
    boolean existsByRequesterIdAndAddresseeId(UUID requesterId, UUID addresseeId);


    //Findet für UC5 (Friend Feed) alle Freundschaften eines Users.
    //Wir suchen nach Einträgen, wo der User ENTWEDER der Absender ODER der Empfänger ist.
    List<Friendship> findAllByRequesterIdOrAddresseeId(UUID requesterId, UUID addresseeId);
}