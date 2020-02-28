package works.hop.db.record;

import works.hop.db.api.DbConnector;
import works.hop.db.api.PgDbConnector;
import works.hop.db.api.Record;
import works.hop.db.entity.TeamMember;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TeamMemberRecord extends Record<TeamMember> {

    //you (member) requesting (requested) to join them (team)
    public final static String requestToJoin = "insert into tbl_team_members (team, member, member_status) values (?, ?, ?::member_status);";
    //them (team) updating your (member) join request (accepted/rejected/blocked)
    public final static String updateRequest = "update tbl_team_members set member_status=?::member_status where team=? and member=?";
    //select a team member
    public final static String selectRecord = "select team, member, member_status, date_joined from tbl_team_members where team=? and member=?";
    //you (member) quiting (delete) the team
    public final static String deleteRecord = "delete from tbl_team_members where team=? and member=?";
    //them (team) retrieving their members
    public final static String fetchMembers = "select team, member, member_status, date_joined from tbl_team_members where team=? offset ? limit ?";
    //you (member) retrieving teams you belong to
    public final static String fetchTeams = "select team, member, member_status, date_joined from tbl_team_members where member=? and member_status = ?::'accepted' offset ? limit ?";
    //clear table records
    public final static String clearRecords = "truncate table tbl_team_members";

    public TeamMemberRecord() {
        this(PgDbConnector.instance());
    }

    public TeamMemberRecord(DbConnector dbConnector) {
        super(TeamMember.class, dbConnector);
    }

    @Override
    public String tableName() {
        return "tbl_team_members";
    }

    @Override
    public String[] dropTable() {
        return new String[]{
                "drop table if exists tbl_team_members;",
                "drop type if exists member_status;"
        };
    }

    @Override
    public String[] createTable() {
        return new String[]{
                "create type member_status AS ENUM ('requested', 'accepted', 'rejected', 'blocked', 'suspended', 'revoked');",
                "create table if not exists tbl_team_members\n" +
                        "(\n" +
                        "    team          int                not null,\n" +
                        "    member        int                not null,\n" +
                        "    member_status member_status not null default 'requested',\n" +
                        "    date_joined   timestamp                   default current_timestamp,\n" +
                        "    foreign key (team) references tbl_team (id),\n" +
                        "    foreign key (member) references tbl_user (id)\n" +
                        ");"
        };
    }

    @Override
    public void prepareCheck(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        //currently not in use
    }

    @Override
    public Long generatedKey(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public void prepareInsert(PreparedStatement pst, TeamMember record) throws SQLException {
        pst.setLong(1, record.team);
        pst.setLong(2, record.member);
        pst.setString(3, record.type.toString());
    }

    @Override
    public TeamMember extractRecord(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new TeamMember(
                    rs.getLong("team"),
                    rs.getLong("member"),
                    TeamMember.MemberStatus.valueOf(rs.getString("member_status")),
                    new Date(rs.getTimestamp("date_joined").getTime())
            );
        }
        return null;
    }

    @Override
    public void prepareSelect(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setLong(1, Long.valueOf(params.get(1).toString()));
        pst.setLong(2, Long.valueOf(params.get(2).toString()));
    }

    @Override
    public void prepareSelectList(PreparedStatement pst, Map<Integer, Object> params, Integer offset, Integer limit) throws SQLException {
        pst.setLong(1, Long.valueOf(params.get(1).toString())); //team/member
        pst.setInt(2, offset);
        pst.setInt(3, limit);
    }

    @Override
    public List<TeamMember> extractRecords(ResultSet rs) throws SQLException {
        List<TeamMember> records = new LinkedList<>();
        while (rs.next()) {
            records.add(new TeamMember(
                    rs.getLong("team"),
                    rs.getLong("member"),
                    TeamMember.MemberStatus.valueOf(rs.getString("member_status")),
                    new Date(rs.getTimestamp("date_joined").getTime())));
        }
        return records;
    }

    @Override
    public void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setString(1, params.get(1).toString());
        pst.setLong(2, Long.valueOf(params.get(2).toString()));
        pst.setLong(3, Long.valueOf(params.get(3).toString()));
    }

    @Override
    public void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setLong(1, Long.valueOf(params.get(1).toString()));
        pst.setLong(2, Long.valueOf(params.get(2).toString()));
    }
}
