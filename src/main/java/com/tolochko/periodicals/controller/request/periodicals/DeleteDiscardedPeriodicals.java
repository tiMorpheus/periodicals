package com.tolochko.periodicals.controller.request.periodicals;

import com.tolochko.periodicals.controller.message.FrontMessage;
import com.tolochko.periodicals.controller.message.FrontMessageFactory;
import com.tolochko.periodicals.controller.request.RequestProcessor;
import com.tolochko.periodicals.controller.util.HttpUtil;
import com.tolochko.periodicals.model.service.PeriodicalService;
import com.tolochko.periodicals.model.service.impl.PeriodicalServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes a POST request to delete all periodicals with status = "discarded".
 */
public class DeleteDiscardedPeriodicals implements RequestProcessor {

    private static final DeleteDiscardedPeriodicals instance = new DeleteDiscardedPeriodicals();
    private FrontMessageFactory messageFactory = FrontMessageFactory.getInstance();
    private PeriodicalService periodicalService = PeriodicalServiceImpl.getInstance();

    private DeleteDiscardedPeriodicals(){}

    public static DeleteDiscardedPeriodicals getInstance() {
        return instance;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        List<FrontMessage> generalMessages = new ArrayList<>();

        persistPeriodicalsToDeleteAndRelatedData();

        int deletedPeriodicalsNumber = periodicalService.deleteAllDiscarded();
        addDeleteResultMessage(generalMessages, deletedPeriodicalsNumber);

        HttpUtil.addGeneralMessagesToSession(request, generalMessages);

        return REDIRECT + "/app/periodicals";
    }

    private void addDeleteResultMessage(List<FrontMessage> generalMessages, int deletedPeriodicalsNumber) {
        FrontMessage message = (deletedPeriodicalsNumber > 0)
                ? messageFactory.getSuccess("validation.discardedPeriodicalsDeleted.success")
                : messageFactory.getWarning("validation.thereIsNoPeriodicalsToDelete.warning");

        generalMessages.add(message);
    }

    private void persistPeriodicalsToDeleteAndRelatedData() {
        /*Here goes implementation of persisting somehow deleted data. It can be serialization into
        * files or saving into an archive database.*/
    }

}
