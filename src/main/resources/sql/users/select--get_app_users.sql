SELECT
	au.user_uuid,
	f.img_uuid,
	f.file_name,
	au.username,
	f.display_name,
	au.email,
	f.role_type,
	au.isactive,
	au.created_dtm
FROM wc01.app_user au
    JOIN wc01.fnc_search_users_select(    ?,
                                          ?,
                                          ?
    ) f
    ON au.id = f.app_user_id
WHERE 1=1
ORDER BY f.display_name ASC
LIMIT COALESCE(?, 20) OFFSET COALESCE (?, 0);