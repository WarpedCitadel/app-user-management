SELECT af.uuid, gp.title, gi.uuid,
	gp.description, g.genre_type
from wc01.app_file af
inner join wc01.app_user au on au.id = af.app_user_id
inner join wc01.game_profile gp on af.id = gp.app_file_id
inner join wc01.game_image gi on gi.game_profile_id = gp.id
inner join wc01.genre g on gp.genre_id = g.id
where af.status_type_id = 4 and gi.iscover and au.uuid = ?::uuid;