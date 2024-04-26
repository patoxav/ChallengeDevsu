package com.pruebas.tasks;

import com.pruebas.model.ModelProductos;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.thucydides.core.annotations.Step;

import static com.pruebas.ui.UserInterfaceCarrito.*;

public class LlenarFormulario implements Task {
    private final ModelProductos data;

    public LlenarFormulario(ModelProductos data){
        this.data = data;
    }
    public static LlenarFormulario ingresoDatos(ModelProductos data){
        return Tasks.instrumented(LlenarFormulario.class, data);
    }
    @Step("{0} llena formulario:")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Task.where("Seleccion e ingresa los datos",
                        Click.on(BUTTON_CARRITO),
                        Click.on(BUTTON_CHECKOUT),
                        Enter.keyValues(data.getNombre()).into(INPUT_NOMBRE),
                        Enter.keyValues(data.getApellido()).into(INPUT_APELLIDO),
                        Enter.keyValues(data.getCodigoPostal()).into(INPUT_CODIGOPOSTAL),
                        Click.on(BUTTON_CONTINUE),
                        Click.on(BUTTON_FINISH)
        ));
    }
}