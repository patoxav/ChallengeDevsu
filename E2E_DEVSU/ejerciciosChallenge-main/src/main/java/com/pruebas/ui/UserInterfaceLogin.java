package com.pruebas.ui;

import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;

@DefaultUrl("page:webdriver.base.url.saucedemo")
public class UserInterfaceLogin extends PageObject {
    public static final Target INPUT_USUARIO = Target.the("Input de ingreso usuario")
            .locatedBy("//input[@id='user-name']");

    public static final Target INPUT_CONTRASENIA = Target.the("Input de ingreso CONTRASENIA")
            .locatedBy("//input[@id='password']");

    public static final Target LOGIN_BUTTON = Target.the("Input de ingreso usuario")
            .locatedBy("//input[@id='login-button']");
}
