package ru.iteco.fmhandroid.ui;

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
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.iteco.fmhandroid.EspressoIdlingResources;
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

    @BeforeClass
    public static void registerIdlingResourceAndLogin() {
        try (ActivityScenario<AppActivity> scenario = ActivityScenario.launch(AppActivity.class)) {
            IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
            AuthPage authPage = new AuthPage(true);
            staticMainPage = authPage.validLogin();
        }
    }

    @Before
    public void setup() {
        mainPage = staticMainPage;
    }

    @AfterClass
    public static void unregisterIdlingResourceAndLogout() {
        try (ActivityScenario<AppActivity> scenario = ActivityScenario.launch(AppActivity.class)) {
            staticMainPage.logOut();
            IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
        }
    }

    // case-nav1 Боковое меню открывается
    @Test
    public void shouldMenuOpen() {
        mainPage.clickButton(mainPage.getMenuButton());
        mainPage.checkMenuElements();
    }

    // case-nav2 Находясь в разделе Main в боковом меню кликабельны News, About. Main некликабелен
    @Test
    public void shouldBeClickableNewsAbout() {
        newsPage = mainPage.goToNews();
        mainPage = newsPage.goToMain();
        aboutPage = mainPage.goToAbout();
    }

    // case-nav3 Находясь в разделе News в боковом меню кликабельны Main, About. News некликабелен
    @Test
    public void shouldBeClickableMainAbout() {
        newsPage = mainPage.goToNews();
        mainPage = newsPage.goToMain();
        newsPage = mainPage.goToNews();
        aboutPage = newsPage.goToAbout();
    }

    // case-nav4 Находясь в разделе About бокового меню нет, есть кнопка "назад"
    @Test
    public void shouldBeButtonBackInAboutPage() {
        aboutPage = mainPage.goToAbout();
        aboutPage.checkElement(aboutPage.getBackButton());
    }

    // case-nav5 Переход с раздела Main в News через боковое меню возможен
    @Test
    public void shouldGoToNewsFromMain() {
        newsPage = mainPage.goToNews();
    }

    // case-nav6 Переход с раздела Main в News по кнопке "All news" возможен
    @Test
    public void shouldGoToNewsFromButtonAllNews() {
        newsPage = mainPage.goToNewsPageFromTextAllNews();
    }

    // case-nav7 Переход с раздела Main в About через боковое меню возможен
    @Test
    public void shouldGoToAbout() {
        aboutPage = mainPage.goToAbout();
    }

    // case-nav8 Переход с раздела News в Main через боковое меню возможен
    @Test
    public void shouldGoToMainFromNews() {
        newsPage = mainPage.goToNews();
        mainPage = newsPage.goToMain();
    }

    // case-nav9 Переход с раздела News в About через боковое меню возможен
    @Test
    public void shouldGoToAboutFromNews() {
        newsPage = mainPage.goToNews();
        aboutPage = newsPage.goToAbout();
    }

    // case-nav10 Переход с раздела About по кнопке "назад" в раздел Main
    @Test
    public void shouldGoToMainFromAbout() {
        aboutPage = mainPage.goToAbout();
        mainPage = aboutPage.clickBackButton();
    }

    // case-nav11 Нажатие на кнопку Цитаты (символ бабочки) открывает раздел с цитатами
    @Test
    public void shouldGoToQuoteFromMain() {
        quotePage = mainPage.goToQuote();
    }

    // case-nav12 Нажатие на профиль на верхней панели открывает кнопку "Log out"
    @Test
    public void shouldOpenPopupMenuLogout() {
        mainPage.clickButton(mainPage.getProfileButton());
        mainPage.getLogOutButton().check(matches(isDisplayed()));
    }
}
