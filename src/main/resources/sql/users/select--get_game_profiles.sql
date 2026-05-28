SELECT af.file_uuid,
       gp.title,
       gi.img_uuid,
	   gp.short_desc,
	   g.genre_type
FROM wc01.app_user               au
     LEFT JOIN wc01.app_file     af ON au.id = af.app_user_id
     LEFT JOIN wc01.game_profile gp ON af.id = gp.app_file_id
     LEFT JOIN wc01.game_image   gi ON gp.id = gi.game_profile_id
                                      AND gi.iscover = true
                                      AND af.status_type_id = 4
     LEFT JOIN wc01.genre         g ON gp.genre_id = g.id
WHERE au.user_uuid = ?::uuid;