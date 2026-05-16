SELECT
    au.uuid,
    aup.profile_image,
    aup.display_name,
    aup.user_bio
FROM wc01.app_user au
INNER JOIN wc01.app_user_profile aup
    ON au.id = aup.app_user_id
WHERE au.uuid = ?::uuid;

