package works.hop.db.record;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import works.hop.db.entity.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AccountRecordMockTest {

    private AccountRecord records = new AccountRecord();

    @Test
    public void tableName() {
        assertEquals(records.tableName(), "tbl_account");
    }

    @Test
    public void dropTable() {
        assertNotNull(records.dropTable());
        assertTrue(records.dropTable().length > 0);
    }

    @Test
    public void createTable() {
        assertNotNull(records.createTable());
        assertTrue(records.createTable().length > 0);
    }

    @Test
    public void generatedKey() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("id")).thenReturn(1L);
        Long id = records.generatedKey(rs);
        assertEquals(id.longValue(), 1);
    }

    @Test
    public void prepareInsert() throws SQLException {
        class InsertValues {
            String username;
            String password;
            String emailAddr;
        }

        InsertValues insert = new InsertValues();

        PreparedStatement pst = mock(PreparedStatement.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Integer index = invocation.getArgument(0, Integer.class);
                String value = invocation.getArgument(1, String.class);
                switch (index){
                    case 1:
                        insert.username = value;
                        break;
                    case 2:
                        insert.password = value;
                        break;
                    case 3:
                        insert.emailAddr = value;
                        break;
                    default:
                        throw new SQLException("Unexpected index '" + index + "' with value " + value);
                }
                return null;
            }
        }).when(pst).setString(anyInt(), anyString());
        records.prepareInsert(pst, new Account(1L, "steve", "mikes", "steve@mikes.com", Account.Type.user, null));
        assertEquals(insert.username, "steve");
        assertEquals(insert.password, "mikes");
        assertEquals(insert.emailAddr, "steve@mikes.com");
    }

    @Test
    public void extractRecord() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("user_name")).thenReturn("steve");
        when(rs.getString("pass_code")).thenReturn("mikes");
        when(rs.getString("email_address")).thenReturn("steve@mikes.com");
        when(rs.getString("account_type")).thenReturn("user");

        Timestamp ts = new Timestamp(new Date().getTime());
        when(rs.getTimestamp("date_created")).thenReturn(ts);
        Account acc = records.extractRecord(rs);
        assertEquals(acc.id.longValue(), 1);
        assertEquals(acc.username, "steve");
        assertEquals(acc.password, "mikes");
        assertEquals(acc.emailAddress, "steve@mikes.com");
        assertEquals(acc.type, Account.Type.user);
        assertEquals(acc.dateCreated, ts);
    }

    @Test
    public void prepareSelect() throws SQLException {
        class SelectValues {
            String username;
        }

        SelectValues select = new SelectValues();

        PreparedStatement pst = mock(PreparedStatement.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Integer index = invocation.getArgument(0, Integer.class);
                String value = invocation.getArgument(1, String.class);
                switch (index){
                    case 1:
                        select.username = value;
                        break;
                    default:
                        throw new SQLException("Unexpected index '" + index + "' with value " + value);
                }
                return null;
            }
        }).when(pst).setString(anyInt(), anyString());
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "mikes");
        records.prepareSelect(pst, params);
        assertEquals(select.username, "mikes");
    }

    @Test
    public void prepareSelectList() throws SQLException {
        class SelectValues {
            Integer offset;
            Integer limit;
        }

        SelectValues select = new SelectValues();

        PreparedStatement pst = mock(PreparedStatement.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Integer index = invocation.getArgument(0, Integer.class);
                Integer value = invocation.getArgument(1, Integer.class);
                switch (index){
                    case 1:
                        select.offset = value;
                        break;
                    case 2:
                        select.limit = value;
                        break;
                    default:
                        throw new SQLException("Unexpected index '" + index + "' with value " + value);
                }
                return null;
            }
        }).when(pst).setInt(anyInt(), anyInt());
        records.prepareSelectList(pst, Collections.emptyMap(), 0, 5);
        assertEquals(select.offset.intValue(), 0);
        assertEquals(select.limit.intValue(), 5);
    }

    @Test
    public void extractRecords() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenAnswer(new Answer<Boolean>() {
            int count = 1;
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                count = count - 1;
                return count >= 0;
            }
        });
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("user_name")).thenReturn("steve");
        when(rs.getString("pass_code")).thenReturn("mikes");
        when(rs.getString("email_address")).thenReturn("steve@mikes.com");
        when(rs.getString("account_type")).thenReturn("user");

        Timestamp ts = new Timestamp(new Date().getTime());
        when(rs.getTimestamp("date_created")).thenReturn(ts);
        List<Account> list = records.extractRecords(rs);
        assertEquals(list.size(), 1);
        Account acc = list.get(0);
        assertEquals(acc.id.longValue(), 1);
        assertEquals(acc.username, "steve");
        assertEquals(acc.password, "mikes");
        assertEquals(acc.emailAddress, "steve@mikes.com");
        assertEquals(acc.type, Account.Type.user);
        assertEquals(acc.dateCreated, ts);
    }

    @Test
    public void prepareUpdate() throws SQLException {
        class UpdateValues {
            String type;
            Long id;
        }

        UpdateValues update = new UpdateValues();

        PreparedStatement pst = mock(PreparedStatement.class);
        Stubber stub = doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Integer index = invocation.getArgument(0, Integer.class);
                switch (index){
                    case 1:
                        String type = invocation.getArgument(1, String.class);
                        update.type = type;
                        break;
                    case 2:
                        Long id = invocation.getArgument(1, Long.class);
                        update.id = id;
                        break;
                    default:
                        throw new SQLException("Unexpected index '" + index);
                }
                return null;
            }
        });
        stub.when(pst).setString(anyInt(), anyString());
        stub.when(pst).setLong(anyInt(), anyLong());

        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "user");
        params.put(2, 1);
        records.prepareUpdate(pst, params);
        assertEquals(update.type, "user");
        assertEquals(update.id.longValue(), 1);
    }

    @Test
    public void prepareDelete() throws SQLException {
        class DeleteValues {
            Long id;
        }

        DeleteValues delete = new DeleteValues();

        PreparedStatement pst = mock(PreparedStatement.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Integer index = invocation.getArgument(0, Integer.class);
                Long id = invocation.getArgument(1, Long.class);
                switch (index){
                    case 1:
                        delete.id = id;
                        break;
                    default:
                        throw new SQLException("Unexpected index '" + index + "' with value " + id);
                }
                return null;
            }
        }).when(pst).setLong(anyInt(), anyLong());
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 11);
        records.prepareDelete(pst, params);
        assertEquals(delete.id.longValue(), 11);
    }
}
