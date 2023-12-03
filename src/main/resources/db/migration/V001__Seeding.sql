-- ## Create UUID generator
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ## Seeding Role
INSERT INTO roles (id,"name","desc",active,created_by,updated_by,created_at,updated_at) VALUES
  (gen_random_uuid(),'ADMIN','Administrator',true,'SYSTEM','SYSTEM','2023-11-24 13:17:50.933535+07','2023-11-24 13:17:50.933535+07'),
  (gen_random_uuid(),'USER','End-user',true,'SYSTEM','SYSTEM','2023-11-24 13:18:32.745687+07','2023-11-24 13:18:32.745687+07');

-- ## Seeding Permission
INSERT INTO permissions (id,"name","desc","type",url,url_method,active,created_by,updated_by,created_at,updated_at) VALUES
  (gen_random_uuid(),'CREATE_PLAN','Create plan','ACTION','/api/v1/plans','POST',true,'SYSTEM','SYSTEM','2023-11-24 13:20:47.874116+07','2023-11-24 13:20:47.874116+07'),
  (gen_random_uuid(),'EDIT_PLAN','Edit plan','ACTION','/api/v1/plans','PUT',true,'SYSTEM','SYSTEM','2023-11-24 13:21:43.873357+07','2023-11-24 13:21:43.873357+07'),
  (gen_random_uuid(),'CREATE_EXERCISE','Create exercise','ACTION','/api/v1/exercises','POST',true,'SYSTEM','SYSTEM','2023-11-24 13:26:47.735617+07','2023-11-24 13:26:47.735617+07'),
  (gen_random_uuid(),'EDIT_EXERCISE','Edit exercise','ACTION','/api/v1/exercises','PUT',true,'SYSTEM','SYSTEM','2023-11-24 13:27:36.96369+07','2023-11-24 13:27:36.96369+07'),
  (gen_random_uuid(),'DELETE_EXERCISE','Delete exercise','ACTION','/api/v1/exercises','DELETE',true,'SYSTEM','SYSTEM','2023-11-24 13:28:05.440706+07','2023-11-24 13:28:05.440706+07'),
  (gen_random_uuid(),'MANAGE_USER','Manage user','MENU',NULL,NULL,true,'SYSTEM','SYSTEM','2023-11-24 13:28:44.056613+07','2023-11-24 13:28:44.056613+07'),
  (gen_random_uuid(),'MANAGE_EXCERCISE','Manage exercise','MENU',NULL,NULL,true,'SYSTEM','SYSTEM','2023-11-24 13:29:06.282712+07','2023-11-24 13:29:06.282712+07');

-- ## Seeding RolePermission
INSERT INTO role_permission (role_name,permission_name,active,created_by,updated_by,created_at,updated_at) VALUES
  -- ## Admin
  ('ADMIN','CREATE_PLAN',true,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('ADMIN','EDIT_PLAN',true,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('ADMIN','CREATE_EXERCISE',true,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('ADMIN','EDIT_EXERCISE',true,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('ADMIN','DELETE_EXERCISE',true,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('ADMIN','MANAGE_USER',true,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('ADMIN','MANAGE_EXERCISE',true,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('ADMIN','DELETE_PLAN',true,'SYSTEM','SYSTEM','2023-11-30 13:48:23.863','2023-11-30 13:48:23.863'),

   -- ## User
  ('USER','CREATE_PLAN',false,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('USER','EDIT_PLAN',false,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('USER','CREATE_EXERCISE',false,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('USER','EDIT_EXERCISE',false,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('USER','DELETE_EXERCISE',false,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('USER','MANAGE_USER',false,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('USER','MANAGE_EXERCISE',false,'SYSTEM','SYSTEM','2023-11-24 18:09:48.367','2023-11-24 18:09:48.367'),
  ('USER','DELETE_PLAN',false,'SYSTEM','SYSTEM','2023-11-30 13:48:52.667','2023-11-30 13:48:52.667');


-- ## Seeding User
INSERT INTO users (id, first_name, last_name, email, pwd, verified, verif_code, verif_code_exp_at, created_by, updated_by, created_at, updated_at) VALUES
  ('a87117c1-6e6a-4785-b9e0-e1a906ad1470', 'ADMIN', '', 'admin@datvu.tech', '$2a$10$to5b5nyzVeHVwSX6gGAlS.rFsGWU2jN1Y6qLVFWMD0gvDcvHhHWtG', true, null, null, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO user_role
  (id, user_id, role_name, active, created_by, updated_by, created_at, updated_at) VALUES
  (nextval('user_role_id_seq'::regclass), 'a87117c1-6e6a-4785-b9e0-e1a906ad1470', 'USER', true, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (nextval('user_role_id_seq'::regclass), 'a87117c1-6e6a-4785-b9e0-e1a906ad1470', 'ADMIN', true, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);