package com.freakz.hokan_ng.common.service;


import com.freakz.hokan_ng.common.entity.Property;
import com.freakz.hokan_ng.common.entity.PropertyName;
import com.freakz.hokan_ng.common.exception.HokanException;
import com.google.api.GoogleAPI;
import com.google.api.GoogleAPIException;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Petri Airio (petri.j.airio@gmail.com) on 29.4.2015.
 *
 */
@Service
@Slf4j
public class GoogleTranslatorServiceImpl implements GoogleTranslatorService {

  @Autowired
  private PropertyService propertyService;

  @Override
  public String getTranslation(Language from, Language to, String... text) {
    GoogleAPI.setHttpReferrer("https://github.com/petria/hokan_ng");

    Property apikey = null;
    try {
      apikey = propertyService.findProperty(PropertyName.PROP_SYS_GOOGLE_API_KEY);
    } catch (HokanException e) {
      log.error("property", e);
    }
    if (apikey == null) {
      log.error("GoogleAPI key missing");
      return "GoogleAPI key missing";
    }
    GoogleAPI.setKey(apikey.getValue());

    StringBuilder sb = new StringBuilder();
    try {
      for (String textLine : text) {
        String translatedText = Translate.DEFAULT.execute(textLine, from, to);
        sb.append(translatedText);
      }
    } catch (GoogleAPIException e) {
      log.error("GoogleAPI", e);
      return e.getMessage();
    }
    return sb.toString();
  }
}
