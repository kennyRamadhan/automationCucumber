Feature: Checkout Flow

  @Regression @Checkout
  Scenario: User melakukan checkout semua produk dengan data valid
    Given user is logged in as "standard_user"
    When user adds all products to cart
    And user proceeds to checkout
    And user fills checkout form with:
      | firstName | lastName | postalCode |
      | Kenny     | Ramadhan | 12345      |
    And user continues checkout
    And user finishes the order
    Then order should be completed successfully
    And subtotal + tax should equal to grand total
