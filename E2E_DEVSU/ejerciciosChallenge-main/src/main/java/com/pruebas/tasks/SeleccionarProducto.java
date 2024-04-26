package com.pruebas.tasks;

import com.pruebas.model.ModelProductos;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.questions.Text;
import net.thucydides.core.annotations.Step;

import static com.pruebas.ui.UserInterfaceCarrito.BUTTON_AGREGAR_CARRITO;
import static com.pruebas.ui.UserInterfaceCarrito.LABEL_PRECIO;

public class SeleccionarProducto implements Task {
    private final ModelProductos data;

    public SeleccionarProducto(ModelProductos data){
        this.data = data;
    }
    public static SeleccionarProducto seleccionarProductos(ModelProductos data){
        return Tasks.instrumented(SeleccionarProducto.class, data);
    }
    @Step("{0} Agregar los productos:")
    @Override
    public <T extends Actor> void performAs(T actor) {

        double precioProductoUno = Double.parseDouble(actor.asksFor(Text.of(LABEL_PRECIO.of(data.getProducto1())))
                .trim()
                .replace("$", "")
        );

        double precioProductodOS = Double.parseDouble(actor.asksFor(Text.of(LABEL_PRECIO.of(data.getProducto2())))
                .trim()
                .replace("$", "")
        );

        double valorRespuesta = precioProductoUno + precioProductoUno;

        actor.attemptsTo(
                Task.where("Agrega los productos",
                        Click.on(BUTTON_AGREGAR_CARRITO.of(data.getProducto1())),
                        Click.on(BUTTON_AGREGAR_CARRITO.of(data.getProducto2()))
                //        Ensure.that(valorRespuesta).isLessThanOrEqualTo(Double.parseDouble(data.getSuma()))
                ) );
    }
}
