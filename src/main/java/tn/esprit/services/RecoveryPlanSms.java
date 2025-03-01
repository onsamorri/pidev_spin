package tn.esprit.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class RecoveryPlanSms {
    // Twilio credentials (replace with your actual credentials)
    private static final String ACCOUNT_SID = "";
    private static final String AUTH_TOKEN = "";
    private static final String TWILIO_PHONE_NUMBER = "";

    // Initialize Twilio API
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    /**
     * Sends an SMS notification to the athlete.
     *
     * @param recipientPhoneNumber The phone number of the recipient 
     * @param messageBody The message to send.
     */
    public static void sendSms(String recipientPhoneNumber, String messageBody) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(recipientPhoneNumber), // Athlete's phone number
                    new PhoneNumber(TWILIO_PHONE_NUMBER),  // Your Twilio phone number
                    messageBody // Message content
            ).create();

            System.out.println("SMS sent successfully! Message SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Error sending SMS: " + e.getMessage());
        }
    }
}
