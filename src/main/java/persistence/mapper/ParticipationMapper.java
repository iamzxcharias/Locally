package persistence.mapper;

import domain.model.Participation;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.ParticipationJpaEntity;

@ApplicationScoped
public class ParticipationMapper {

    public ParticipationJpaEntity toEntity(Participation participation) {
        return new ParticipationJpaEntity(
                participation.getId(),
                participation.getUserId(),
                participation.getEventId(),
                participation.getStatus(),
                participation.getCreatedAt()
        );
    }

    public Participation toDomain(ParticipationJpaEntity entity) {
        return new Participation(
                entity.getId(),
                entity.getUserId(),
                entity.getEventId(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
