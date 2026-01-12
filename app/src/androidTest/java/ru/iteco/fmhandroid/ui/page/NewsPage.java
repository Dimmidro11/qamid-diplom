package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;

public class NewsPage extends MainPage {

    private final ViewInteraction newsText = onView(
                    allOf(withText("News"),
                    withParent(withParent(withId(R.id.container_list_news_include)))));
    private final ViewInteraction filterButton = onView(withId(R.id.sort_news_material_button));
    private final ViewInteraction sortButton = onView(withId(R.id.filter_news_material_button));
    private final ViewInteraction acceptFilterButton = onView(withId(R.id.filter_button));
    private final ViewInteraction cancelFilterButton = onView(withId(R.id.cancel_button));
    private final ViewInteraction editNewsButton = onView(withId(R.id.edit_news_material_button));
    private final ViewInteraction refreshNewsListButton = onView(withId(R.id.news_retry_material_button));
    private final ViewInteraction emptyNewsText = onView(withId(R.id.empty_news_list_text_view));

    public NewsPage() {
        checkElement(newsText);
    }

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

    public void clickButton(ViewInteraction button) {
        button.perform(click());
    }

    public ControlPage goToControlPage() {
        clickButton(editNewsButton);

        return new ControlPage();
    }
}
