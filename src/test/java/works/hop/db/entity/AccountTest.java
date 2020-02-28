package works.hop.db.entity;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AccountTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidateAccountWithMissingId() {
        Account account = new Account(null, "administrator", "yes!yes!yes", "some@email.com", Account.Type.user, new Date());
        Map<String, String> results = account.validate(validator);
        assertEquals("Expecting 1", 1, results.size());
        assertEquals("Id value should not be null", results.get("id"));
    }

    @Test
    public void testValidateAccountWithMissingUsername() {
        Account account = new Account(10L, null, "yes!yes!yes", "some@email.com", Account.Type.admin, new Date());
        Map<String, String> results = account.validate(validator);
        assertEquals("Expecting 1", 1, results.size());
        assertEquals("Username should not be null", results.get("username"));
    }

    @Test
    public void testValidateAccountWithInvalidEmail() {
        Account account = new Account(10L, "administrator", "yes!yes!yes", "email", Account.Type.admin, new Date());
        Map<String, String> results = account.validate(validator);
        assertEquals("Expecting 1", 1, results.size());
        assertEquals("Email value should be a valid email address", results.get("emailAddress"));
    }

    @Test
    public void testValidateAccountWithInvalidPassword() {
        Account account = new Account(10L, "administrator", "yes", "some@email.com", Account.Type.admin, new Date());
        Map<String, String> results = account.validate(validator);
        assertEquals("Expecting 1", 1, results.size());
        assertEquals("Password should be between 8 and 14 characters", results.get("password"));
    }

    @Test
    public void testValidateAccountWithMissingAccountType() {
        Account account = new Account(10L, "administrator", "yes!yes!yes", "some@email.com", null, new Date());
        Map<String, String> results = account.validate(validator);
        assertEquals("Expecting 1", 1, results.size());
        assertEquals("Account type should not be null", results.get("type"));
    }

    @Test
    public void testValidateAccountWithMissingCreatedDate() {
        Account account = new Account(10L, "administrator", "yes!yes!yes", "some@email.com", Account.Type.user, null);
        Map<String, String> results = account.validate(validator);
        assertEquals("Expecting 1", 1, results.size());
        assertEquals("Date created should not be null", results.get("dateCreated"));
    }
}
