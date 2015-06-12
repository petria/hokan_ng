package com.freakz.hokan_ng.core_engine.command.handlers;

import com.freakz.hokan_ng.common.exception.HokanException;
import com.freakz.hokan_ng.common.rest.InternalRequest;
import com.freakz.hokan_ng.common.rest.messages.EngineResponse;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.freakz.hokan_ng.common.util.StaticStrings.ARG_PLACE;

/**
 * Created by Petri Airio on 11.6.2015.
 *
 */
@Component
@Scope("prototype")
public class WeatherBattleCmd extends Cmd {

    public WeatherBattleCmd() {
        UnflaggedOption opt = new UnflaggedOption(ARG_PLACE)
                .setDefault("Jyväskylä")
                .setRequired(true)
                .setGreedy(false);
        registerParameter(opt);

    }

    @Override
    public String getMatchPattern() {
        return "!wbattle.*";
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

    }
}
