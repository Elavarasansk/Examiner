--------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS public.vexamine_test_category;
DROP SEQUENCE IF EXISTS public.vexamine_test_category_id_seq;

CREATE SEQUENCE public.vexamine_test_category_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE TABLE IF NOT EXISTS public.vexamine_test_category (
  id                          BIGINT NOT NULL DEFAULT nextval('vexamine_test_category_id_seq'),
  category                    TEXT NOT NULL,
  sub_category                TEXT NOT NULL,
  create_user                 VARCHAR(200),
  update_user                 VARCHAR(200),
  create_date                 TIMESTAMP WITH TIME ZONE,
  update_date                 TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

--------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS public.vexamine_question_bank;
DROP SEQUENCE IF EXISTS public.vexamine_question_bank_id_seq;

CREATE SEQUENCE public.vexamine_question_bank_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE TABLE IF NOT EXISTS public.vexamine_question_bank (
  id                          BIGINT NOT NULL DEFAULT nextval('vexamine_question_bank_id_seq'),
  category_id                 BIGINT NOT NULL,
  question_bank_name          TEXT NOT NULL,
  create_user                 VARCHAR(200),
  update_user                 VARCHAR(200),
  create_date                 TIMESTAMP WITH TIME ZONE,
  update_date                 TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (category_id) REFERENCES public.vexamine_test_category (id)
);

--------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS public.vexamine_question_answer;
DROP SEQUENCE IF EXISTS public.vexamine_question_answer_id_seq;

CREATE SEQUENCE public.vexamine_question_answer_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE TABLE IF NOT EXISTS public.vexamine_question_answer (
  id                          BIGINT NOT NULL DEFAULT nextval('vexamine_question_answer_id_seq'),
  question                    TEXT NOT NULL,
  option1                     TEXT,
  option2                     TEXT,
  option3                     TEXT,
  option4                     TEXT,
  answer                      TEXT,
  question_bank_id            BIGINT NULL,
  question_type               BIGINT NULL,
  create_user                 VARCHAR(200),
  update_user                 VARCHAR(200),
  create_date                 TIMESTAMP WITH TIME ZONE,
  update_date                 TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (question_bank_id) REFERENCES public.vexamine_question_bank (id)
);

ALTER TABLE public.vexamine_question_answer RENAME COLUMN "option1" TO "mcq_option1";
ALTER TABLE public.vexamine_question_answer RENAME COLUMN "option2" TO "mcq_option2";
ALTER TABLE public.vexamine_question_answer RENAME COLUMN "option3" TO "mcq_option3";
ALTER TABLE public.vexamine_question_answer RENAME COLUMN "option4" TO "mcq_option4";
ALTER TABLE public.vexamine_question_answer ADD choice_option1 TEXT;
ALTER TABLE public.vexamine_question_answer ADD choice_option2 TEXT;

--------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS public.vexamine_manager_info;
DROP SEQUENCE IF EXISTS public.vexamine_vexamine_manager_info_id_seq;

CREATE SEQUENCE public.vexamine_manager_info_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE TABLE IF NOT EXISTS public.vexamine_manager_info (
  id                          BIGINT NOT NULL DEFAULT nextval('vexamine_manager_info_id_seq'),
  user_auth_id                BIGINT NOT NULL,
  company                     TEXT,
  question_bank_id            BIGINT NOT NULL,
  expiration_date             TIMESTAMP,
  create_user                 VARCHAR(200),
  update_user                 VARCHAR(200),
  create_date                 TIMESTAMP WITH TIME ZONE,
  update_date                 TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (user_auth_id) REFERENCES public.vexamine_user_authority_info (id),
  FOREIGN KEY (question_bank_id) REFERENCES public.vexamine_question_bank (id)
);

--------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------