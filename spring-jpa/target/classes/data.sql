CREATE TABLE clients (
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	first_name VARCHAR(250) NOT NULL,
	last_name VARCHAR(250),
	email VARCHAR(250) NOT NULL
);

CREATE TABLE addresses (
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	id_client INT NOT NULL,
	address VARCHAR(250) NOT NULL,
	number VARCHAR(20) NOT NULL,
	complement VARCHAR(250),
	postal_code VARCHAR(10),
	city VARCHAR(250),
	state VARCHAR(250),
	country VARCHAR(250),
	FOREIGN KEY (id_client) REFERENCES public.clients(id)
);





