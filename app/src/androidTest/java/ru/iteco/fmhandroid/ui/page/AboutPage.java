package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;

public class AboutPage {
    // Набор элементов
    private ViewInteraction versionText = onView(withId(R.id.about_version_title_text_view));
    private ViewInteraction backButton = onView(withId(R.id.about_back_image_button));

    // Конструктор с проверкой нахождения на странице About по видимости текста с версией
    public AboutPage() {
        Allure.step("Проверка страницы About в конструкторе класса");
        try {
            versionText.check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            throw new AssertionError(
                    "Страница AboutPage не открыта\n" +
                            "Проверка в конструкторе класса на видимость элемента с id about_version_title_text_view не пройдена");
        }
    }

    // Геттеры
    public ViewInteraction getBackButton() {
        return backButton;
    }

    // Метод нажатия на кнопку Назад
    @Step("Нажатие на кнопку назад")
    public MainPage clickBackButton() {
        Allure.step("Нажатие на кнопку назад");
        backButton.check(matches(isDisplayed()));
        backButton.perform(click());

        return new MainPage();
    }
}
