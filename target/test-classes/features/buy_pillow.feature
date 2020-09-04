@midtrans-store
Feature: User buy pillow with credit card

  @buy-pillow
  Scenario Outline: Buy pillow with credit card both success and declined card
    Given Open "chrome" browser and start application
    When I click buy now button
    Then I should see Shopping Cart details
    When I input the customer details
    And I press checkout
    Then I should see order summary popup
    Then I get the right shipping details
    When I click continue button
    And I picked Credit Card as payment method
    Then I get the right amount to pay
    And I input card number
      |cardNumber | <cardNumber>|
    And I input expiry date and cvv
    And I click on pay now button
    And I am on input token form
    And I input BANK OTP
    And I press OK
    Then I should see transaction status
      |status | <transactionStatus>|
    Then I close the browser
    Examples:
      | cardNumber          | transactionStatus |
      | 4811 1111 1111 1114 |       success     |
      | 4911 1111 1111 1113 |       failed      |
