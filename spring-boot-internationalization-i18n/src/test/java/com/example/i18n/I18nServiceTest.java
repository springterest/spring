package com.example.i18n;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class I18nServiceTest {

    @Mock
    private MessageSource messageSource;

    @Autowired
    private I18nService i18nService;

    @Value("${spring.messages.logging-language}")
    private String loggingLanguage;

    @Test
    void testGetLogMessage() {
        // Arrange
        String code = "i18n.test.log.message";
        String expectedMessage = "This is a test log message";
        when(messageSource.getMessage(anyString(), any(), any(Locale.class)))
                .thenReturn(expectedMessage);

        // Act
        i18nService.setLoggingLanguage(loggingLanguage);
        String actualMessage = i18nService.getLogMessage(code);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetMessage_WithArgs() {
        // Arrange
        String code = "i18n.test.message.args";
        String[] args = {"John"};
        String expectedMessage = "This is a test message with arguments: " + args[0];
        when(messageSource.getMessage(code, args, Locale.US)).thenReturn(expectedMessage);

        // Act
        String actualMessage = i18nService.getMessage(code, args);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetMessage_WithoutArgs() {
        // Arrange
        String code = "i18n.test.message.no.args";
        String expectedMessage = "This is a test message without arguments";
        when(messageSource.getMessage(code, null, Locale.US)).thenReturn(expectedMessage);

        // Act
        String actualMessage = i18nService.getMessage(code);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }
}