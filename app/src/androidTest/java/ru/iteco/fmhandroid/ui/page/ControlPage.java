package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static ru.iteco.fmhandroid.ui.utils.CustomViewAssertions.isRecycleView;
import static ru.iteco.fmhandroid.ui.data.DataHelper.formatDateFromArray;
import static ru.iteco.fmhandroid.ui.data.DataHelper.getCurrentDate;
import static ru.iteco.fmhandroid.ui.data.DataHelper.isElementVisible;

import android.view.View;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;

import org.hamcrest.Matcher;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.utils.MaterialTextExtractor;

public class ControlPage {
    // Набор матчеров элементов
    private final Matcher<View> titleNewMatcher = withId(R.id.news_item_title_text_view);
    private final Matcher<View> newItemCardMatcher = withId(R.id.news_item_material_card_view);
    private final Matcher<View> newListMatcher = withId(R.id.news_list_recycler_view);
    private final Matcher<View> pubDateMatcher = withId(R.id.news_item_publication_date_text_view);
    private final Matcher<View> createDateMatcher = withId(R.id.news_item_create_date_text_view);
    private final Matcher<View> newDescriptionMatcher = withId(R.id.news_item_description_text_view);
    private final Matcher<View> deleteNewFromListMatcher = withId(R.id.delete_news_item_image_view);
    private final Matcher<View> editNewMatcher = withId(R.id.edit_news_item_image_view);

    // Набор элементов
    private final ViewInteraction controlText = onView(anyOf(withText("Control panel"), withText("Панель управления")));
    private final ViewInteraction titleNew = onView(titleNewMatcher);
    private final ViewInteraction sortButton = onView(withId(R.id.sort_news_material_button));
    private final ViewInteraction filterButton = onView(withId(R.id.filter_news_material_button));
    private final ViewInteraction filterNewsCheckboxActive = onView(withId(R.id.filter_news_active_material_check_box));
    private final ViewInteraction filterNewsCheckboxNotActive = onView(withId(R.id.filter_news_inactive_material_check_box));
    private final ViewInteraction categoryField = onView(withId(com.google.android.material.R.id.text_input_end_icon));
    private final ViewInteraction acceptFilterButton = onView(withId(R.id.filter_button));
    private final ViewInteraction cancelConfirmationButton = onView(allOf(withId(android.R.id.button2)));
    private final ViewInteraction acceptConfirmationButton = onView(allOf(withId(android.R.id.button1)));
    private final ViewInteraction editNewFromList = onView(editNewMatcher);
    private final ViewInteraction newsList = onView(newListMatcher);
    private final ViewInteraction addNewButton = onView(allOf(withId(R.id.add_news_image_view), withContentDescription("Add news button")));

    // Конструктор с проверкой нахождения на странице Control по видимости элемента с текстом Control Panel
    public ControlPage() {
        controlText.check(matches(isDisplayed()));
    }

    // Геттеры
    public ViewInteraction getSortButton() {
        return sortButton;
    }

    public ViewInteraction getTitleNew() {
        return titleNew;
    }

    public ViewInteraction getNewsList() {
        return newsList;
    }

    // Геттер для матчера заголовка новости из списке новостей
    public Matcher<View> getMatcherTitle(String title) {
        return allOf(
                newItemCardMatcher,
                hasDescendant(allOf(
                        titleNewMatcher,
                        withText(title)
                ))
        );
    }

    // Геттер для элемента заголовка в списке новостей
    public ViewInteraction getTitleText(String title) {
        return onView(allOf(titleNewMatcher, withText(title)));
    }

    // Метод клика на элемент
    @Step("Клик на элемент")
    public void clickButton(ViewInteraction button) {
        Allure.step("Клик на элемент");
        button.perform(click());
    }

    // Метод для перехода на страницу создания новости по клику на кнопку Create new
    @Step("Переход на страницу создания новости")
    public EditOrCreateNewsPage goToCreateNews() {
        Allure.step("Переход на страницу создания новости");
        clickButton(addNewButton);

        return new EditOrCreateNewsPage();
    }

    // Метод для перехода на страницу редактирования первой новости из списка по клику на кнопку Edit new
    @Step("Переход к редактированию первой новости из списка")
    public EditOrCreateNewsPage goToEditNews() {
        Allure.step("Переход к редактированию первой новости из списка");
        if (!isElementVisible(allOf(newDescriptionMatcher, isDisplayed()))) {
            onView(newListMatcher)
                    .perform(actionOnItemAtPosition(0, click()));
        }
        onView(allOf(
                editNewMatcher,
                hasSibling(allOf(newDescriptionMatcher, isDisplayed()))
        )).perform(click());


        return new EditOrCreateNewsPage();
    }

    // Метод для перехода к редактированию новости из списка по заголовку
    @Step("Переход к редактированию новости по заголовку")
    public EditOrCreateNewsPage goToEditNews(String title) {
        Allure.step("Переход к редактированию новости по заголовку");
        Matcher<View> newsCard = getMatcherTitle(title);

        scrollTONew(newsCard);

        onView(allOf(
                editNewMatcher,
                isDescendantOfA(newsCard)
        )).perform(click());

        return new EditOrCreateNewsPage();
    }

    // Метод проверки видимости новости в списке по заголовку
    @Step("Проверка на видимость новости по заголовку")
    public void checkAddedTitleNew(String title) {
        Allure.step("Проверка на видимость новости по заголовку");
        Matcher<View> newsCard = getMatcherTitle(title);
        scrollTONew(newsCard);
        getNewsList().check(isRecycleView());
        getTitleText(title).check(matches(isDisplayed()));
    }

    // Метод проверки отсутствия новости в списке по заголовку
    @Step("Проверка на отсутствие в дереве элементов новости по заголовку")
    public void checkExistTitleNew(String title) {
        Allure.step("Проверка на отсутствие в дереве элементов новости по заголовку");
        getTitleText(title).check(doesNotExist());
    }

    // Метод для скролла к карточке новости
    public void scrollTONew(Matcher<View> newsCard) {
        Allure.step("Скролл к новости из списка");
        onView(newListMatcher)
                .perform(RecyclerViewActions.scrollTo(newsCard));
    }

    // Метод для клика на карточку новости
    @Step("Нажатие на карточку новости")
    public void clickNew(Matcher<View> newsCard) {
        Allure.step("Нажатие на новость в спике");
        onView(newsCard).perform(click());
    }

    // Метод для проверки полей заголовка, описания и даты публикации созданной/редактированной новости
    @Step("Проверка заголовка, описания и даты публикации новости")
    public void checkCreatedNew(String title, String description, int[] date) {
        Allure.step("Проверка заголовка, описания и даты публикации новости");

        Matcher<View> newsCard = getMatcherTitle(title);

        scrollTONew(newsCard);
        clickNew(newsCard);

        onView(allOf(
                pubDateMatcher,
                isDescendantOfA(newsCard)
        )).check(DataHelper.createTextAssertion(formatDateFromArray(date)));

        onView(allOf(
                newDescriptionMatcher,
                isDescendantOfA(newsCard)
        )).check(DataHelper.createTextAssertion(description));

    }

    // Метод проверки даты создания/редактирования новости
    @Step("Проверка даты создания новости")
    public void checkNewsCreatedDate(String title) {
        Allure.step("Проверка даты создания новости");

        Matcher<View> newsCard = getMatcherTitle(title);

        scrollTONew(newsCard);
        clickNew(newsCard);

        onView(allOf(
                createDateMatcher,
                isDescendantOfA(newsCard)
        )).check(DataHelper.createTextAssertion(getCurrentDate()));
    }

    // Метод для удаления новости из списка по заголовку, с подтверждением/отказом удаления и соответствующая проверка
    @Step("Удаление новости по заголовку с подтверждением/отказом сохранения с соответствующей проверкой")
    public void deleteNew(Boolean confirm, String title) {
        Allure.step("Удаление новости по заголовку с подтверждением/отказом сохранения с соответствующей проверкой");
        Matcher<View> newsCard = getMatcherTitle(title);
        onView(newListMatcher)
                .perform(RecyclerViewActions.scrollTo(newsCard));
        onView(allOf(
                deleteNewFromListMatcher,
                isDescendantOfA(newsCard)
        )).perform(click());
        if (confirm) {
            acceptConfirmationButton.perform(click());
            getNewsList().check(isRecycleView());
            getTitleText(title).check(doesNotExist());
        } else {
            cancelConfirmationButton.perform(click());
            getNewsList().check(isRecycleView());
            getTitleText(title).check(matches(isDisplayed()));
        }
    }

    // Метод для извлечения текста из заголовка первой новости из списка новостей с проверкой открытого описания и клика по новости, если описание скрыто
    @Step("Извленение заголовка первой новости с проверкой на открытое описание и кликом, при закрытом описании на первую новость")
    public String extractTitle () {
        Allure.step("Извленение заголовка первой новости с проверкой на открытое описание и кликом, при закрытом описании на первую новость");
        if (!isElementVisible(allOf(newDescriptionMatcher, isDisplayed()))) {
            onView(newListMatcher)
                    .perform(actionOnItemAtPosition(0, click()));
        }
        return MaterialTextExtractor.extractText(allOf(
                titleNewMatcher,
                hasSibling(allOf(newDescriptionMatcher, isDisplayed()))
        ));
    }

    // Метод для извлечения текста из даты публикации первой новости из списка новостей с проверкой открытого описания и клика по новости, если описание скрыто
    @Step("Извленение даты публикации первой новости с проверкой на открытое описание и кликом, при закрытом описании на первую новость")
    public String extractDate() {
        Allure.step("Извленение даты публикации первой новости с проверкой на открытое описание и кликом, при закрытом описании на первую новость");
        if (!isElementVisible(allOf(newDescriptionMatcher, isDisplayed()))) {
            onView(newListMatcher)
                    .perform(actionOnItemAtPosition(0, click()));
        }
        return MaterialTextExtractor.extractText(allOf(
                pubDateMatcher,
                hasSibling(allOf(newDescriptionMatcher, isDisplayed()))
        ));
    }

    // Метод для извлечения текста из описания первой новости из списка новостей с проверкой открытого описания и клика по новости, если описание скрыто
    @Step("Извленение описания первой новости с проверкой на открытое описание и кликом, при закрытом описании на первую новость")
    public String extractDescription() {
        Allure.step("Извленение описания первой новости с проверкой на открытое описание и кликом, при закрытом описании на первую новость");
        if (!isElementVisible(allOf(newDescriptionMatcher, isDisplayed()))) {
            onView(newListMatcher)
                    .perform(actionOnItemAtPosition(0, click()));
        }
        return MaterialTextExtractor.extractText(allOf(
                newDescriptionMatcher, isDisplayed()
        ));
    }

    // Метод для извлечения текста из заголовка, даты публикации и описания  первой новости из списка новостей с проверкой открытого описания и клика по новости, если описание скрыто
    @Step("Извленение данных первой новости с проверкой на открытое описание и кликом, при закрытом описании на первую новость")
    public String[] extractData() {
        Allure.step("Извленение данных первой новости с проверкой на открытое описание и кликом, при закрытом описании на первую новость");
        if (!isElementVisible(allOf(newDescriptionMatcher, isDisplayed()))) {
            onView(newListMatcher)
                    .perform(actionOnItemAtPosition(0, click()));
        }
        return new String[]{
                MaterialTextExtractor.extractText(allOf(
                        titleNewMatcher,
                        hasSibling(allOf(newDescriptionMatcher, isDisplayed()))
                )),
                MaterialTextExtractor.extractText(allOf(
                        pubDateMatcher,
                        hasSibling(allOf(newDescriptionMatcher, isDisplayed()))
                )),
                MaterialTextExtractor.extractText(allOf(
                        newDescriptionMatcher, isDisplayed()
                ))
        };
    }
}
