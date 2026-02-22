
-- To fazendo os seed respeitando a entrada e saida de acordo com a movimentação para o estoque
INSERT INTO produto (descricao, tipo_produto, valor_no_fornecedor, estoque) VALUES
-- ELETRONICO
('Mouse Gamer RGB',        'ELETRONICO',      85.00,   20), -- +10 -5 => 25
('Teclado Mecânico',       'ELETRONICO',     220.00,   10), -- +8  -3 => 15
('Monitor 24 Polegadas',   'ELETRONICO',     650.00,   10), -- -2      => 8
('Notebook i5 16GB',       'ELETRONICO',    3200.00,    6), -- -1      => 5

-- ELETRODOMESTICO
('Geladeira Frost Free',   'ELETRODOMESTICO',2100.00,   2), -- +2 -1 => 3
('Microondas 30L',         'ELETRODOMESTICO', 550.00,   8), -- -2    => 6
('Liquidificador Turbo',   'ELETRODOMESTICO', 180.00,  10), -- +6 -4 => 12
('Máquina de Lavar 11kg',  'ELETRODOMESTICO',1900.00,   5), -- -1    => 4

-- MOVEL
('Sofá 3 Lugares',         'MOVEL',         1400.00,   3), -- -1 => 2
('Mesa de Escritório',     'MOVEL',          420.00,   5), -- +5 -3 => 7
('Cadeira Gamer',          'MOVEL',          680.00,   7), -- -2 => 5
('Guarda Roupa Casal',     'MOVEL',         1750.00,   4); -- -1 => 3


INSERT INTO movimento_estoque
(codigo_produto, tipo_movimentacao, valor_venda, data_venda, qtd_movimentada)
VALUES
-- Mouse Gamer RGB
(1, 'ENTRADA', NULL, NULL, 10),
(1, 'SAIDA', 129.90, '2026-02-10 10:30:00', 5),

-- Teclado Mecânico
(2, 'ENTRADA', NULL, NULL, 8),
(2, 'SAIDA', 299.90, '2026-02-12 14:00:00', 3),

-- Monitor 24"
(3, 'SAIDA', 899.00, '2026-02-15 16:20:00', 2),

-- Notebook
(4, 'SAIDA', 4200.00, '2026-02-18 11:10:00', 1),

-- Geladeira
(5, 'ENTRADA', NULL, NULL, 2),
(5, 'SAIDA', 2890.00, '2026-02-16 09:45:00', 1),

-- Microondas
(6, 'SAIDA', 749.90, '2026-02-14 13:00:00', 2),

-- Liquidificador
(7, 'ENTRADA', NULL, NULL, 6),
(7, 'SAIDA', 259.90, '2026-02-13 17:30:00', 4),

-- Máquina de lavar
(8, 'SAIDA', 2490.00, '2026-02-19 15:00:00', 1),

-- Sofá
(9, 'SAIDA', 1990.00, '2026-02-11 18:00:00', 1),

-- Mesa escritório
(10, 'ENTRADA', NULL, NULL, 5),
(10, 'SAIDA', 599.00, '2026-02-17 10:00:00', 3),

-- Cadeira gamer
(11, 'SAIDA', 990.00, '2026-02-20 12:40:00', 2),

-- Guarda roupa
(12, 'SAIDA', 2490.00, '2026-02-21 09:00:00', 1);