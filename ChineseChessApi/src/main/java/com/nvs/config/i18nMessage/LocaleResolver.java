package com.nvs.config.i18nMessage;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

  List<Locale> LOCALES = List.of(Locale.of("en"), Locale.of("vi"));

  @NotNull
  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    String languageHeader = request.getHeader("Accept-Language");
    return !StringUtils.hasLength(languageHeader)
        ? Locale.US
        : Locale.lookup(Locale.LanguageRange.parse(languageHeader), LOCALES);
  }

}
