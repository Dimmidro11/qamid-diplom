package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static ru.iteco.fmhandroid.ui.data.DataHelper.createTextAssertion;

import android.view.View;
import android.widget.DatePicker;

import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.utils.ToastMatcher;

public class EditOrCreateNewsPage {
    // Набор матчеров
    private final Matcher<View> toastErrorMatcher = withText(R.string.empty_fields);

    //Набор элементов
    private final ViewInteraction createText = onView(withId(R.id.custom_app_bar_title_text_view));
    private final ViewInteraction okDateOrTime = onView(allOf(withId(android.R.id.button1), withText("OK")));
    private final ViewInteraction saveEditOrCreateNewButton = onView(allOf(withId(R.id.save_button), withContentDescription("Save")));
    private final ViewInteraction cancelEditOrCreateNewButton = onView(allOf(withId(R.id.cancel_button), withContentDescription("Cancel")));
    private final ViewInteraction categoryField = onView(withId(R.id.news_item_category_text_auto_complete_text_view));
    private final ViewInteraction titleField = onView(withId(R.id.news_item_title_text_input_edit_text));
    private final ViewInteraction publicationDateField = onView(withId(R.id.news_item_publish_date_text_input_edit_text));
    private final ViewInteraction publicationTimeField = onView(withId(R.id.news_item_publish_time_text_input_edit_text));
    private final ViewInteraction descriptionField = onView(withId(R.id.news_item_description_text_input_edit_text));
    private final ViewInteraction checkboxActive = onView(withId(R.id.switcher));
    private final ViewInteraction cancelConfirmationButton = onView(allOf(withId(android.R.id.button2)));
    private final ViewInteraction acceptConfirmationButton = onView(allOf(withId(android.R.id.button1)));
    private final ViewInteraction categoryError = onView(allOf(withId(R.id.text_input_start_icon), isDescendantOfA(withId(R.id.news_item_category_text_input_layout))));
    private final ViewInteraction titleError = onView(allOf(withId(R.id.text_input_end_icon), isDescendantOfA(withId(R.id.news_item_title_text_input_layout))));
    private final ViewInteraction dateError = onView(allOf(withId(R.id.text_input_end_icon), isDescendantOfA(withId(R.id.news_item_create_date_text_input_layout))));
    private final ViewInteraction timeError = onView(allOf(withId(R.id.text_input_end_icon), isDescendantOfA(withId(R.id.news_item_publish_time_text_input_layout))));
    private final ViewInteraction descriptionError = onView(allOf(withId(R.id.text_input_end_icon), isDescendantOfA(withId(R.id.news_item_description_text_input_layout))));

    // Мапа для полей новости с соответствующими ошибками пустого поля
    private final Map<ViewInteraction, ViewInteraction> fieldToErrorMap = new HashMap<>();

    // Массив с категорями в выпадающем списке поля категорий
    private final String[] categoriesArray = {"Объявление", "День рождения", "Зарплата", "Профсоюз", "Праздник", "Массаж", "Благодарность", "Нужна помощь"};

    // Конструктор с проверкой нахождения на странице EditOrCreate по видимости элемента и заполнением мапы значениями
    public EditOrCreateNewsPage() {
        Allure.step("Проверка страницы EditOrCreatePage в конструкторе класса");
        createText.check(matches(isDisplayed()));
        {
            fieldToErrorMap.put(categoryField, categoryError);
            fieldToErrorMap.put(titleField, titleError);
            fieldToErrorMap.put(publicationDateField, dateError);
            fieldToErrorMap.put(publicationTimeField, timeError);
            fieldToErrorMap.put(descriptionField, descriptionError);
        }
    }

    // Геттеры
    public ViewInteraction getOkDateOrTime() {
        return  okDateOrTime;
    }

    public ViewInteraction getCreateText() {
        return createText;
    }

    public String[] getCategoriesArray() {
        return categoriesArray;
    }

    public ViewInteraction getCancelEditOrCreateNewButton() {
        return cancelEditOrCreateNewButton;
    }

    public ViewInteraction getCategoryField() {
        return categoryField;
    }

    public ViewInteraction getTitleField() {
        return titleField;
    }

    public ViewInteraction getPublicationDateField() {
        return publicationDateField;
    }

    public ViewInteraction getPublicationTimeField() {
        return publicationTimeField;
    }

    public ViewInteraction getDescriptionField() {
        return descriptionField;
    }

    public ViewInteraction getCheckboxActive() {
        return checkboxActive;
    }

    public ViewInteraction getCancelConfirmationButton() {
        return cancelConfirmationButton;
    }

    public ViewInteraction getAcceptConfirmationButton() {
        return acceptConfirmationButton;
    }

    public ViewInteraction getSaveEditOrCreateNewButton() {
        return saveEditOrCreateNewButton;
    }

    // Метод клика по элементу
    @Step("Клик по элементу")
    public void clickButton(ViewInteraction button) {
        Allure.step("Клик по элементу");
        button.perform(click());
    }

    // Метод клика по категории в выпадающем списке поля категории
    @Step("Клик по категории из выпадающего списка")
    public void clickDescription(String category) {
        Allure.step("Клик по категории из выпадающего списка");
        onData(is(category)).inRoot(isPlatformPopup()).perform(click());
    }

    // Метод пересчета категорий в выпадающем списке категорий
    @Step("Подсчет количества категорий в выпадающем списке")
    public int countCategories() {
        Allure.step("Подсчет количества категорий в выпадающем списке");
        int itemCount = 0;
        try {
            while (true) {
                onData(anything())
                        .atPosition(itemCount)
                        .inRoot(isPlatformPopup())
                        .check(matches(isDisplayed()));
                itemCount++;
            }
        } catch (Exception e) {
        }
        return itemCount;
    }

    // Метод для сравнения количества категорий в списке с данными в хелпере
    @Step("Сравнение количества категорий в списке и из хелпера")
    public void checkCountCategories() {
        Allure.step("Сравнение количества категорий в списке и из хелпера");
        categoryField.perform(clearText());
        clickButton(categoryField);
        assertThat(countCategories(), is(DataHelper.getCountCategory()));
    }

    // Метод заполнения полей новости заданными данными
    @Step("Заполнение полей с заданной датой")
    public void fillField(String category, String titleText, String description, int day, int month, int year) {
        Allure.step("Заполнение полей с заданной датой");
        categoryField.perform(clearText(), click());
        clickDescription(category);
        titleField.perform(clearText(), typeText(titleText));
        closeSoftKeyboard();
        publicationDateField.perform(click());
        setRandomFutureDate(year, month, day);
        okDateOrTime.perform(click());
        publicationTimeField.perform(click());
        okDateOrTime.perform(click());
        descriptionField.perform(clearText(), typeText(description));
        closeSoftKeyboard();
    }

    // Метод заполнения полей категории, заголовка, описания заданными данными, дата и время выбираются текущие
    @Step("Заполнение полей с текущей датой")
    public void fillField(String category, String titleText, String description) {
        Allure.step("Заполнение полей с текущей датой");
        categoryField.perform(clearText(), click());
        clickDescription(category);
        titleField.perform(clearText(), typeText(titleText));
        closeSoftKeyboard();
        publicationDateField.perform(click());
        okDateOrTime.perform(click());
        publicationTimeField.perform(click());
        okDateOrTime.perform(click());
        descriptionField.perform(clearText(), typeText(description));
        closeSoftKeyboard();
    }

    // Метод для создания новости с текущей датоей и временем
    @Step("Создание новости с текущей датой и временем")
    public ControlPage createNew(String category, String titleText, String description) {
        Allure.step("Создание новости с текущей датой и временем");
        fillField(category, titleText, description);
        Allure.step("Нажатие на Сохранить");
        clickButton(saveEditOrCreateNewButton);

        return new ControlPage();
    }

    // Метод создания новости с заданным данными
    @Step("Создание новости с заданной датой")
    public ControlPage createNew(String category, String titleText, String description, int[] date) {
        Allure.step("Создание новости с заданной датой");
        fillField(category, titleText, description, date[2], date[1], date[0]);
        Allure.step("Нажатие на сохранить");
        clickButton(saveEditOrCreateNewButton);

        return new ControlPage();
    }

    // Метод заполнения полей новости, оставляя одно поле пустым, кликом на сохранение новости и проверкой соответствующей проверкой
    @Step("Заполнение полей, оставляя одно поле пустым с проверкой на ошибку")
    public void createNewWithoutField(String category, String titleText, String description, ViewInteraction deletedField) {
        Allure.step("Заполнение полей, оставляя одно поле пустым с проверкой на ошибку");
        fillField(category, titleText, description);
        Allure.step("Очищение выбранного поля");
        deletedField.perform(clearText());
        closeSoftKeyboard();
        Allure.step("Нажатие Сохранить");
        clickButton(saveEditOrCreateNewButton);
        Allure.step("Проверка на ошибку");
        onView(toastErrorMatcher)
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
        ViewInteraction errorField = fieldToErrorMap.get(deletedField);
        if (errorField != null) {
            errorField.check(matches(isDisplayed()));
        }
    }

    // Метод для заполнения полей новости, с кликом на отмену и подтверждением
    @Step("Заполнение полей новости с отменой сохранения")
    public ControlPage dontCreateNew(String category, String titleText, String description) {
        Allure.step("Заполнение полей новости с отменой сохранения");
        fillField(category, titleText, description);
        Allure.step("Нажатие Отмена");
        clickButton(cancelEditOrCreateNewButton);
        Allure.step("Подтверждение отмены");
        clickButton(acceptConfirmationButton);

        return new ControlPage();
    }

    // Метод заполнения поля даты случайной будущей датой
    @Step("Заполнение поля даты случайной будущей датой")
    public void setRandomFutureDate(int year, int month, int day) {
        Allure.step("Заполнение поля даты случайной будущей датой");
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(setDate(year, month, day));
    }

    // Метод проверки корректности заполненных полей редактируемой новости по заголовку, дате и описанию
    @Step("Проверка полей заголовка, даты и описания редактируемой записи (переменные раздельные)")
    public void checkEditingNewsFields(String title, String date, String description) {
        Allure.step("Проверка полей заголовка, даты и описания редактируемой записи (переменные раздельные)");
        getTitleField().check(createTextAssertion(title));
        getPublicationDateField().check(createTextAssertion(date));
        getDescriptionField().check(createTextAssertion(description));
    }

    // Метод проверки корректности заполненных полей редактируемой новости по наборе данных
    @Step("Проверка полей заголовка, даты и описания редактируемой записи (переменные в массиве)")
    public void checkEditingNewsFields(String[] data) {
        Allure.step("Проверка полей заголовка, даты и описания редактируемой записи (переменные в массиве)");
        getTitleField().check(createTextAssertion(data[0]));
        getPublicationDateField().check(createTextAssertion(data[1]));
        getDescriptionField().check(createTextAssertion(data[2]));
    }
}
