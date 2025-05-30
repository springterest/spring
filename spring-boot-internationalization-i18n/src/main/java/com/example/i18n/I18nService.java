package com.example.i18n;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * With each HTTP Servlet Request, a language (locale) is provided within
 * the Accept-Language Header.
 * This class returns the related message in the provided request locale.
 * The message files are located in resources/i18n
 *
 * <p>Configured with the following properties:</p>
 * <ul>
 *   <li><code>spring.messages.logging-language</code>: The configured logging language which is set within the application-i18n.yml file</li>
 * </ul>
 *
 * <p>Dependencies:</p>
 * <ul>
 *   <li>{@link MessageSource}: Spring's interface for resolving messages, with support for the parameterization and internationalization of such messages.</li>
 *   <li>{@link HttpServletRequest}: Represents an HTTP request and provides methods to access request parameters, headers, and attributes in a web application.</li>
 * </ul>
 *
 * <p>Annotations:</p>
 * <ul>
 *   <li>{@code @Service}: Marks this class as a Spring service.</li>
 *   <li>{@code @RequiredArgsConstructor}: Generates a constructor with required arguments.</li>
 * </ul>
 *
 * @since 0.0.1
 */
@Service
@RequiredArgsConstructor
class I18nService {

    private final MessageSource messageSource;

    private final HttpServletRequest request;

    @Setter
    @Value("${spring.messages.logging-language}")
    private String loggingLanguage;

    /**
     * Retrieves a localized message for logging purposes based on the provided code.
     *
     * @param code the message code to lookup in the message source
     * @return the localized log message
     */
    public String getLogMessage(String code) {
        return messageSource.getMessage(code, null, Locale.of(loggingLanguage));
    }

    /**
     * Retrieves a localized message based on the provided code and arguments,
     * using the current locale from the HTTP request.
     *
     * @param code the message code to lookup in the message source
     * @param args optional arguments for the message (could be null)
     * @return the localized message
     */
    public String getMessage(String code, @Nullable String... args) {
        return messageSource.getMessage(code, args, request.getLocale());
    }
}