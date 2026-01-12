package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;

public class MainPage {
    private final ViewInteraction imageView = onView(withId(R.id.trademark_image_view));
    private final ViewInteraction profileButton = onView(withId(R.id.authorization_image_button));
    private final ViewInteraction logOutButton = onView(allOf(withId(android.R.id.title), withText("Log out")));
    private final ViewInteraction menuButton = onView(withId(R.id.main_menu_image_button));
    private final ViewInteraction mainButton = onView(allOf(withId(android.R.id.title), withText(R.string.main)));
    private final ViewInteraction newsButton = onView(allOf(withId(android.R.id.title), withText(R.string.news)));
    private final ViewInteraction aboutButton = onView(allOf(withId(android.R.id.title), withText(R.string.about)));
    private final ViewInteraction quoteButton = onView(withId(R.id.our_mission_image_button));
    private final ViewInteraction allNewsButton = onView(withId(R.id.all_news_text_view));

    public MainPage() {
        checkElement(imageView);
    }

    public void checkElement(ViewInteraction element) {
        element.check(matches(isDisplayed()));
    }

    public void checkMenuElements() {
        checkElement(mainButton);
        checkElement(newsButton);
        checkElement(aboutButton);
    }

    public ViewInteraction getMenuButton() {
        return menuButton;
    }

    public ViewInteraction getProfileButton() {
        return profileButton;
    }

    public ViewInteraction getLogOutButton() {
        return logOutButton;
    }

    public void clickButton(ViewInteraction button) {
        button.check(matches(isDisplayed()));
        button.perform(click());
    }


    public AuthPage logOut() {
        clickButton(profileButton);
        clickButton(logOutButton);

        return new AuthPage();
    }

    public NewsPage goToNews() {
        clickButton(menuButton);
        clickButton(newsButton);

        return new NewsPage();
    }

    public NewsPage goToNewsPageFromTextAllNews() {
        clickButton(allNewsButton);

        return new NewsPage();
    }

    public AboutPage goToAbout() {
        clickButton(menuButton);
        clickButton(aboutButton);

        return new AboutPage();
    }

    public QuotePage goToQuote() {
        clickButton(quoteButton);

        return new QuotePage();
    }

    public MainPage goToMain() {
        clickButton(menuButton);
        clickButton(mainButton);

        return new MainPage();
    }
}
