package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import static ru.iteco.fmhandroid.ui.data.DataHelper.createTextAssertion;

import android.service.autofill.FieldClassification;
import android.view.View;
import android.widget.DatePicker;

import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;

import java.util.HashMap;
import java.util.Map;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.data.ToastMatcher;

public class EditOrCreateNewsPage {
    private final Matcher<View> toastErrorMatcher = withText(R.string.empty_fields);
    private final ViewInteraction createText = onView(withId(R.id.custom_app_bar_title_text_view));
    private final ViewInteraction okDateOrTime = onView(allOf(withId(android.R.id.button1), withText("OK")));
    private final ViewInteraction saveEditOrCreateNewButton = onView(allOf(withId(R.id.save_button), withText("Save"), withContentDescription("Save")));
    private final ViewInteraction cancelEditOrCreateNewButton = onView(allOf(withId(R.id.cancel_button), withText("Cancel"), withContentDescription("Cancel")));
    private final ViewInteraction categoryField = onView(withId(R.id.news_item_category_text_auto_complete_text_view));
    private final ViewInteraction titleField = onView(withId(R.id.news_item_title_text_input_edit_text));
    private final ViewInteraction publicationDateField = onView(withId(R.id.news_item_publish_date_text_input_edit_text));
    private final ViewInteraction publicationTimeField = onView(withId(R.id.news_item_publish_time_text_input_edit_text));
    private final ViewInteraction descriptionField = onView(withId(R.id.news_item_description_text_input_edit_text));
    private final ViewInteraction checkboxActive = onView(withId(R.id.switcher));
    private final ViewInteraction cancelConfirmationButton = onView(allOf(withId(android.R.id.button2), withText("Cancel")));
    private final ViewInteraction acceptConfirmationButton = onView(allOf(withId(android.R.id.button1), withText("OK")));
    private final ViewInteraction categoryError = onView(allOf(withId(R.id.text_input_start_icon), isDescendantOfA(withId(R.id.news_item_category_text_input_layout))));
    private final ViewInteraction titleError = onView(allOf(withId(R.id.text_input_end_icon), isDescendantOfA(withId(R.id.news_item_title_text_input_layout))));
    private final ViewInteraction dateError = onView(allOf(withId(R.id.text_input_end_icon), isDescendantOfA(withId(R.id.news_item_create_date_text_input_layout))));
    private final ViewInteraction timeError = onView(allOf(withId(R.id.text_input_end_icon), isDescendantOfA(withId(R.id.news_item_publish_time_text_input_layout))));
    private final ViewInteraction descriptionError = onView(allOf(withId(R.id.text_input_end_icon), isDescendantOfA(withId(R.id.news_item_description_text_input_layout))));
    private final Map<ViewInteraction, ViewInteraction> fieldToErrorMap = new HashMap<>();
    private final String[] categoriesArray = {"Объявление", "День рождения", "Зарплата", "Профсоюз", "Праздник", "Массаж", "Благодарность", "Нужна помощь"};

    public EditOrCreateNewsPage() {
        createText.check(matches(isDisplayed()));
        {
            fieldToErrorMap.put(categoryField, categoryError);
            fieldToErrorMap.put(titleField, titleError);
            fieldToErrorMap.put(publicationDateField, dateError);
            fieldToErrorMap.put(publicationTimeField, timeError);
            fieldToErrorMap.put(descriptionField, descriptionError);
        }
    }
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

    public void clickButton(ViewInteraction button) {
        button.perform(click());
    }

    public void clickDescription(String category) {
        onData(is(category)).inRoot(isPlatformPopup()).perform(click());
    }

    public int countCategories() {
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

    public void checkCountCategories() {
        categoryField.perform(clearText());
        clickButton(categoryField);
        assertThat(countCategories(), is(DataHelper.getCountCategory()));
    }

    public void fillField(String category, String titleText, String description, int day, int month, int year) {
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

    public void fillField(String category, String titleText, String description) {
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

    public ControlPage createNew(String category, String titleText, String description) {
        fillField(category, titleText, description);
        clickButton(saveEditOrCreateNewButton);

        return new ControlPage();
    }

    public ControlPage createNew(String category, String titleText, String description, int[] date) {
        fillField(category, titleText, description, date[2], date[1], date[0]);
        clickButton(saveEditOrCreateNewButton);

        return new ControlPage();
    }

    public void createNewWithoutField(String category, String titleText, String description, ViewInteraction deletedField) {
        fillField(category, titleText, description);
        deletedField.perform(clearText());
        closeSoftKeyboard();
        clickButton(saveEditOrCreateNewButton);
//        onView(toastErrorMatcher)
//                .inRoot(new ToastMatcher())
//                .check(matches(isDisplayed()));
        ViewInteraction errorField = fieldToErrorMap.get(deletedField);
        if (errorField != null) {
            errorField.check(matches(isDisplayed()));
        }
    }

    public ControlPage dontCreateNew(String category, String titleText, String description) {
        fillField(category, titleText, description);
        clickButton(cancelEditOrCreateNewButton);
        clickButton(acceptConfirmationButton);

        return new ControlPage();
    }

    public void setRandomFutureDate(int year, int month, int day) {
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(setDate(year, month, day));
    }

    public void checkEditingNewsFields(String title, String date, String description) {
        getTitleField().check(createTextAssertion(title));
        getPublicationDateField().check(createTextAssertion(date));
        getDescriptionField().check(createTextAssertion(description));
    }

    public void checkEditingNewsFields(String[] data) {
        getTitleField().check(createTextAssertion(data[0]));
        getPublicationDateField().check(createTextAssertion(data[1]));
        getDescriptionField().check(createTextAssertion(data[2]));
    }
}
