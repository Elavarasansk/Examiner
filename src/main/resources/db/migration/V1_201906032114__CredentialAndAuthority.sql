
DROP TABLE IF EXISTS public.vexamine_user_credentials;
DROP SEQUENCE IF EXISTS public.vexamine_user_credentials_id_seq;

CREATE SEQUENCE public.vexamine_user_credentials_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE TABLE IF NOT EXISTS public.vexamine_user_credentials (
  id                          BIGINT NOT NULL DEFAULT nextval('vexamine_user_credentials_id_seq'),
  mail_id                     VARCHAR(150) UNIQUE NOT NULL,
  user_name                     VARCHAR(150),
  hashed_password             BYTEA,
  hashed_salt                 BYTEA,
  create_user                 VARCHAR(200),
  update_user                 VARCHAR(200),
  create_date                 TIMESTAMP,
  invite_sent                 BOOLEAN,
  update_date                 TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

--------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS public.vexamine_role;
DROP SEQUENCE IF EXISTS public.vexamine_role_id_seq;

CREATE SEQUENCE public.vexamine_role_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE TABLE IF NOT EXISTS public.vexamine_role (
  id                          BIGINT NOT NULL DEFAULT nextval('vexamine_role_id_seq'),
  type                        TEXT UNIQUE DEFAULT NULL,
  description                 TEXT DEFAULT NULL,
  create_user                 VARCHAR(200),
  update_user                 VARCHAR(200),
  create_date                 TIMESTAMP WITH TIME ZONE,
  update_date                 TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

--------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS public.vexamine_user_authority_info;
DROP SEQUENCE IF EXISTS public.vexamine_user_authority_info_id_seq;

CREATE SEQUENCE public.vexamine_user_authority_info_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE TABLE IF NOT EXISTS public.vexamine_user_authority_info (
  id                          BIGINT NOT NULL DEFAULT nextval('vexamine_user_authority_info_id_seq'),
  user_id                     BIGINT NOT NULL,
  user_role_id                BIGINT NOT NULL,
  create_user                 VARCHAR(200),
  update_user                 VARCHAR(200),
  create_date                 TIMESTAMP WITH TIME ZONE,
  update_date                 TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES public.vexamine_user_credentials (id),
  FOREIGN KEY (user_role_id) REFERENCES public.vexamine_role (id)
);

--------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------
