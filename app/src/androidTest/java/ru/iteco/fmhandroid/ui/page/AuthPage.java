package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;

import java.util.Objects;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.data.ToastMatcher;

public class AuthPage {
    private final ViewInteraction loginField = onView(withHint("Login"));
    private final ViewInteraction passwordField = onView(withHint("Password"));
    private final ViewInteraction enterButton = onView(withId(R.id.enter_button));

    public AuthPage(boolean strict) {
        if (!strict) {
            loginField.check(matches(isDisplayed()));
        }
    }

    public AuthPage() {

    }

    public ViewInteraction getLoginField() {
        return loginField;
    }

    private void fillLoginField(String login) {
        loginField.perform(typeText(login));
    }

    private void fillPasswordField(String pass) {
        passwordField.check(matches(isDisplayed()));
        passwordField.perform(typeText(pass));
    }

    private void clickEnter() {
        enterButton.check(matches(isDisplayed()));
        enterButton.perform(click());
    }

    public void checkLoginField() {
        loginField.check(matches(isDisplayed()));
    }

    public boolean isLoginFieldVisible() {
        try {
            loginField.check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public MainPage validLogin() {
        if (!isLoginFieldVisible()) {
            return new MainPage();
        }

        fillLoginField(DataHelper.getCorrectLogin());
        fillPasswordField(DataHelper.getCorrectPassword());
        clickEnter();

        return new MainPage();
    }

    public void invalidLogin(String login, String pass) {
        fillLoginField(login);

        fillPasswordField(pass);

        clickEnter();

        if (Objects.equals(login, "") || Objects.equals(pass, "")) {
//            onView(withText(R.string.empty_login_or_password))
//                    .inRoot(new ToastMatcher())
//                    .check(matches(isDisplayed()));
            loginField.check(matches(isDisplayed()));
        } else {
//            onView(withText(R.string.error))
//                    .inRoot(new ToastMatcher())
//                    .check(matches(isDisplayed()));
            loginField.check(matches(isDisplayed()));
        }
    }
}
