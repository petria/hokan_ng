package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.EngineRequest;
import com.freakz.hokan_ng.common.rest.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.cheffo.jeplite.JEP;
import org.cheffo.jeplite.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_EXPRESSION;

/**
 * User: petria
 * Date: 11/27/13
 * Time: 3:20 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
public class CalcCmd extends Cmd {

  private final JEP jep;

  public CalcCmd() {
    super();
    setHelp("I am your pocket calculator.");

    jep = new JEP();
    jep.addStandardConstants();
    jep.addStandardFunctions();

    UnflaggedOption flg = new UnflaggedOption(ARG_EXPRESSION)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(EngineRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String result;
    String expression = results.getString(ARG_EXPRESSION);

    jep.parseExpression(expression);

    String error = jep.getErrorInfo();
    if (error != null) {
      result = "Expression parse error: " + error.replaceAll("\"", "");
    } else {
      try {
        result = expression + " = " + jep.getValue();
      } catch (ParseException e) {
        throw new HokanException(e);
      }
    }
    response.addResponse(result);
  }
}
