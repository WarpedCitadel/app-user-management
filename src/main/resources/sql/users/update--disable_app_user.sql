UPDATE wc01.app_user
SET isactive = FALSE::BOOLEAN
WHERE user_uuid = ?::UUID;