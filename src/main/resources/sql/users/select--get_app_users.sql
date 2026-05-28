select
	au.user_uuid,
	pi.img_uuid,
	coalesce(aup.display_name, au.username)
		as display_name,
	au.email,
	r.role_type,
	au.isactive,
	au.created_dtm
from wc01.app_user au
left join wc01.app_user_profile aup
	on au.id = aup.app_user_id
left join wc01.role r
	on au.role = r.id
left join wc01.profile_image pi
    on aup.id = pi.app_user_profile_id
WHERE au.username ILIKE ? OR aup.display_name ILIKE ?
ORDER BY username
LIMIT ? OFFSET ?;