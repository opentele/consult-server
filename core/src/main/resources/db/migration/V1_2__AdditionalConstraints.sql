ALTER TABLE users
    ADD CONSTRAINT email_or_mobile_present CHECK (
                (CASE WHEN email IS NULL THEN 0 ELSE 1 END) +
                (CASE WHEN mobile IS NULL THEN 0 ELSE 1 END) > 0);
