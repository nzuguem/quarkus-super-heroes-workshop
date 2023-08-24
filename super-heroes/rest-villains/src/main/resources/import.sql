ALTER SEQUENCE villain_seq RESTART WITH 50;

INSERT INTO villain(id, name, otherName, picture, powers, level)
VALUES (nextval('villain_seq'), 'Buuccolo', 'Majin Buu',
        'https://www.superherodb.com/pictures2/portraits/10/050/15355.jpg',
        'Accelerated Healing, Adaptation, Agility, Flight, Immortality, Intelligence, Invulnerability, Reflexes, Self-Sustenance, Size Changing, Spatial Awareness, Stamina, Stealth, Super Breath, Super Speed, Super Strength, Teleportation',
        22);
INSERT INTO villain(id, name, otherName, picture, powers, level)
VALUES (nextval('villain_seq'), 'Darth Vader', 'Anakin Skywalker',
        'https://www.superherodb.com/pictures2/portraits/10/050/10444.jpg',
        'Accelerated Healing, Agility, Astral Projection, Cloaking, Danger Sense, Durability, Electrokinesis, Energy Blasts, Enhanced Hearing, Enhanced Senses, Force Fields, Hypnokinesis, Illusions, Intelligence, Jump, Light Control, Marksmanship, Precognition, Psionic Powers, Reflexes, Stealth, Super Speed, Telekinesis, Telepathy, The Force, Weapons Master',
        13);
INSERT INTO villain(id, name, otherName, picture, powers, level)
VALUES (nextval('villain_seq'), 'The Rival (CW)', 'Edward Clariss',
        'https://www.superherodb.com/pictures2/portraits/10/050/13846.jpg',
        'Accelerated Healing, Agility, Bullet Time, Durability, Electrokinesis, Endurance, Enhanced Senses, Intangibility, Marksmanship, Phasing, Reflexes, Speed Force, Stamina, Super Speed, Super Strength',
        10);
