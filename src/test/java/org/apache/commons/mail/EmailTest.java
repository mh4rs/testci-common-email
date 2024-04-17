package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class EmailTest {
	
	private static final String[] TEST_EMAILS = { "ab@bc.com", "a.b@c.org", 
	"abcdefghijklmnopqrst@abcdefghijklmnopqrst.com.bd" };
	
	/* Concrete Email Class for testing */
	private EmailConcrete email;
	
	@Before
	public void setUpEmailTest() throws Exception {
		
		email = new EmailConcrete();
	}
	
	@After
	public void tearDownEmailTest() throws Exception {
		
	}
	
	/*
	 *  Test addBcc(String email) function
	 */
	@Test
	public void testAddBcc() throws Exception {
		
		
		email.addBcc(TEST_EMAILS);
		
		assertEquals(3, email.getBccAddresses().size());
		
	}

	
	/*
	 *  Test addCc(String email) function
	 */

	@Test
	public void testAddCcValidEmail() throws Exception {
	    // Test adding a valid email to CC. this should succeed and the CC list size should increase
	    email.addCc("test@example.com");
	    assertEquals("CC email addresses should include the added email", 1, email.getCcAddresses().size());
	}

	@Test(expected = EmailException.class)
	public void testAddCcInvalidEmail() throws Exception {
	    // Test adding an invalid email to CC. this should fail and throw an EmailException
	    email.addCc("invalidemail");
	}

	/*
	 *  Test addHeader(String name, String value) function
	 */

	@Test
	public void testAddHeaderValid() throws Exception {
		// Test adding a valid header. the header should be retrievable after
		email.addHeader("X-Test-Header", "TestValue");
		assertEquals("The header should match the value added", "TestValue", email.getHeaders().get("X-Test-Header"));
}

	@Test(expected = IllegalArgumentException.class)
	public void testAddHeaderInvalid() throws Exception {
		// Test adding an invalid header with empty name. this should throw an IllegalArgumentException
		email.addHeader("", "TestValue");
}

	/*
	 *  Test addReplyTo(String email, String name) function
	 */


	@Test
    public void testAddReplyToValid() throws Exception {
        email.addReplyTo("valid.email@example.com", "Valid Name");

        assertFalse("ReplyTo list should not be empty after adding a valid email", email.getReplyToAddresses().isEmpty());
        assertTrue("ReplyTo list should contain the added email", email.getReplyToAddresses().stream()
                .anyMatch(address -> address.getAddress().equals("valid.email@example.com") 
                && address.getPersonal().equals("Valid Name")));
    }

    @Test(expected = EmailException.class)
    public void testAddReplyToInvalidEmail() throws Exception {
        email.addReplyTo("invalid-email", "Invalid");
    }

	
	 /*
     * Test buildMimeMessage() function
     */
    
    @Test
    public void testBuildMimeMessageMinimal() throws Exception {
        email.setFrom("no-reply@example.com");
        email.addTo("user@example.com");
        email.setSubject("Subject");
        email.setMsg("Message body");

        email.buildMimeMessage();

        assertNotNull("MimeMessage should be built", email.getMimeMessage());
    }

    // Test handling of missing mandatory fields like 'from'
    @Test(expected = EmailException.class)
    public void testBuildMimeMessageMissingFrom() throws Exception {
        email.addTo("user@example.com");
        email.buildMimeMessage();
    }

    // Test correct handling of multiple recipient types
    @Test
    public void testBuildMimeMessageWithMultipleRecipientTypes() throws Exception {
        email.setFrom("no-reply@example.com");
        email.addTo("to@example.com");
        email.addCc("cc@example.com");
        email.addBcc("bcc@example.com");

        email.buildMimeMessage();

        // Assertions here would need to inspect the MimeMessage to verify recipients
        // This might require mocking javax.mail.internet.MimeMessage or using reflection
    }

    // Further tests here would focus on attachments, alternative content types, etc.


    /*
     * Test getHostName() function
     */
    @Test
    public void testGetHostNameNotSet() {
        assertNull("getHostName should return null when host name is not set", email.getHostName());
    }

    @Test
    public void testGetHostName() {
        final String hostName = "smtp.example.com";
        email.setHostName(hostName);
        assertEquals("getHostName should return the correct host name", hostName, email.getHostName());
    }

    /*
     * Test getMailSession() function
     */
    @Test(expected = EmailException.class)
    public void testGetMailSessionWithoutHostName() throws EmailException {
        email.getMailSession();
    }

    @Test
    public void testGetMailSessionWithHostName() throws EmailException {
        email.setHostName("smtp.example.com");
        assertNotNull("getMailSession should not return null with host name set", email.getMailSession());
    }
    

    /*
     * Test getSentDate() function
     */
    
    @Test
    public void testGetSentDate() {
        Date now = new Date();
        email.setSentDate(now);
        assertEquals("getSentDate should return the correct date", now, email.getSentDate());
    }

    /*
     * Test getSocketConnectionTimeout() function
     */
    @Test
    public void testGetSocketConnectionTimeout() {
        int timeout = 10000; // Example timeout in milliseconds
        email.setSocketConnectionTimeout(timeout);
        assertEquals("getSocketConnectionTimeout should return the correct timeout", timeout, email.getSocketConnectionTimeout());
    }

    /*
     * Test setFrom(String email) function
     */
    @Test
    public void testSetFrom() throws Exception {
        String fromEmail = "no-reply@example.com";
        email.setFrom(fromEmail);
        assertEquals("setFrom should set the correct from email", fromEmail, email.getFromAddress().getAddress());
    }

}
