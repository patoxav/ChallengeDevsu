package com.pruebas.glue;

import com.pruebas.model.ModelCredenciales;
import com.pruebas.model.ModelProductos;
import com.pruebas.tasks.LlenarFormulario;
import com.pruebas.tasks.Login;
import com.pruebas.tasks.SeleccionarProducto;
import com.pruebas.ui.UserInterfaceLogin;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Open;

import static com.pruebas.ui.UserInterfaceCarrito.LABEL_MENSAJEEXITOSO;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.containsText;
import static net.serenitybdd.screenplay.questions.WebElementQuestion.the;


public class EjercicioTallerGlue {
    @Given("que el cliente {actor} ingresa las credenciales {string} {string}")
    public void queElClienteAdminIngresaLasCredenciales(Actor actor, String usuario, String contrasenia) {
        ModelCredenciales modelCredenciales = new ModelCredenciales(usuario, contrasenia);
        givenThat(actor).attemptsTo(
                Open.browserOn().the(UserInterfaceLogin.class),
                Login.ingresoCredenciales(modelCredenciales)
        );
    }
    @When("selecciona {string} {string} {string} e ingresa los datos {string} {string} {string}")
    public void seleccionaEIngresaLosDatos(String producto1, String producto2, String suma, String nombre, String apellido, String codigo_postal) {
        ModelProductos modelProductos = new ModelProductos(producto1, producto2, suma, nombre, apellido, codigo_postal);
        when(theActorInTheSpotlight()).attemptsTo(
                SeleccionarProducto.seleccionarProductos(modelProductos),
                LlenarFormulario.ingresoDatos(modelProductos)
        );
    }
    @Then("visualiza el mensaje {string}")
    public void visualizaElMensaje(String mensaje) {
        then(theActorInTheSpotlight()).should(
                seeThat(the(LABEL_MENSAJEEXITOSO), containsText(mensaje))
        );
    }

}
