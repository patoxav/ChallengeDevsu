package com.pruebas.ui;

import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;

public class UserInterfaceCarrito extends PageObject {
    public static final Target BUTTON_AGREGAR_CARRITO = Target.the("Botón agregar carrito")
            .locatedBy("//div[normalize-space()='{0}']//following::button[1]");

    public static final Target LABEL_PRECIO = Target.the("Label precio")
            .locatedBy("//div[normalize-space()='{0}']//following::div[contains(text(),'$') and @class='inventory_item_price'][1]");

    public static final Target BUTTON_CARRITO = Target.the("Botón carrito")
            .locatedBy("//a[@class='shopping_cart_link']");

    public static final Target BUTTON_CHECKOUT = Target.the("Botón checkout")
            .locatedBy("//button[@id='checkout']");
    public static final Target INPUT_NOMBRE = Target.the("Input de nombre")
            .locatedBy("//input[@id='first-name']");
    public static final Target INPUT_APELLIDO = Target.the("Input de apellido")
            .locatedBy("//input[@id='last-name']");
    public static final Target INPUT_CODIGOPOSTAL = Target.the("Input codigo postal")
            .locatedBy("//input[@id='postal-code']");
    public static final Target BUTTON_CONTINUE = Target.the("Botón continuar")
            .locatedBy("//input[@id='continue']");
    public static final Target BUTTON_FINISH = Target.the("Botón finalizar")
            .locatedBy("//button[@id='finish']");
    public static final Target LABEL_MENSAJEEXITOSO = Target.the("Label mensaje exitoso")
            .locatedBy("//h2[normalize-space()='Thank you for your order!']");
    public static final Target BUTTON_MENU = Target.the("Botón menú")
            .locatedBy("//button[@id='react-burger-menu-btn']");


}
