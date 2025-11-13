INSERT INTO usuarios (nombre, apellido, email, contrasena, telefono, barrio, ciudad, is_admin)
VALUES ('Juan', 'Pérez', 'juan.perez@example.com', '1234', '1112345678', 'Palermo', 'Buenos Aires', false);

INSERT INTO usuarios (nombre, apellido, email, contrasena, telefono, barrio, ciudad, is_admin)
VALUES ('María', 'Gómez', 'maria.gomez@example.com', 'abcd', '1122334455', 'Centro', 'Córdoba', false);

INSERT INTO usuarios (nombre, apellido, email, contrasena, telefono, barrio, ciudad, is_admin)
VALUES ('Carlos', 'Fernández', 'carlos.fernandez@example.com', 'pass', '1133445566', 'Sur', 'Rosario', false);

INSERT INTO usuarios (nombre, apellido, email, contrasena, telefono, barrio, ciudad, is_admin)
VALUES ('Lucía', 'Martínez', 'lucia.martinez@example.com', 'lucia123', '1144556677', 'Recoleta', 'Buenos Aires', false);

INSERT INTO usuarios (nombre, apellido, email, contrasena, telefono, barrio, ciudad, is_admin)
VALUES ('Admin', 'Sistema', 'admin@example.com', 'admin', '1100000000', 'Microcentro', 'Buenos Aires', true);



-- ==========================
-- ESTADOS
INSERT INTO estado (nombre_estado) VALUES ('Perdido Propio');
INSERT INTO estado (nombre_estado) VALUES ('Perdido Ajeno');
INSERT INTO estado (nombre_estado) VALUES ('Encontrado');
INSERT INTO estado (nombre_estado) VALUES ('Adoptado');

-- ==========================
-- UBICACIONES
-- ==========================
INSERT INTO ubicaciones (barrio, latitud, longitud)
VALUES ('Palermo', -34.588, -58.417);

INSERT INTO ubicaciones (barrio, latitud, longitud)
VALUES ('Centro', -31.416, -64.183);

INSERT INTO ubicaciones (barrio, latitud, longitud)
VALUES ('Recoleta', -34.589, -58.392);

-- Nuevas ubicaciones
INSERT INTO ubicaciones (barrio, latitud, longitud)
VALUES ('Belgrano', -34.562, -58.458);

INSERT INTO ubicaciones (barrio, latitud, longitud)
VALUES ('San Telmo', -34.621, -58.373);

INSERT INTO ubicaciones (barrio, latitud, longitud)
VALUES ('Caballito', -34.618, -58.442);

INSERT INTO ubicaciones (barrio, latitud, longitud)
VALUES ('Villa Urquiza', -34.573, -58.494);

INSERT INTO ubicaciones (barrio, latitud, longitud)
VALUES ('La Boca', -34.634, -58.363);

-- ==========================
-- MASCOTAS
-- ==========================
-- Mascota 1: de Juan Pérez (usuario_id = 1)
INSERT INTO mascotas (usuario_id, nombre, tamano, color, fecha, ubicacion_id, estado_id, descripcion_extra)
VALUES (1, 'Luna', 'Mediana', 'Negra', '2024-03-10', 1, 1, 'Labrador negra, muy juguetona');

-- Mascota 2: de Juan Pérez (usuario_id = 1)
INSERT INTO mascotas (usuario_id, nombre, tamano, color, fecha, ubicacion_id, estado_id, descripcion_extra)
VALUES (1, 'Toby', 'Pequeño', 'Marrón', '2023-11-21', 2, 2, 'Perrito mestizo con collar rojo');

-- Mascota 3: de María Gómez (usuario_id = 2)
INSERT INTO mascotas (usuario_id, nombre, tamano, color, fecha, ubicacion_id, estado_id, descripcion_extra)
VALUES (2, 'Milo', 'Grande', 'Blanco', '2024-01-05', 3, 1, 'Ovejero blanco, se escapó del patio');

-- Mascota 4: de María Gómez (usuario_id = 2)
INSERT INTO mascotas (usuario_id, nombre, tamano, color, fecha, ubicacion_id, estado_id, descripcion_extra)
VALUES (2, 'Nina', 'Mediana', 'Gris', '2024-06-17', 2, 1, 'Gata gris con ojos verdes');

-- Mascota 5: de Carlos Fernández (usuario_id = 3)
INSERT INTO mascotas (usuario_id, nombre, tamano, color, fecha, ubicacion_id, estado_id, descripcion_extra)
VALUES (3, 'Rocky', 'Grande', 'Marrón', '2023-09-30', 1, 2, 'Pitbull rescatado, muy dócil');




-- ==========================
-- AVISTAMIENTOS
-- ==========================

INSERT INTO avistamientos (usuario_id, mascota_id, ubicacion_id, foto, fecha, comentario)
VALUES
    (1, 1, 1, 'foto1.jpg', '2025-11-01T15:30:00', 'La vi cerca del parque central, parecía asustada'),
    (2, 1, 2, 'foto2.jpg', '2025-11-02T18:10:00', 'Pasó por mi calle, llevaba un collar rojo'),
    (3, 2, 3, 'foto3.jpg', '2025-11-03T10:45:00', 'Podría ser la misma que buscan, estaba con otro perro'),
    (1, 3, 4, 'foto4.jpg', '2025-11-04T09:20:00', 'Estaba frente a la panadería, parecía perdida'),
    (2, 4, 2, 'foto5.jpg', '2025-11-05T20:00:00', 'Vi una similar corriendo hacia la plaza del sur'),
    (3, 5, 5, 'foto6.jpg', '2025-11-06T12:30:00', 'Estaba bajo un auto, parecía tener hambre'),
    (1, 2, 1, 'foto7.jpg', '2025-11-07T16:40:00', 'La vi cruzando la avenida, llevaba un arnés azul'),
    (2, 3, 4, 'foto8.jpg', '2025-11-08T11:15:00', 'Está por el barrio Belgrano, parece bien cuidada');
