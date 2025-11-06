package com.kenny.automation.Web.StepDefinitions;

import org.testng.asserts.SoftAssert;

import com.kenny.automation.Listeners.LogHelper;
import com.kenny.automation.Page.Login;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;

@Epic("Login Feature")
@Feature("Authentication")
public class LoginStep {

    private final Login login = new Login();
    private final SoftAssert softAssert = new SoftAssert();

    @Given("user is on the login page")
    public void userIsOnLoginPage() {
        
        LogHelper.step("User berada di halaman login");
        LogHelper.detail("Berhasil Menampilkan Halaman Login");
    }

    @When("user login with username {string} and password {string}")
    public void userLogin(String username, String password) {
    	
        login.performLogin(username, password);
        
    }

    @Then("login result should be {string}")
    public void loginResultShouldBe(String result) {
    	LogHelper.step("Verifikasi Login");
        if (result.equalsIgnoreCase("success")) {
            softAssert.assertTrue(login.isLoginSuccess(), "Login seharusnya berhasil tapi gagal.");
            LogHelper.step("Verifikasi Sukses");
        } else if (result.equalsIgnoreCase("failed")) {
            softAssert.assertTrue(login.isLoginFailed(), "Login seharusnya gagal tapi malah sukses.");
            LogHelper.step("Verifikasi Gagal");
        }
        softAssert.assertAll();
    }
}
