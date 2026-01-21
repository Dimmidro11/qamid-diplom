package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import io.qameta.allure.android.rules.ScreenshotRule;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.EspressoIdlingResources;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.page.AboutPage;
import ru.iteco.fmhandroid.ui.page.AuthPage;
import ru.iteco.fmhandroid.ui.page.MainPage;
import ru.iteco.fmhandroid.ui.page.NewsPage;
import ru.iteco.fmhandroid.ui.page.QuotePage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class NavigationTests {
    private static MainPage staticMainPage;
    private MainPage mainPage;
    private AboutPage aboutPage;
    private NewsPage newsPage;
    private QuotePage quotePage;

    @Rule
    public ActivityScenarioRule<AppActivity> activityScenarioRule = new ActivityScenarioRule<>(AppActivity.class);

    @Rule
    public TestRule rule = new ScreenshotRule();

    // Проверяем на какой странице находимся, если на странице авторизации - авторизуемся
    @BeforeClass
    public static void registerIdlingResourceAndLogin() {
        try (ActivityScenario<AppActivity> scenario = ActivityScenario.launch(AppActivity.class)) {
            IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
            AuthPage authPage = new AuthPage(false);
            staticMainPage = authPage.validLogin();
        }
    }

    // В начале каждого теста находимся на странице Main
    @Before
    public void setup() {
        mainPage = staticMainPage;
    }

    // Выходим из системы после всех тестов
    @AfterClass
    public static void unregisterIdlingResourceAndLogout() {
        try (ActivityScenario<AppActivity> scenario = ActivityScenario.launch(AppActivity.class)) {
            staticMainPage.logOut();
            IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
        }
    }

    // case-nav1 Боковое меню открывается
    @Test
    @DisplayName("case-nav1 Боковое меню открывается")
    public void shouldMenuOpen() {
        // Кликаем на кнопку Меню
        mainPage.clickButton(mainPage.getMenuButton());

        // Проверяем на видимость кнопки меню
        mainPage.checkMenuElements();
    }

    // case-nav2 Находясь в разделе Main в боковом меню кликабельны News, About. Main некликабелен
    @Test
    @DisplayName("case-nav2 Находясь в разделе Main в боковом меню кликабельны News, About. Main некликабелен")
    public void shouldBeClickableNewsAbout() {
        // Переходим на страницу  News, проверяем переход
        newsPage = mainPage.goToNews();

        // Возвращаемся на страницу Main, проверяем переход
        mainPage = newsPage.goToMain();

        // Переходим на страницу About, проверяем переход
        aboutPage = mainPage.goToAbout();
    }

    // case-nav3 Находясь в разделе News в боковом меню кликабельны Main, About. News некликабелен
    @Test
    @DisplayName("case-nav3 Находясь в разделе News в боковом меню кликабельны Main, About. News некликабелен")
    public void shouldBeClickableMainAbout() {
        // Переходим на страницу  News, проверяем переход
        newsPage = mainPage.goToNews();

        // Возвращаемся на страницу Main, проверяем переход
        mainPage = newsPage.goToMain();

        // Возвращаемся на страницу  News, проверяем переход
        newsPage = mainPage.goToNews();

        // Переходим на страницу About, проверяем переход
        aboutPage = newsPage.goToAbout();
    }

    // case-nav4 Находясь в разделе About бокового меню нет, есть кнопка "назад"
    @Test
    @DisplayName("case-nav4 Находясь в разделе About бокового меню нет, есть кнопка \"назад\"")
    public void shouldBeButtonBackInAboutPage() {
        // Переходим на страницу About, проверяем переход
        aboutPage = mainPage.goToAbout();

        // Проверяем на видимость кнопку Назад на странице About
        aboutPage.getBackButton().check(matches(isDisplayed()));
    }

    // case-nav5 Переход с раздела Main в News через боковое меню возможен
    @Test
    @DisplayName("case-nav5 Переход с раздела Main в News через боковое меню возможен")
    public void shouldGoToNewsFromMain() {
        // Переходим на страницу News через основное меню, проверяем переход
        newsPage = mainPage.goToNews();
    }

    // case-nav6 Переход с раздела Main в News по кнопке "All news" возможен
    @Test
    @DisplayName("case-nav6 Переход с раздела Main в News по кнопке \"All news\" возможен")
    public void shouldGoToNewsFromButtonAllNews() {
        // Переходим на страницу News по кнопку All news на странице Main, проверяем переход
        newsPage = mainPage.goToNewsPageFromTextAllNews();
    }

    // case-nav7 Переход с раздела Main в About через боковое меню возможен
    @Test
    @DisplayName("case-nav7 Переход с раздела Main в About через боковое меню возможен")
    public void shouldGoToAbout() {
        // Переходим на страницу About через основное меню, проверяем переход
        aboutPage = mainPage.goToAbout();
    }

    // case-nav8 Переход с раздела News в Main через боковое меню возможен
    @Test
    @DisplayName("case-nav8 Переход с раздела News в Main через боковое меню возможен")
    public void shouldGoToMainFromNews() {
        // Переходим на страницу News, проверяем переход
        newsPage = mainPage.goToNews();


        // Переходим на страницу Main через основное меню, проверяем переход
        mainPage = newsPage.goToMain();
    }

    // case-nav9 Переход с раздела News в About через боковое меню возможен
    @Test
    @DisplayName("case-nav9 Переход с раздела News в About через боковое меню возможен")
    public void shouldGoToAboutFromNews() {
        // Переходим на страницу News, проверяем переход
        newsPage = mainPage.goToNews();

        // Переходим на страницу About через основное меню, проверяем переход
        aboutPage = newsPage.goToAbout();
    }

    // case-nav10 Переход с раздела About по кнопке "назад" в раздел Main
    @Test
    @DisplayName("case-nav10 Переход с раздела About по кнопке \"назад\" в раздел Main")
    public void shouldGoToMainFromAbout() {
        // Переходим на страницу About через основное меню, проверяем переход
        aboutPage = mainPage.goToAbout();

        // Кликаем на кнопку Назад, проверяем переход на страницу Main
        mainPage = aboutPage.clickBackButton();
    }

    // case-nav11 Нажатие на кнопку Цитаты (символ бабочки) открывает раздел с цитатами
    @Test
    @DisplayName("case-nav11 Нажатие на кнопку Цитаты (символ бабочки) открывает раздел с цитатами")
    public void shouldGoToQuoteFromMain() {
        // Кликаем на кнопку Цитаты и проверяем переход на страницу цитат
        quotePage = mainPage.goToQuote();
    }

    // case-nav12 Нажатие на профиль на верхней панели открывает кнопку "Log out"
    @Test
    @DisplayName("case-nav12 Нажатие на профиль на верхней панели открывает кнопку \"Log out\"")
    public void shouldOpenPopupMenuLogout() {
        // Кликаем на кнопку Профиль для вызова выпадающего
        mainPage.clickButton(mainPage.getProfileButton());

        // Проверяем выпадающую кнопку LogOut на видимость
        mainPage.getLogOutButton().check(matches(isDisplayed()));
    }
}
