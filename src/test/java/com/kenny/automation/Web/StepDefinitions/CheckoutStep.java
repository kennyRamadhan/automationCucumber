package com.kenny.automation.Web.StepDefinitions;

import java.util.Map;


import org.testng.asserts.SoftAssert;

import com.kenny.automation.Listeners.LogHelper;
import com.kenny.automation.Page.Checkout;
import com.kenny.automation.Page.Dashboard;
import com.kenny.automation.Page.Login;
import com.kenny.automation.Web.Hooks.Hook;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;


@Epic("Checkout Feature")
@Feature("Checkout Order")
public class CheckoutStep {

    private final Login login = new Login();
    private final Dashboard dashboard = new Dashboard();
    private final Checkout checkout = new Checkout();
    private final SoftAssert softAssert = new SoftAssert();

    private Double subTotal;
    private Double tax;
    private Double grandTotal;

    @SuppressWarnings("unused")
	private Hook hook = new Hook(); 
  

    @Given("user is logged in as {string}")
    public void userIsLoggedIn(String username) {
    
        login.performLoginWithHardcoded(username);
        softAssert.assertTrue(login.isLoginSuccess(), "Login gagal untuk user: " + username);
    }

    @When("user adds all products to cart")
    public void userAddsAllProductsToCart() {
        int added = dashboard.selectAllProductsToCart();
        softAssert.assertTrue(added > 0, "Tidak ada produk yang berhasil ditambahkan.");
    }

    @And("user proceeds to checkout")
    public void userProceedsToCheckout() {
        checkout.checkoutProducts();
    }

    @And("user fills checkout form with:")
    public void userFillsCheckoutForm(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        checkout.inputFirstName(data.get("firstName"));
        checkout.inputLastName(data.get("lastName"));
        checkout.inputPostalCode(data.get("postalCode"));
    }

    @And("user continues checkout")
    public void userContinuesCheckout() {
        boolean isValid = checkout.submitInformation();
        softAssert.assertTrue(isValid, "Form checkout tidak valid atau muncul error.");
    }

    @And("user finishes the order")
    public void userFinishesOrder() {
        checkout.scrollToFinishOrder();
        subTotal = checkout.getSubTotal();
        tax = checkout.getTax();
        grandTotal = checkout.getGrandTotal();
        checkout.finishOrder();
    }

    @Then("order should be completed successfully")
    public void orderShouldBeCompletedSuccessfully() {
        softAssert.assertTrue(checkout.isSuccessOrderDisplayed(), "Pesan sukses tidak muncul setelah checkout.");
    }

    @And("subtotal + tax should equal to grand total")
    public void verifyTotalCalculation() {
        double expectedTotal = Math.round((subTotal + tax) * 100.0) / 100.0;
        softAssert.assertEquals(grandTotal, expectedTotal, "Grand total tidak sesuai hasil perhitungan.");
        LogHelper.step("Subtotal + Tax = " + expectedTotal + " | GrandTotal: " + grandTotal);
        softAssert.assertAll();
    }
}
