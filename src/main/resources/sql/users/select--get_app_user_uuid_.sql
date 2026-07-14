SELECT
    user_uuid
FROM wc01.app_user
WHERE LOWER(username) = LOWER(?);