package com.pruebas.tasks;

import com.pruebas.model.ModelCredenciales;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.thucydides.core.annotations.Step;

import static com.pruebas.ui.SauceDemoLogin.*;
import static com.pruebas.ui.UserInterfaceLogin.INPUT_CONTRASENIA;
import static com.pruebas.ui.UserInterfaceLogin.INPUT_USUARIO;

public class Login implements Task {
    private final ModelCredenciales data;

    public Login(ModelCredenciales data){
        this.data = data;
    }
    public static Login ingresoCredenciales(ModelCredenciales data){
        return Tasks.instrumented(Login.class, data);
    }
    @Step("{0} inicia sesión:")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Task.where("Ingresa las credenciales e inicia sesión",
                        Enter.keyValues(data.getUsuario()).into(INPUT_USUARIO),
                        Enter.keyValues(data.getContrasenia()).into(INPUT_CONTRASENIA),
                        Click.on(LOGIN_BUTTON)
                ));
    }
}