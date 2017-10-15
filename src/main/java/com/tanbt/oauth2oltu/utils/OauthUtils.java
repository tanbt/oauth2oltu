package com.tanbt.oauth2oltu.utils;

import java.net.URI;

import org.springframework.web.servlet.view.RedirectView;

public class OauthUtils {

    public static RedirectView redirect(URI url) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url.toString());
        return redirectView;
    }

    public static RedirectView redirect(String url) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }
}
