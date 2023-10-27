CREATE TABLE users(
	id uuid NOT NULL PRIMARY KEY,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	email varchar(320) NOT NULL UNIQUE,
	pwd varchar(255) NOT NULL,
	verified bool NOT NULL DEFAULT false,	
	verif_code varchar(255),
	verif_code_exp_at bigint,
	created_by varchar(320) NOT NULL,
	updated_by varchar(320) NOT NULL,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp
);

CREATE TABLE roles(
	id uuid NOT NULL PRIMARY KEY,
	"name" varchar(32) NOT NULL UNIQUE,
	"desc" varchar(255),
	active boolean NOT NULL,
	created_by varchar(320) NOT NULL,
	updated_by varchar(320) NOT NULL,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp
);

CREATE TABLE permissions(
	id uuid NOT NULL PRIMARY KEY,
	"name" varchar(32) NOT NULL UNIQUE,
	"desc" varchar(255),
	"type" varchar(32),
	"url" varchar(255),
	url_method varchar(16),
	active boolean NOT NULL,
	created_by varchar(320) NOT NULL,
	updated_by varchar(320) NOT NULL,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp
);

CREATE TABLE role_permission(
	id serial8 NOT NULL PRIMARY KEY,
	role_name varchar(32) NOT NULL,
	permission_name varchar(32),
	active boolean NOT NULL,
	created_by varchar(320) NOT NULL,
	updated_by varchar(320) NOT NULL,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp
);

CREATE TABLE sessions(
	id uuid NOT NULL PRIMARY KEY,
	user_id uuid NOT NULL,
	signed_in_at timestamptz NOT NULL,
	signed_out_at timestamptz,
	ref_token varchar(255),
	ref_token_exp_at bigint,
	created_by varchar(320) NOT NULL,
	updated_by varchar(320) NOT NULL,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp
);

CREATE TABLE plans(
	id uuid NOT NULL PRIMARY KEY,
	"name" varchar(255) NOT NULL UNIQUE,
	"desc" text,
	avatar varchar(255),
	deleted boolean NOT NULL DEFAULT true,
	created_by varchar(320) NOT NULL,
	updated_by varchar(320) NOT NULL,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp
);

CREATE TABLE user_role(
	id serial8 NOT NULL PRIMARY KEY,
	user_id uuid NOT NULL,
	role_name varchar(32) NOT NULL,
	active boolean NOT NULL DEFAULT false,
	created_by varchar(320) NOT NULL,
	updated_by varchar(320) NOT NULL,
	created_at timestamptz NOT NULL DEFAULT current_timestamp,
	updated_at timestamptz NOT NULL DEFAULT current_timestamp
);