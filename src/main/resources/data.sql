INSERT INTO themes (description)
SELECT 'Angular'
WHERE NOT EXISTS (
  SELECT 1 FROM themes WHERE description = 'Angular'
);

INSERT INTO themes (description)
SELECT 'React'
WHERE NOT EXISTS (
  SELECT 1 FROM themes WHERE description = 'React'
);

INSERT INTO themes (description)
SELECT 'Git'
WHERE NOT EXISTS (
  SELECT 1 FROM themes WHERE description = 'Git'
);

INSERT INTO themes (description)
SELECT 'Spring Boot'
WHERE NOT EXISTS (
  SELECT 1 FROM themes WHERE description = 'Spring Boot'
);

INSERT INTO themes (description)
SELECT 'Outros'
WHERE NOT EXISTS (
  SELECT 1 FROM themes WHERE description = 'Outros'
);
