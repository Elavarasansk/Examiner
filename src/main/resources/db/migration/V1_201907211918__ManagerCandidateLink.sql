DROP TABLE IF EXISTS public.vexamine_manager_credit_candidate_list;

CREATE TABLE vexamine_manager_credit_candidate_list (
  manager_credit_id bigint NOT NULL,
  candidate_list_id bigint NOT NULL,
  CONSTRAINT vexamine_manager_credit_candidate_list_pkey PRIMARY KEY (manager_credit_id, candidate_list_id),
  CONSTRAINT fkf6db1500mcspgacl63l9vx624 FOREIGN KEY (candidate_list_id)
      REFERENCES vexamine_user_authority_info (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fksglw4gsmky6ewvxx8xas5vgga FOREIGN KEY (manager_credit_id)
      REFERENCES vexamine_manager_credit (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

