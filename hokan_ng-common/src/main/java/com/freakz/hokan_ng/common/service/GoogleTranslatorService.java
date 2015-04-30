package com.freakz.hokan_ng.common.service;

import com.google.api.translate.Language;

/**
 * Created by Petri Airio on 29.4.2015.
 *
 */
public interface GoogleTranslatorService {

  String getTranslation(Language from, Language to, String... text);

}
