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
import static ru.iteco.fmhandroid.ui.data.CustomViewAssertions.isRecycleView;
import static ru.iteco.fmhandroid.ui.data.DataHelper.formatDateFromArray;
import static ru.iteco.fmhandroid.ui.data.DataHelper.getCurrentDate;
import static ru.iteco.fmhandroid.ui.data.DataHelper.isElementVisible;

import android.view.View;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;

import org.hamcrest.Matcher;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.data.MaterialTextExtractor;

public class ControlPage {
    private final Matcher<View> titleNewMatcher = withId(R.id.news_item_title_text_view);
    private final Matcher<View> newItemCardMatcher = withId(R.id.news_item_material_card_view);
    private final Matcher<View> newListMatcher = withId(R.id.news_list_recycler_view);
    private final Matcher<View> pubDateMatcher = withId(R.id.news_item_publication_date_text_view);
    private final Matcher<View> createDateMatcher = withId(R.id.news_item_create_date_text_view);
    private final Matcher<View> newDescriptionMatcher = withId(R.id.news_item_description_text_view);
    private final Matcher<View> deleteNewFromListMatcher = withId(R.id.delete_news_item_image_view);
    private final Matcher<View> editNewMatcher = withId(R.id.edit_news_item_image_view);
    private final ViewInteraction controlText = onView(withText("Control panel"));
    private final ViewInteraction titleNew = onView(titleNewMatcher);
    private final ViewInteraction sortButton = onView(withId(R.id.sort_news_material_button));
    private final ViewInteraction filterButton = onView(withId(R.id.filter_news_material_button));
    private final ViewInteraction filterNewsCheckboxActive = onView(withId(R.id.filter_news_active_material_check_box));
    private final ViewInteraction filterNewsCheckboxNotActive = onView(withId(R.id.filter_news_inactive_material_check_box));
    private final ViewInteraction categoryField = onView(withId(com.google.android.material.R.id.text_input_end_icon));
    private final ViewInteraction acceptFilterButton = onView(withId(R.id.filter_button));
    private final ViewInteraction cancelConfirmationButton = onView(allOf(withId(android.R.id.button2), withText("Cancel")));
    private final ViewInteraction acceptConfirmationButton = onView(allOf(withId(android.R.id.button1), withText("OK")));
    private final ViewInteraction editNewFromList = onView(editNewMatcher);
    private final ViewInteraction newsList = onView(newListMatcher);
    private final ViewInteraction addNewButton = onView(allOf(withId(R.id.add_news_image_view), withContentDescription("Add news button")));


    public ControlPage() {
        controlText.check(matches(isDisplayed()));
    }

    public ViewInteraction getSortButton() {
        return sortButton;
    }

    public ViewInteraction getTitleNew() {
        return titleNew;
    }

    public ViewInteraction getNewsList() {
        return newsList;
    }

    public Matcher<View> getMatcherTitle(String title) {
        return allOf(
                newItemCardMatcher,
                hasDescendant(allOf(
                        titleNewMatcher,
                        withText(title)
                ))
        );
    }

    public ViewInteraction getTitleText(String title) {
        return onView(allOf(titleNewMatcher, withText(title)));
    }

    public void clickButton(ViewInteraction button) {
        button.perform(click());
    }

    public EditOrCreateNewsPage goToCreateNews() {
        clickButton(addNewButton);

        return new EditOrCreateNewsPage();
    }

    public EditOrCreateNewsPage goToEditNews() {
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

    public EditOrCreateNewsPage goToEditNews(String title) {
        Matcher<View> newsCard = getMatcherTitle(title);

        scrollAndClickNew(newsCard);

        onView(allOf(
                editNewMatcher,
                isDescendantOfA(newsCard)
        )).perform(click());

        return new EditOrCreateNewsPage();
    }

    public void checkAddedTitleNew(String title) {
        getNewsList().check(isRecycleView());
        getTitleText(title).check(matches(isDisplayed()));
    }

    public void scrollAndClickNew(Matcher<View> newsCard) {
        onView(newListMatcher)
                .perform(RecyclerViewActions.scrollTo(newsCard));
        onView(newsCard).perform(click());
    }

    public void checkCreatedNew(String title, String description, int[] date) {

        Matcher<View> newsCard = getMatcherTitle(title);

        scrollAndClickNew(newsCard);

        onView(allOf(
                pubDateMatcher,
                isDescendantOfA(newsCard)
        )).check(DataHelper.createTextAssertion(formatDateFromArray(date)));

        onView(allOf(
                newDescriptionMatcher,
                isDescendantOfA(newsCard)
        )).check(DataHelper.createTextAssertion(description));

    }

    public void checkNewsCreatedDate(String title) {

        Matcher<View> newsCard = getMatcherTitle(title);

        scrollAndClickNew(newsCard);

        onView(allOf(
                createDateMatcher,
                isDescendantOfA(newsCard)
        )).check(DataHelper.createTextAssertion(getCurrentDate()));
    }

    public void deleteNew(Boolean confirm, String title) {
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

    public String extractTitle () {
        if (!isElementVisible(allOf(newDescriptionMatcher, isDisplayed()))) {
            onView(newListMatcher)
                    .perform(actionOnItemAtPosition(0, click()));
        }
        return MaterialTextExtractor.extractText(allOf(
                titleNewMatcher,
                hasSibling(allOf(newDescriptionMatcher, isDisplayed()))
        ));
    }

    public String extractDate() {
        return MaterialTextExtractor.extractText(allOf(
                pubDateMatcher,
                hasSibling(allOf(newDescriptionMatcher, isDisplayed()))
        ));
    }

    public String extractDescription() {
        if (!isElementVisible(allOf(newDescriptionMatcher, isDisplayed()))) {
            onView(newListMatcher)
                    .perform(actionOnItemAtPosition(0, click()));
        }
        return MaterialTextExtractor.extractText(allOf(
                newDescriptionMatcher, isDisplayed()
        ));
    }

    public String[] extractData() {
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
