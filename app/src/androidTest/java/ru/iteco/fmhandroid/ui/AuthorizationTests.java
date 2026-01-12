package ru.iteco.fmhandroid.ui;


import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.iteco.fmhandroid.EspressoIdlingResources;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AuthPage;
import ru.iteco.fmhandroid.ui.page.MainPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class AuthorizationTests {

    private MainPage mainPage;

    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
    }

    // case-auth1 Успешная авторизация с валидными данными
    @Test
    public void shouldLoginHappyPath() {
        AuthPage authPage = new AuthPage();
        mainPage = authPage.validLogin();
        mainPage.logOut();
    }

    // case-auth2 Неуспешная авторизация с невалидным логином
    @Test
    public void shouldNotLoginInvalidLogin() {
        AuthPage authPage = new AuthPage();
        authPage.invalidLogin(DataHelper.getRandomString(), DataHelper.getCorrectPassword());
    }

    // case-auth3 Неуспешная авторизация с невалидными паролем
    @Test
    public void shouldNotLoginInvalidPassword() {
        AuthPage authPage = new AuthPage();
        authPage.invalidLogin(DataHelper.getCorrectLogin(), DataHelper.getRandomString());
    }

    // case-auth4 Неуспешная авторизация с невалидными логином и паролем
    @Test
    public void shouldNotLoginInvalidLoginAndPassword() {
        AuthPage authPage = new AuthPage();
        authPage.invalidLogin(DataHelper.getRandomString(), DataHelper.getRandomString());
    }

    // case-auth5 Неуспешная авторизация с пустым логином
    @Test
    public void shouldNotLoginEmptyLogin() {
        AuthPage authPage = new AuthPage();
        authPage.invalidLogin("", DataHelper.getRandomString());
    }

    // case-auth6 Неуспешная авторизация с пустым паролем
    @Test
    public void shouldNotLoginEmptyPassword() {
        AuthPage authPage = new AuthPage();
        authPage.invalidLogin(DataHelper.getCorrectLogin(), "");
    }

    // case-auth7 Выход из системы "Log out" возвращает к экрану авторизации
    @Test
    public void shouldLogOutReturnToAuthorisation() {
        AuthPage authPage = new AuthPage();
        mainPage = authPage.validLogin();
        authPage = mainPage.logOut();
        authPage.checkLoginField();
    }
}
