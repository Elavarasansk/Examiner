ALTER TABLE "vexamine_test_summary" DROP COLUMN "user_auth_id";
ALTER TABLE "vexamine_test_summary" DROP COLUMN "question_taken";
ALTER TABLE "vexamine_test_summary" DROP COLUMN "test_date";
ALTER TABLE "vexamine_test_summary" DROP COLUMN "time_taken";
ALTER TABLE vexamine_test_summary ADD COLUMN time_taken BIGINT;