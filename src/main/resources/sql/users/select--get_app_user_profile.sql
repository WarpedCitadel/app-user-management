SELECT
    au.user_uuid,
    COALESCE(aup.display_name, au.username)
        AS display_name,
    au.username,
    aup.user_bio,
    pi.img_uuid,
    pi.file_name
FROM wc01.app_user au
LEFT JOIN wc01.app_user_profile aup
    ON au.id = aup.app_user_id
LEFT JOIN wc01.profile_image pi
	ON aup.id = pi.app_user_profile_id
WHERE au.user_uuid = ?::uuid;