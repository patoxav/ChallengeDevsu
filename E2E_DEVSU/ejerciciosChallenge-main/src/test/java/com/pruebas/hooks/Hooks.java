package com.pruebas.hooks;

import com.google.common.base.Splitter;
import com.pruebas.model.ModelScenario;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.Scenario;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

import java.util.List;

import static net.serenitybdd.screenplay.actors.OnStage.setTheStage;

public class Hooks {

    @Before
    public void firstSetTheStage() {
        setTheStage(new OnlineCast());
    }

    @Before
    public void setScenario(Scenario scenario) {
        ModelScenario.setScenario(scenario);
    }

    @ParameterType(".*")
    public Actor actor(String actor) {
        return OnStage.theActorCalled(actor);
    }

    @ParameterType(".*")
    public List<String> items(String listOfItems) {
        return Splitter.on(",").trimResults().omitEmptyStrings().splitToList(listOfItems);
    }
}
