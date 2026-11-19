SET client_encoding = 'UTF8';

-- 1. Tabla de Contenedores Marítimos
CREATE TABLE IF NOT EXISTS contenedores (
    id SERIAL PRIMARY KEY,
    numero_contenedor VARCHAR(50) UNIQUE NOT NULL,
    naviera VARCHAR(50) NOT NULL,
    puerto_origen VARCHAR(100) NOT NULL,
    puerto_destino VARCHAR(100) NOT NULL,
    estado_aduanero VARCHAR(50) NOT NULL, -- 'EN_TRANSITO_MARITIMO', 'EN_INSPECCION_DIAN', 'NACIONALIZADO_EN_BODEGA'
    fecha_embarque DATE NOT NULL,
    fecha_llegada_estimada DATE NOT NULL, -- ETA
    notas_aduaneras TEXT
);

-- 2. Tabla de Catálogo de Productos Cosméticos (SKUs)
CREATE TABLE IF NOT EXISTS productos (
    id SERIAL PRIMARY KEY,
    sku VARCHAR(50) UNIQUE NOT NULL,
    nombre_producto VARCHAR(150) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    precio_unitario NUMERIC(10, 2) NOT NULL
);

-- 3. Detalle de Embarque (Qué productos y cantidades van en cada contenedor)
CREATE TABLE IF NOT EXISTS embarques_detalle (
    id SERIAL PRIMARY KEY,
    contenedor_id INT REFERENCES contenedores(id) ON DELETE CASCADE,
    producto_id INT REFERENCES productos(id) ON DELETE CASCADE,
    orden_compra VARCHAR(50) NOT NULL,
    unidades_en_transito INT NOT NULL,
    lote_fabricacion VARCHAR(50)
);

-- ====================================================================
-- DATOS SEMILLA REALES (Caso Empresa de Maquillaje)
-- ====================================================================

INSERT INTO productos (sku, nombre_producto, categoria, precio_unitario) VALUES
('MK-LIP-04', 'Labial Mate Larga Duración Tono 04 Rubí', 'Labios', 35000.00),
('MK-FND-02', 'Base Líquida Cobertura Total Tono 02 Beige Natural', 'Rostro', 58000.00),
('MK-SRM-30', 'Serum Facial Hidratante Ácido Hialurónico 30ml', 'Cuidado Facial', 72000.00),
('MK-MSK-01', 'Pestañina Waterproof Volumen Extremo Negro', 'Ojos', 42000.00),
('MK-FND-03', 'Base Líquida Cobertura Total Tono 03 Beige Oscuro', 'Rostro', 58000.00)
ON CONFLICT (sku) DO NOTHING;

INSERT INTO contenedores (numero_contenedor, naviera, puerto_origen, puerto_destino, estado_aduanero, fecha_embarque, fecha_llegada_estimada, notas_aduaneras) VALUES
('MSCU9823145', 'MSC', 'Shanghai, China', 'Buenaventura, Colombia', 'EN_INSPECCION_DIAN', '2026-10-15', '2026-11-20', 'Llegó a puerto el 18 de Noviembre. En proceso de aforo físico e inspección sanitaria INVIMA.'),
('MAEU4581290', 'Maersk', 'Busan, Corea del Sur', 'Cartagena, Colombia', 'EN_TRANSITO_MARITIMO', '2026-11-02', '2026-12-05', 'Navegando en travesía pacífico. Sin novedades meteorológicas reportadas.'),
('CMAU7712399', 'CMA CGM', 'Ningbo, China', 'Buenaventura, Colombia', 'NACIONALIZADO_EN_BODEGA', '2026-09-20', '2026-10-28', 'Levante aduanero aprobado. Mercancía disponible en Centro de Distribución Principal.')
ON CONFLICT (numero_contenedor) DO NOTHING;

INSERT INTO embarques_detalle (contenedor_id, producto_id, orden_compra, unidades_en_transito, lote_fabricacion) VALUES
(1, 1, 'PO-2026-881', 5000, 'LOT-LIP-2026A'),
(1, 2, 'PO-2026-881', 3500, 'LOT-FND-2026B'),
(2, 3, 'PO-2026-902', 8000, 'LOT-SRM-2026C'),
(3, 4, 'PO-2026-750', 12000, 'LOT-MSK-2026A'),
(2, 5, 'PO-2026-902', 5000, 'LOT-FND-2026C')
ON CONFLICT DO NOTHING;