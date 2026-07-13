SELECT DISTINCT
	gp.game_profile_uuid,
    gp.title,
    gi.iscover,
    gi.file_name,
	gp.short_desc,
	g.genre_type
FROM wc01.app_user au
LEFT JOIN wc01.game_profile gp
	ON au.id = gp.app_user_id
LEFT JOIN wc01.game_image gi
	ON gp.id = gi.game_profile_id
LEFT JOIN wc01.game_file af
	ON gp.id = af.game_profile_id
LEFT JOIN wc01.game_genre g
	ON gp.game_genre_id = g.id
WHERE gi.iscover = true
AND af.status_type_id = 4
AND au.user_uuid = ?::uuid;