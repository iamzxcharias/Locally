-- 1. USER (Tabelle: users)
INSERT INTO users (id, name, email)
VALUES ('11111111-1111-1111-1111-111111111111', 'Alice', 'alice@locally.de');

INSERT INTO users (id, name, email)
VALUES ('22222222-2222-2222-2222-222222222222', 'Bob', 'bob@locally.de');

INSERT INTO users (id, name, email)
VALUES ('33333333-3333-3333-3333-333333333333', 'Charlie', 'charlie@locally.de');

INSERT INTO users (id, name, email)
VALUES ('44444444-4444-4444-4444-444444444444', 'Diana', 'diana@locally.de');

-- 2. EVENTS (Tabelle: events)
INSERT INTO events (id, title, category, description, starts_at, place_name, lat, lng, creator_id)
VALUES ('eeeeeeee-1111-1111-1111-eeeeeeeeeeee', 'Locally Tech Meetup', 'TECH', 'Networking für Entwickler', '2026-06-01 18:00:00', 'Coworking Space', 48.1351, 11.5820, '11111111-1111-1111-1111-111111111111');

INSERT INTO events (id, title, category, description, starts_at, place_name, lat, lng, creator_id)
VALUES ('eeeeeeee-2222-2222-2222-eeeeeeeeeeee', 'Pizza Party', 'SOCIAL', 'Essen und Trinken für alle', '2026-07-15 19:30:00', 'Bobs Garten', 48.1400, 11.5900, '22222222-2222-2222-2222-222222222222');

-- 3. FRIENDSHIPS (Tabelle: friendships)
INSERT INTO friendships (id, requester_id, addressee_id, status, created_at)
VALUES (random_uuid(), '11111111-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222', 'ACCEPTED', CURRENT_TIMESTAMP);

INSERT INTO friendships (id, requester_id, addressee_id, status, created_at)
VALUES (random_uuid(), '33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', 'PENDING', CURRENT_TIMESTAMP);

-- 4. PARTICIPATIONS (Tabelle: participations)
INSERT INTO participations (id, user_id, event_id, status, created_at)
VALUES (random_uuid(), '11111111-1111-1111-1111-111111111111', 'eeeeeeee-2222-2222-2222-eeeeeeeeeeee', 'INTERESTED', CURRENT_TIMESTAMP);