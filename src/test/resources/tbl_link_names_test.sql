--create link names
insert into tbl_link_names (name, description ) values ('user-account', 'links a user to their account');
insert into tbl_link_names (name, description ) values ('user-follower', 'links a user to another user that is following them');
insert into tbl_link_names (name, description ) values ('user-followee', 'links a user to another user they are following');
insert into tbl_link_names (name, description ) values ('location-address', 'links a location to its address');
insert into tbl_link_names (name, description ) values ('team-captain', 'links a team to its manager, who is a user');
insert into tbl_link_names (name, description ) values ('team-location', 'links a team to its location');
insert into tbl_link_names (name, description ) values ('player-team', 'links a team member to an existing team');
insert into tbl_link_names (name, description ) values ('player-user', 'links a team member to an existing user');
insert into tbl_link_names (name, description ) values ('reserve-player', 'links a team''s backup member to an existing user');

