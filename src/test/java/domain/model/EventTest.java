package domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private final UUID creatorId = UUID.randomUUID();
    private final LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

    @Test
    @DisplayName("Sollte ein valides Event erfolgreich erstellen")
    void shouldCreateValidEvent() {
        assertDoesNotThrow(() -> new Event(
                "Flohmarkt", "SOCIAL", "Beschreibung",
                futureDate, "Marktplatz", 49.0, 10.0, creatorId
        ));
    }

    @Test
    @DisplayName("Sollte Fehler werfen, wenn der Titel leer ist")
    void shouldThrowExceptionWhenTitleIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Event("", "CAT", "Desc", futureDate, "Place", 49.0, 10.0, creatorId)
        );
        assertEquals("Event title must not be empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Sollte Fehler werfen, wenn das Datum in der Vergangenheit liegt")
    void shouldThrowExceptionForPastDate() {
        LocalDateTime pastDate = LocalDateTime.now().minusMinutes(1);
        assertThrows(IllegalArgumentException.class, () ->
                new Event("Titel", "CAT", "Desc", pastDate, "Place", 49.0, 10.0, creatorId)
        );
    }

    @Test
    @DisplayName("Sollte Fehler werfen, wenn die Latitude außerhalb des Bereichs liegt")
    void shouldThrowExceptionForInvalidLatitude() {
        assertThrows(IllegalArgumentException.class, () ->
                new Event("Titel", "CAT", "Desc", futureDate, "Place", 90.1, 10.0, creatorId)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Event("Titel", "CAT", "Desc", futureDate, "Place", -91.0, 10.0, creatorId)
        );
    }

    @Test
    @DisplayName("Sollte Fehler werfen, wenn die Longitude außerhalb des Bereichs liegt")
    void shouldThrowExceptionForInvalidLongitude() {
        assertThrows(IllegalArgumentException.class, () ->
                new Event("Titel", "CAT", "Desc", futureDate, "Place", 49.0, 180.5, creatorId)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Event("Titel", "CAT", "Desc", futureDate, "Place", 49.0, -181.0, creatorId)
        );
    }

    @Test
    @DisplayName("Sollte Fehler werfen, wenn die CreatorId fehlt")
    void shouldThrowExceptionWhenCreatorIdIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Event("Titel", "CAT", "Desc", futureDate, "Place", 49.0, 10.0, null)
        );
    }
}