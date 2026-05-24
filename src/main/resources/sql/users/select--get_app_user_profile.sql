SELECT
    au.uuid,
    aup.display_name,
    aup.user_bio,
    pi.uuid
FROM wc01.app_user au
INNER JOIN wc01.app_user_profile aup
    ON au.id = aup.app_user_id
inner join wc01.profile_image pi
	on aup.id = pi.user_profile_id
WHERE au.uuid = ?::uuid;

