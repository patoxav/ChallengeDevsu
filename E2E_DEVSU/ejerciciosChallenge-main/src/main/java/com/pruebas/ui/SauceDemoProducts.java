package com.pruebas.ui;

import net.serenitybdd.screenplay.targets.Target;

public class SauceDemoProducts {

    private SauceDemoProducts() {
    }

    public static final Target PRODUCT_TITLE = Target.the("Titulo de lista de Productos")
            .locatedBy("//span[contains(text(), 'Products')]");
}
