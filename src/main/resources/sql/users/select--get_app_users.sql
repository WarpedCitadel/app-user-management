SELECT username, user_uuid
FROM wc01.app_user
where username ILIKE ?
ORDER BY username
LIMIT ? OFFSET ?;