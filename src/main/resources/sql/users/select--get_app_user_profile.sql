SELECT
    au.user_uuid,
    aup.display_name,
    aup.user_bio,
    pi.img_uuid
FROM wc01.app_user au
left JOIN wc01.app_user_profile aup
    ON au.id = aup.app_user_id
left join wc01.profile_image pi
	on aup.id = pi.app_user_profile_id
WHERE au.user_uuid = ?::uuid;

