Feature: Login Feature

  @Smoke
  Scenario Outline: Verify user can login with various credentials
    Given user is on the login page
    When user login with username "<username>" and password "<password>"
    Then login result should be "<result>"

    Examples:
      | username         | password        | result   |
      | standard_user    | secret_sauce    | success  |
      | invalid_username | wrong_password  | failed   |
