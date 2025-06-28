-- Create Schema
CREATE SCHEMA IF NOT EXISTS makerspace_inventory;

-- Create Sequences
CREATE SEQUENCE IF NOT EXISTS makerspace_inventory.users_seq
    INCREMENT 50
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

-- Inventory Table
CREATE TABLE makerspace_inventory.Inventory (
    SKU INT PRIMARY KEY,
    domain VARCHAR(255),
    type VARCHAR(255),
    brand VARCHAR(255),
    part_number VARCHAR(255),
    ohq INT, -- On Hand Quantity (stock)
    mohq INT, -- Maximum On Hand Quantity
    moq INT, -- Minimum Order Quantity
    unit_cost double precision,
    link TEXT,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Item Table
CREATE TABLE makerspace_inventory.Item (
    item_id SERIAL PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    unit_of_measurement VARCHAR(50),
    SKU INT UNIQUE NOT NULL,
    FOREIGN KEY (SKU) REFERENCES makerspace_inventory.Inventory(SKU)
);

-- Supplier Table
CREATE TABLE makerspace_inventory.Supplier (
    supplier_id SERIAL PRIMARY KEY,
    supplier_name VARCHAR(255) NOT NULL,
    supplier_link TEXT
);

-- User Table (for authentication)
CREATE TABLE makerspace_inventory.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Order Table
CREATE TABLE makerspace_inventory.Order (
    order_id SERIAL PRIMARY KEY,
    sku INT NOT NULL,
    status VARCHAR(25) NOT NULL, -- NOT ORDERED, PENDING, RECEIVED
    quantity INT NOT NULL,
    last_ordered TIMESTAMP,
    date_received TIMESTAMP,
    amount_received INT,
    FOREIGN KEY (sku) REFERENCES makerspace_inventory.Item(sku)
);

-- Transaction Table
CREATE TABLE makerspace_inventory.Transaction (
    transaction_id SERIAL PRIMARY KEY,
    transaction_inventory_id INT NOT NULL REFERENCES makerspace_inventory.Inventory(sku),
    user_id INT REFERENCES makerspace_inventory.users(id),
    quantity INT NOT NULL,
    time_of_transaction TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for frequently queried columns
CREATE INDEX idx_inventory_sku ON makerspace_inventory.Inventory(sku);
CREATE INDEX idx_domain ON makerspace_inventory.Inventory(domain);
CREATE INDEX idx_type ON makerspace_inventory.Inventory(type);
CREATE INDEX idx_brand ON makerspace_inventory.Inventory(brand);
CREATE INDEX idx_supplier_id ON makerspace_inventory.Supplier(supplier_id);
CREATE INDEX idx_order_sku ON makerspace_inventory.Order(sku);
CREATE INDEX idx_transaction_inventory_id ON makerspace_inventory.Transaction(transaction_inventory_id);

-- Add default users
INSERT INTO makerspace_inventory.users (id, username, password, role)
    VALUES (1, 'MakerspaceAdmin', '$2a$12$pejSDmmkdVM0O0asijd7gOaeAVZTkI2.0TjEBopwLJgTkHe3rcwZK', 'ROLE_ADMIN')
    ON CONFLICT (id) DO NOTHING;
