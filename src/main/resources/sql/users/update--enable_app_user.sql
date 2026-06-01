UPDATE wc01.app_user
SET isactive = TRUE::BOOLEAN
WHERE user_uuid = ?::UUID;