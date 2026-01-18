package persistence.mapper;

import domain.model.Event;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.EventJpaEntity;

@ApplicationScoped
public class EventMapper {

    public EventJpaEntity toEntity(Event event) {
        return new EventJpaEntity(
                event.getId(),
                event.getTitle(),
                event.getCategory(),
                event.getDescription(),
                event.getStartsAt(),
                event.getPlaceName(),
                event.getLat(),
                event.getLng(),
                event.getCreatorId(),
                event.getParticipantCount()
        );
    }

    public Event toDomain(EventJpaEntity entity) {
        return new Event(
                entity.getId(),
                entity.getTitle(),
                entity.getCategory(),
                entity.getDescription(),
                entity.getStartsAt(),
                entity.getPlaceName(),
                entity.getLat(),
                entity.getLng(),
                entity.getCreatorId(),
                entity.getParticipantCount()
        );
    }
}
