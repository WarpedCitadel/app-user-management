SELECT
    au.uuid
    au.username,
    aup.user_bio
FROM wc01.app_user au
INNER JOIN wc01.app_user_profile aup
    ON au.id = aup.app_user_id
WHERE au.uuid = ?;

