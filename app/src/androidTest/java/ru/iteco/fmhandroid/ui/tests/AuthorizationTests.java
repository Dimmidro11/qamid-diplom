package ru.iteco.fmhandroid.ui.tests;


import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import io.qameta.allure.android.rules.ScreenshotRule;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.EspressoIdlingResources;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AuthPage;
import ru.iteco.fmhandroid.ui.page.MainPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class AuthorizationTests {

    private MainPage mainPage;
    private AuthPage authPage;

    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Rule
    public TestRule rule = new ScreenshotRule();

    @BeforeClass
    public static void registerIdlingResourceAndLogin() {
        // Проверяем на какой странице находимся, если на Main, тогда выходим из системы
        try (ActivityScenario<AppActivity> scenario = ActivityScenario.launch(AppActivity.class)) {
            IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
            MainPage staticMainPage = new MainPage(false);
            AuthPage staticAuthPage = staticMainPage.logOut();
            IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
        }
    }

    @Before
    public void registerIdlingResource() {
        // Регистрируем IdlingResources для отслеживания команды ожидания
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
        authPage = new AuthPage();
    }

    @After
    public void unregisterIdlingResource() {
        // Отключаем IdlingResources
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
    }

    // case-auth1 Успешная авторизация с валидными данными
    @Test
    @DisplayName("case-auth1 Успешная авторизация с валидными данными")
    public void shouldLoginHappyPath() {

        // Вводим валидные данные и проверяем переход на главную страницу по видимости элемента "ВХОСПИСЕ"
        mainPage = authPage.validLogin();

        // Завершаем сеанс для следующих тестов
        mainPage.logOut();
    }

    // case-auth2 Неуспешная авторизация с невалидным логином
    @Test
    @DisplayName("case-auth2 Неуспешная авторизация с невалидным логином")
    public void shouldNotLoginInvalidLogin() {

        // Вводим невалидный логин и валидный пароль, пробуем войти. Проверяем провал входа по видимости поля Логин
        authPage.invalidLogin(DataHelper.getRandomString(), DataHelper.getCorrectPassword());
    }

    // case-auth3 Неуспешная авторизация с невалидными паролем
    @Test
    @DisplayName("case-auth3 Неуспешная авторизация с невалидными паролем")
    public void shouldNotLoginInvalidPassword() {

        // Вводим валидный логин и невалидный пароль, пробуем войти. Проверяем провал входа по видимости поля Логин
        authPage.invalidLogin(DataHelper.getCorrectLogin(), DataHelper.getRandomString());
    }

    // case-auth4 Неуспешная авторизация с невалидными логином и паролем
    @Test
    @DisplayName("case-auth4 Неуспешная авторизация с невалидными логином и паролем")
    public void shouldNotLoginInvalidLoginAndPassword() {
        // Вводим невалидные логин и пароль, пробуем войти. Проверяем провал входа по видимости поля Логин
        authPage.invalidLogin(DataHelper.getRandomString(), DataHelper.getRandomString());
    }

    // case-auth5 Неуспешная авторизация с пустым логином
    @Test
    @DisplayName("case-auth5 Неуспешная авторизация с пустым логином")
    public void shouldNotLoginEmptyLogin() {
        // Вводим пустой логин и невалидный пароль, пробуем войти. Проверяем провал входа по видимости поля Логин
        authPage.invalidLogin("", DataHelper.getRandomString());
    }

    // case-auth6 Неуспешная авторизация с пустым паролем
    @Test
    @DisplayName("case-auth6 Неуспешная авторизация с пустым паролем")
    public void shouldNotLoginEmptyPassword() {
        // Вводим пустые логин и пароль, пробуем войти. Проверяем провал входа по видимости поля Логин
        authPage.invalidLogin(DataHelper.getCorrectLogin(), "");
    }

    // case-auth7 Выход из системы "Log out" возвращает к экрану авторизации
    @Test
    @DisplayName("case-auth7 Выход из системы \"Log out\" возвращает к экрану авторизации")
    public void shouldLogOutReturnToAuthorisation() {
        // Логинимся
        mainPage = authPage.validLogin();

        // Выходим из системы
        authPage = mainPage.logOut();

        // Проверяем на видимость поле ввода логина
        authPage.checkLoginField();
    }
}
