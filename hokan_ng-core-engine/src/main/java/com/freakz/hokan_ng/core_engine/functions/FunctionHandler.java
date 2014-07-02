package com.freakz.hokan_ng.core_engine.functions;

/**
 * Created by pairio on 23.6.2014.
 *
 * @author Petri Airio (petri.airio@polar.com)
 */
public interface FunctionHandler {

  String handleFunctions(String inputLine);

  boolean hasFunction(String line);

  java.util.List<HokanFunction> parseFunctions(String line);

}
