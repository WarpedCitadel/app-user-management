SELECT af.file_uuid,
       gp.title,
       gi.img_uuid,
	   gp.short_desc,
	   g.genre_type
from wc01.app_user               au
     left join wc01.app_file     af on au.id = af.app_user_id
     left join wc01.game_profile gp on af.id = gp.app_file_id
     left join wc01.game_image   gi on gp.id = gi.game_profile_id
                                      and gi.iscover = true
                                      and af.status_type_id = 4
     left join wc01.genre         g on gp.genre_id = g.id
where au.user_uuid = ?::uuid;