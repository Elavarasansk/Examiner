DROP TABLE IF EXISTS public.vexamine_manager_credit;
DROP SEQUENCE IF EXISTS public.vexamine_manager_credit_id_seq;

CREATE SEQUENCE public.vexamine_manager_credit_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE TABLE IF NOT EXISTS public.vexamine_manager_credit (
  id                          BIGINT NOT NULL DEFAULT nextval('vexamine_manager_credit_id_seq'),
  manager_auth_id             BIGINT NOT NULL,
  purchased_credits           BIGINT DEFAULT 0,
  used_credits                BIGINT DEFAULT 0,
  create_user                 VARCHAR(200),
  update_user                 VARCHAR(200),
  create_date                 TIMESTAMP WITH TIME ZONE,
  update_date                 TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (manager_auth_id) REFERENCES public.vexamine_user_authority_info(id)
);


--------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------