package com.krasova.service;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.Address;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by osamo on 3/26/2017.
 */
public class MailServiceTest {


    @InjectMocks
    private MailService tested;
    @Mock
    private JavaMailSenderImpl mailSender;

    @Before
    public void initializeMocks() {
        initMocks(this);
        when(mailSender.createMimeMessage())
                .thenCallRealMethod();
    }
//

    @Test
    public void notify_happyPath() {
        //setup
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        List<String> fileNames = Arrays.asList(RandomStringUtils.random(10), RandomStringUtils.random(10));
        mailMessage.setTo("olga.v.krasova@gmail.com");
        mailMessage.setFrom("olga.v.krasova@gmail.com");
        mailMessage.setSubject("New files");
        mailMessage.setText(fileNames.toString());
        //execute
        tested.notify(fileNames);
        //verify
        verify(mailSender).send(argThat(new MimeMessagePreparatorMatcher(mailMessage)));

    }

    private class MimeMessagePreparatorMatcher extends ArgumentMatcher<MimeMessagePreparator> {
        private final SimpleMailMessage mailMessage;

        public MimeMessagePreparatorMatcher(SimpleMailMessage mailMessage) {
            this.mailMessage = mailMessage;
        }

        @Override
        public boolean matches(Object argument) {
            if (argument instanceof MimeMessagePreparator) {
                try {
                    MimeMessagePreparator preparator = (MimeMessagePreparator) argument;
                    MimeMessage message = mailSender.createMimeMessage();
                    preparator.prepare(message);

                    String[] expectedTo = mailMessage.getTo();
                    Address[] actualTo = message.getAllRecipients();

                    if (!addressesAreEqual(expectedTo,
                            actualTo)) {
                        return false;
                    }

                    String[] expectedFrom;

                    {
                        String from = mailMessage.getFrom();
                        expectedFrom = from == null ? new String[0]
                                : new String[]{from};
                    }

                    Address[] actualFrom = message.getFrom();

                    if (!addressesAreEqual(expectedFrom,
                            actualFrom)) {
                        return false;
                    }

                    return true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }

            return false;
        }

        private boolean addressesAreEqual(String[] expectedAddresses,
                                          Address[] actualAddresses) {
            boolean expectedIsEmpty = expectedAddresses == null ||
                    expectedAddresses.length == 0;
            boolean actualIsEmpty = actualAddresses == null ||
                    actualAddresses.length == 0;

            if (expectedIsEmpty != actualIsEmpty) {
                return false;
            }

            if (expectedAddresses != null &&
                    actualAddresses != null) {
                if (expectedAddresses.length != actualAddresses.length) {
                    return false;
                }

                for (int i = 0; i < expectedAddresses.length; i++) {
                    String expected = expectedAddresses[i];
                    String actual = actualAddresses[i].toString();

                    if (!Objects.equals(expected,
                            actual)) {
                        return false;
                    }
                }
            }

            return true;
        }

    }


}