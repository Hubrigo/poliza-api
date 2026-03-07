INSERT INTO polizas (tipo, estado, canon_mensual, prima, vigencia_meses, fecha_inicio, fecha_fin)
VALUES ('COLECTIVA', 'ACTIVA', 1000000.00, 12000000.00, 12, '2025-01-01', '2025-12-31');

INSERT INTO polizas (tipo, estado, canon_mensual, prima, vigencia_meses, fecha_inicio, fecha_fin)
VALUES ('INDIVIDUAL', 'ACTIVA', 800000.00, 9600000.00, 12, '2025-01-01', '2025-12-31');

INSERT INTO riesgos (descripcion, estado, poliza_id)
VALUES ('Apartamento Torre 1', 'ACTIVO', 1);

INSERT INTO riesgos (descripcion, estado, poliza_id)
VALUES ('Local Comercial 201', 'ACTIVO', 1);

INSERT INTO riesgos (descripcion, estado, poliza_id)
VALUES ('Apartamento Individual 301', 'ACTIVO', 2);