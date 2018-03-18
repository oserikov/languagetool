package org.languagetool.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.languagetool.server.dto.CheckResultDTO;
import org.languagetool.server.dto.LanguageDTO;
import org.languagetool.server.service.LanguageToolApiService;
import org.languagetool.server.service.impl.LanguageToolApiServiceImpl;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.threadPool;

@Slf4j
public class LanguageToolApiController {

    private final ObjectMapper mapper;
    private final LanguageToolApiService languageToolApiService;

    public LanguageToolApiController() {
        log.info("BEFORE init()");
        mapper = new ObjectMapper();
        languageToolApiService = new LanguageToolApiServiceImpl();
        threadPool(200, 25, 60000);
        setUpEndPoints();
        log.info("AFTER init()");
    }


    private void setUpEndPoints() {
        get("/languages", (request, response) -> {

            log.info("GET /languages request");

            String responseString;
            int responseStatus;
            try {
                response.type("application/json");
                List<LanguageDTO> languages = languageToolApiService.languages();
                responseString = mapper.writeValueAsString(languages);
                responseStatus = HttpStatus.OK_200;
            } catch (Exception e) {
                log.error("Error!", e);
                responseString = "";
                responseStatus = HttpStatus.BAD_REQUEST_400;
            }
            response.status(responseStatus);

            log.info("GET /languages response='[body='{}', status='{}']'", responseString, response.status());

            return responseString;
        });

        post("/check", (request, response) -> {
            String text = request.queryParams("text");
            String language = request.queryParams("language");
            String motherTongue = request.queryParams("motherTongue");
            String preferredVariants = request.queryParams("preferredVariants");
            String enabledRules = request.queryParams("enabledRules");
            String disabledRules = request.queryParams("disabledRules");
            String enabledCategories = request.queryParams("enabledCategories");
            String disabledCategories = request.queryParams("disabledCategories");
            boolean enabledOnly = Boolean.parseBoolean(request.queryParams("enabledOnly"));

            log.info("POST /check request: " +
                            "text='{}', " +
                            "language='{}', " +
                            "motherTongue='{}', " +
                            "preferredVariants='{}', " +
                            "enabledRules='{}', " +
                            "disabledRules='{}', " +
                            "enabledCategories='{}', " +
                            "disabledCategories='{}', " +
                            "enabledOnly='{}'",
                    text,
                    language,
                    motherTongue,
                    preferredVariants,
                    enabledRules,
                    disabledRules,
                    enabledCategories,
                    disabledCategories,
                    enabledOnly
            );

            String responseString;
            int responseStatus;
            try {
                response.type("application/json");
                CheckResultDTO checkResultDTO = languageToolApiService.check(text, language, motherTongue, preferredVariants, enabledRules,
                        disabledRules, enabledCategories, disabledCategories, enabledOnly);
                responseString = mapper.writeValueAsString(checkResultDTO);
                responseStatus = HttpStatus.OK_200;
            } catch (Exception e) {
                log.error("Error!", e);
                responseString = "";
                responseStatus = HttpStatus.BAD_REQUEST_400;
            }

            response.status(responseStatus);

            log.info("POST /check request: " +
                            "text='{}', " +
                            "language='{}', " +
                            "motherTongue='{}', " +
                            "preferredVariants='{}', " +
                            "enabledRules='{}', " +
                            "disabledRules='{}', " +
                            "enabledCategories='{}', " +
                            "disabledCategories='{}', " +
                            "enabledOnly='{}', " +
                            "response='[body='{}', status='{}']'",
                    text,
                    language,
                    motherTongue,
                    preferredVariants,
                    enabledRules,
                    disabledRules,
                    enabledCategories,
                    disabledCategories,
                    enabledOnly,
                    responseString,
                    response.status()
            );

            return responseString;
        });
    }

}
