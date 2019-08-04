package com.ex.books.utils;

import org.apache.commons.lang3.StringUtils;

import javax.faces.application.FacesMessage;
import java.text.MessageFormat;
import java.util.MissingResourceException;

public class Msg {
    public static final String BUNDLE_NAME = "text";

    public static String format(String key, Object[] params) {
        return MessageFormat.format(text(key), params);
    }

    public static String text(String key) {
        if (StringUtils.isBlank(key)) {
            return StringUtils.EMPTY;
        }
        try {
            return JSFUtils.getResourceBundle(BUNDLE_NAME).getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static void addPlain(FacesMessage.Severity severity, String summary, String detail) {
        add(new FacesMessage(severity, summary, detail));
    }

    public static void add(FacesMessage fm) {
        JSFUtils.getFacesContext().addMessage(null, fm);
    }

    public static void add(FacesMessage.Severity severity, String keySummary, String keyDetail, Object[]... params) {
        add(create(severity, keySummary, keyDetail, params));
    }

    public static FacesMessage create(FacesMessage.Severity severity, String keySummary, String keyDetail, Object[]... params) {
        return new FacesMessage(severity,
                (params.length >= 1) ? format(keySummary, params[0]) : text(keySummary),
                (params.length >= 2) ? format(keyDetail, params[1]) : text(keyDetail)
        );
    }


}
