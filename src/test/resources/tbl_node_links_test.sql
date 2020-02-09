--create adjacency links
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_location','address', 1, 'tbl_address', 'id', 1, 'location-address');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_location','address', 2, 'tbl_address', 'id', 2, 'location-address');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_location','address', 3, 'tbl_address', 'id', 3, 'location-address');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_location','address', 4, 'tbl_address', 'id', 4, 'location-address');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_location','address', 5, 'tbl_address', 'id', 5, 'location-address');

insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_user','user_account', 1, 'tbl_account', 'id', 1, 'user-account');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_user','user_account', 2, 'tbl_account', 'id', 2, 'user-account');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_user','user_account', 3, 'tbl_account', 'id', 3, 'user-account');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_user','user_account', 4, 'tbl_account', 'id', 4, 'user-account');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_user','user_account', 5, 'tbl_account', 'id', 5, 'user-account');

insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team','captain', 1, 'tbl_user', 'id', 1, 'team-captain');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team','captain', 2, 'tbl_user', 'id', 5, 'team-captain');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team','captain', 3, 'tbl_user', 'id', 3, 'team-captain');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team','captain', 4, 'tbl_user', 'id', 2, 'team-captain');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team','captain', 5, 'tbl_user', 'id', 4, 'team-captain');

insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team','location', 1, 'tbl_location', 'id', 1, 'team-location');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team','location', 2, 'tbl_location', 'id', 2, 'team-location');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team','location', 3, 'tbl_location', 'id', 3, 'team-location');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team','location', 4, 'tbl_location', 'id', 4, 'team-location');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team','location', 5, 'tbl_location', 'id', 5, 'team-location');

insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 1, 'tbl_team', 'id', 1, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 2, 'tbl_team', 'id', 1, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 3, 'tbl_team', 'id', 1, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 4, 'tbl_team', 'id', 2, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 5, 'tbl_team', 'id', 2, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 6, 'tbl_team', 'id', 2, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 7, 'tbl_team', 'id', 3, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 8, 'tbl_team', 'id', 3, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 9, 'tbl_team', 'id', 3, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 10, 'tbl_team', 'id', 4, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 11, 'tbl_team', 'id', 4, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 12, 'tbl_team', 'id', 4, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 13, 'tbl_team', 'id', 5, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 14, 'tbl_team', 'id', 5, 'player-team');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','team', 15, 'tbl_team', 'id', 5, 'player-team');

insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 1, 'tbl_user', 'id', 1, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 2, 'tbl_user', 'id', 2, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 3, 'tbl_user', 'id', 3, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 4, 'tbl_user', 'id', 4, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 5, 'tbl_user', 'id', 5, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 6, 'tbl_user', 'id', 1, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 7, 'tbl_user', 'id', 2, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 8, 'tbl_user', 'id', 3, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 9, 'tbl_user', 'id', 4, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 10, 'tbl_user', 'id', 5, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 11, 'tbl_user', 'id', 1, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 12, 'tbl_user', 'id', 2, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 13, 'tbl_user', 'id', 3, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 14, 'tbl_user', 'id', 4, 'player-user');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_team_members','member', 15, 'tbl_user', 'id', 5, 'player-user');

insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','followee', 1, 'tbl_user', 'id', 1, 'user-followee');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','followee', 2, 'tbl_user', 'id', 1, 'user-followee');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','followee', 3, 'tbl_user', 'id', 1, 'user-followee');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','followee', 4, 'tbl_user', 'id', 1, 'user-followee');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','followee', 5, 'tbl_user', 'id', 2, 'user-followee');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','followee', 6, 'tbl_user', 'id', 2, 'user-followee');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','followee', 7, 'tbl_user', 'id', 2, 'user-followee');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','followee', 8, 'tbl_user', 'id', 3, 'user-followee');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','followee', 9, 'tbl_user', 'id', 3, 'user-followee');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','followee', 10, 'tbl_user', 'id', 4, 'user-followee');

insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','follower', 1, 'tbl_user', 'id', 2, 'user-follower');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','follower', 2, 'tbl_user', 'id', 3, 'user-follower');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','follower', 3, 'tbl_user', 'id', 4, 'user-follower');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','follower', 4, 'tbl_user', 'id', 5, 'user-follower');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','follower', 5, 'tbl_user', 'id', 3, 'user-follower');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','follower', 6, 'tbl_user', 'id', 4, 'user-follower');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','follower', 7, 'tbl_user', 'id', 5, 'user-follower');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','follower', 8, 'tbl_user', 'id', 4, 'user-follower');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','follower', 9, 'tbl_user', 'id', 5, 'user-follower');
insert into tbl_node_links ( table_from, column_from, value_from, table_to, column_to, value_to, link_name ) values ('tbl_users_network','follower', 10, 'tbl_user', 'id', 5, 'user-follower');
