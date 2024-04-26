@REQ_PQBP-511 @Saucedemo @cucumber @R1 @Agente1
Feature: Iniciar sesion en la pagina SauceDemo

  @id:1 @login @testTaller @TestNoFuncional
  Scenario Outline: T-E2E-PQBP-551-CA1- Iniciar sesión con credenciales correctas
    Given que el cliente admin ingresa las credenciales "<usuario>" "<contrasenia>"
    When selecciona "<producto1>" "<producto2>" "<suma>" e ingresa los datos "<nombre>" "<apellido>" "<codigo_postal>"
    Then visualiza el mensaje "Thank you for your order!"
    Examples:
      | @externaldata@EjercicioTaller.csv |
