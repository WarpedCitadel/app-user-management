SELECT
	au.user_uuid,
	pi.img_uuid,
	COALESCE(aup.display_name, au.username)
		AS display_name,
	au.email,
	r.role_type,
	au.isactive,
	au.created_dtm
FROM wc01.app_user au
LEFT JOIN wc01.app_user_profile aup
	ON au.id = aup.app_user_id
LEFT JOIN wc01.role r
	ON au.role = r.id
LEFT JOIN wc01.profile_image pi
    ON aup.id = pi.app_user_profile_id
WHERE au.username ILIKE ? OR aup.display_name ILIKE ?
ORDER BY username
LIMIT ? OFFSET ?;