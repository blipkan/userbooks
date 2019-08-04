package com.ex.books.view;

import com.ex.books.utils.JSFUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Named
@SessionScoped
@Slf4j
public class LangView {

    private Locale locale;
    @Getter
    private Map<String, String> languages;

    @PostConstruct
    public void init() {
        locale = JSFUtils.getApplication().getDefaultLocale();
        languages = new HashMap<>();
        languages.put("Русский", "ru");
        languages.put("English", "en");
    }

    public Locale getLocale() {
        return locale;
    }

    public String getLang() {
        return locale.getLanguage();
    }

    public void setLang(String language) {
        locale = new Locale(language);
        JSFUtils.setLocale(locale);
    }

}
