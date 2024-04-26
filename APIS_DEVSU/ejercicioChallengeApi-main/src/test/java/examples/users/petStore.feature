Feature: Ejercicio de Automatizaci?n APIs
  Background:
    * baseUrl= 'https://petstore.swagger.io/'
    * pathPet= '/v2/pet/'
    * pathStatus= '/findByStatus?status=sold/'

  @id:1 @PetStore @CrearMascota
  Scenario: Agregar Nueva Mascota
    * header content-type = 'application/json'
    Given url baseUrl + pathPet
    And def pet = read('classpath:../examples/data/pet.json')
    And request pet
    When method POST
    Then status 200
    And print pet
    And print response


  @id:2 @PetStore @ConsultarMascota
  Scenario Outline: Consultar mascota por id
    Given url baseUrl + pathPet
    And path <id>
    When method GET
    Then print response
    Examples:
      | id |
      | 2111 |

  @id:3 @PetStore @ActualizarMascota
  Scenario Outline: Actualizar sombre y estado mascota
    Given url baseUrl + pathPet
    And def pet = read('classpath:../examples/data/petupdate.json')
    And request pet
    When method POST
    Then status 200
    And print pet
    Examples:
      | id | nombre | Estado    |
      | 2111  | Moccachina  | sold |

  @id:4 @PetStore @ConsultarMascotaActualizada
  Scenario: Consultar mascota por estado
    Given url baseUrl + pathPet + pathStatus
    When method GET
    Then status 200

