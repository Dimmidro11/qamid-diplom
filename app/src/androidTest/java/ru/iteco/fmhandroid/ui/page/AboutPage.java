package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.allOf;

import android.view.View;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;

public class AboutPage extends MainPage {
    private ViewInteraction versionText = onView(withId(R.id.about_version_title_text_view));
    private ViewInteraction backButton = onView(withId(R.id.about_back_image_button));

    public AboutPage() {
        versionText.check(matches(isDisplayed()));
    }

    public ViewInteraction getBackButton() {
        return backButton;
    }

    public MainPage clickBackButton() {
        backButton.check(matches(isDisplayed()));
        backButton.perform(click());

        return new MainPage();
    }
}
