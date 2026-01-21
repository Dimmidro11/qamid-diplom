package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static ru.iteco.fmhandroid.ui.data.DataHelper.isElementVisible;

import android.view.View;

import androidx.test.espresso.ViewInteraction;

import junit.framework.AssertionFailedError;

import org.hamcrest.Matcher;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;

public class MainPage {
    // Набор матчеров
    private final Matcher<View> mainMatcher = allOf(withId(android.R.id.title), withText(R.string.main));

    // Набор элементов
    private final ViewInteraction imageView = onView(withId(R.id.trademark_image_view));
    private final ViewInteraction profileButton = onView(withId(R.id.authorization_image_button));
    private final ViewInteraction logOutButton = onView(allOf(withId(android.R.id.title), anyOf(withText("Log out"), withText("Выйти"))));
    private final ViewInteraction menuButton = onView(withId(R.id.main_menu_image_button));
    private final ViewInteraction mainButton = onView(mainMatcher);
    private final ViewInteraction newsButton = onView(allOf(withId(android.R.id.title), withText(R.string.news)));
    private final ViewInteraction aboutButton = onView(allOf(withId(android.R.id.title), withText(R.string.about)));
    private final ViewInteraction quoteButton = onView(withId(R.id.our_mission_image_button));
    private final ViewInteraction allNewsButton = onView(withId(R.id.all_news_text_view));

    // Конструктор с проверкой нахождения на странице Main по видимости элемента с логотипом ВХОСПИСЕ с флагом проверка/нет проверки
    public MainPage(boolean strict) {
        if (strict) {
            Allure.step("Проверка страницы Main в конструкторе");
            checkElement(imageView);
        }
    }

    // Конструктор с проверкой нахождения на странице Main по видимости элемента с логотипом ВХОСПИСЕ
    public MainPage() {
        Allure.step("Проверка страницы Main в конструкторе");
        checkElement(imageView);
    }

    // Геттеры
    public ViewInteraction getMenuButton() {
        return menuButton;
    }

    public ViewInteraction getProfileButton() {
        return profileButton;
    }

    public ViewInteraction getLogOutButton() {
        return logOutButton;
    }

    // Метод проверки видимости элемента
    @Step("Проверка элемента на видимость")
    public void checkElement(ViewInteraction element) {
        Allure.step("Проверка элемента на видимость");
        element.check(matches(isDisplayed()));
    }

    // Метод проверки видимости всех элементов открывающегося меню
    @Step("Проверка элементов меню на видимость")
    public void checkMenuElements() {
        Allure.step("Проверка элементов меню на видимость");
        checkElement(mainButton);
        checkElement(newsButton);
        checkElement(aboutButton);
    }

    // Метод клика по элементу
    @Step("Клик по элементу")
    public void clickButton(ViewInteraction button) {
        Allure.step("Клик по элементу");
        button.check(matches(isDisplayed()));
        button.perform(click());
    }

    // Метод для открытия меню с проверкой
    @Step("Открытие меню, если закрыто")
    private void openHamburger() {
        if (!isElementVisible(mainMatcher)) {
            Allure.step("Открытие закрытого меню");
            clickButton(menuButton);
        }
    }

    // Метод для проверки видимости элемента ВХОСПИСЕ с возвратом булева значения
    public boolean isImageViewVisible() {
        try {
            checkElement(imageView);
            return true;
        } catch (AssertionFailedError e) {
            return false;
        }
    }

    // Метод выхода из системы с проверкой
    @Step("Выход из системы, если авторизация пройдена")
    public AuthPage logOut() {
        Allure.step("Выход из системы, если авторизация пройдена");
        if (!isImageViewVisible()) {
            return new AuthPage();
        }
        clickButton(profileButton);
        clickButton(logOutButton);

        return new AuthPage();
    }

    // Метод перехода на страницу News через меню
    @Step("Переход на страницу News через меню")
    public NewsPage goToNews() {
        Allure.step("Переход на страницу News через меню");
        openHamburger();
        clickButton(newsButton);

        return new NewsPage();
    }

    // Метод перехода на страницу News по клику на All news
    @Step("Переход на страницу News по кнопке All news")
    public NewsPage goToNewsPageFromTextAllNews() {
        Allure.step("Переход на страницу News по кнопке All news");
        checkElement(allNewsButton);
        clickButton(allNewsButton);

        return new NewsPage();
    }

    // Метод для перехода на страницу About через меню
    @Step("Переход на страницу About через меню")
    public AboutPage goToAbout() {
        Allure.step("Переход на страницу About через меню");
        openHamburger();
        clickButton(aboutButton);

        return new AboutPage();
    }

    // Метод для перехода на страницу цитат
    @Step("Переход на страницу Quote")
    public QuotePage goToQuote() {
        Allure.step("Переход на страницу Quote");
        checkElement(quoteButton);
        clickButton(quoteButton);

        return new QuotePage();
    }

    // Метод для перехода на страницу Main через меню (для дочерних классов)
    @Step("Переход на страницу Main")
    public MainPage goToMain() {
        Allure.step("Переход на страницу Main");
        openHamburger();
        clickButton(mainButton);

        return new MainPage();
    }
}
