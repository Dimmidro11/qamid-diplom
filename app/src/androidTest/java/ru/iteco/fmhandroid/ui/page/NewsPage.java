package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.matchesPattern;

import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;

public class NewsPage extends MainPage {
    // Набор элементов
    private final ViewInteraction newsText = onView(
                    allOf(anyOf(withText("News"), withText("Новости")),
                    withParent(withParent(withId(R.id.container_list_news_include)))));
    private final ViewInteraction filterButton = onView(withId(R.id.sort_news_material_button));
    private final ViewInteraction sortButton = onView(withId(R.id.filter_news_material_button));
    private final ViewInteraction acceptFilterButton = onView(withId(R.id.filter_button));
    private final ViewInteraction cancelFilterButton = onView(withId(R.id.cancel_button));
    private final ViewInteraction editNewsButton = onView(withId(R.id.edit_news_material_button));
    private final ViewInteraction refreshNewsListButton = onView(withId(R.id.news_retry_material_button));
    private final ViewInteraction emptyNewsText = onView(withId(R.id.empty_news_list_text_view));

    // Конструктор с проверкой нахождения на странице News по видимости элемента с текстом News
    public NewsPage() {
        Allure.step("Проверка страницы News в конструкторе");
        checkElement(newsText);
    }

    // Геттеры
    public ViewInteraction getCancelFilterButton() {
        return cancelFilterButton;
    }

    public ViewInteraction getEditNewsButton() {
        return editNewsButton;
    }

    public ViewInteraction getRefreshNewsListButton() {
        return refreshNewsListButton;
    }

    public ViewInteraction getEmptyNewsText() {
        return emptyNewsText;
    }

    public ViewInteraction getNewsText() {
        return newsText;
    }

    public ViewInteraction getAcceptFilterButton() {
        return acceptFilterButton;
    }

    public ViewInteraction getFilterButton() {
        return filterButton;
    }

    public ViewInteraction getSortButton() {
        return sortButton;
    }

    // Метод клика по элементу
    @Step("Клик по элементу")
    public void clickButton(ViewInteraction button) {
        Allure.step("Клик по элементу");
        button.perform(click());
    }

    // Метод перехода на страницу ControlPage по клику на кнопку EditNews
    @Step("Переход на страницу Control по кнопку Edit News")
    public ControlPage goToControlPage() {
        Allure.step("Переход на страницу Control по кнопку Edit News");
        editNewsButton.check(matches(isDisplayed()));
        clickButton(editNewsButton);

        return new ControlPage();
    }
}
