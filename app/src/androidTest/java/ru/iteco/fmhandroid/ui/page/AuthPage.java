package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anyOf;

import androidx.test.espresso.ViewInteraction;

import java.util.Objects;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.utils.ToastMatcher;

public class AuthPage {
    // Набор элементов
    private final ViewInteraction loginField = onView(anyOf(withHint("Login"), withHint("Логин")));
    private final ViewInteraction passwordField = onView(anyOf(withHint("Password"), withHint("Пароль")));
    private final ViewInteraction enterButton = onView(withId(R.id.enter_button));


    // Конструктор с проверкой нахождения на странице Auth по видимости поля логина с флагом проверка/нет проверки
    public AuthPage(boolean strict) {
        if (strict) {
            Allure.step("Проверка страницы AuthPage в конструкторе");
            checkLoginField();
        }
    }

    // Конструктор с проверкой нахождения на странице Auth по видимости поля логина
    public AuthPage() {
        Allure.step("Проверка страницы AuthPage в конструкторе");
        checkLoginField();
    }

    // Геттеры
    public ViewInteraction getLoginField() {
        return loginField;
    }

    // Метод заполнения поля логина текстом
    @Step("Заполнение поля Логин")
    private void fillLoginField(String login) {
        Allure.step("Заполнение поля Логин");
        loginField.perform(typeText(login));
    }

    // Метод заполнения поля пароля текстом, с проверкой видимости поля
    @Step("Заполнение поля Пароль с проверкой видимости")
    private void fillPasswordField(String pass) {
        Allure.step("Заполнение поля Пароль с проверкой видимости");
        passwordField.check(matches(isDisplayed()));
        passwordField.perform(typeText(pass));
    }

    // Метод нажатия на кнопку Sign in, с проверкой ее видимости
    @Step("Нажатие на кнопку Войти с проверкой видимости")
    private void clickEnter() {
        Allure.step("Нажатие на кнопку Война с проверкой видимости");
        enterButton.check(matches(isDisplayed()));
        enterButton.perform(click());
    }

    // Метод проверки поля логина на видимость
    @Step("Проверка поля Логин на видимость")
    public void checkLoginField() {
        Allure.step("Проверка поля Логин на видимость");
        loginField.check(matches(isDisplayed()));
    }

    // Метод для возврата булева значения видимости поля логина
    public boolean isLoginFieldVisible() {
        try {
            checkLoginField();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Метод заполнения полей логина и пароля валидными данными и клика по кнопке входа
    @Step("Попытка залогиниться с валидными данными")
    public MainPage validLogin() {
        Allure.step("Попытка залогиниться с валидными данными");
        if (!isLoginFieldVisible()) {
            return new MainPage();
        }

        fillLoginField(DataHelper.getCorrectLogin());
        fillPasswordField(DataHelper.getCorrectPassword());
        clickEnter();

        return new MainPage();
    }

    // Метод для заполнения полей логина и пароля заданынми значениями с проверкой ошибок
    @Step("Заполнение полей Логна и Пароля данными с проверкой на ошибки")
    public void invalidLogin(String login, String pass) {
        fillLoginField(login);

        fillPasswordField(pass);

        clickEnter();

        Allure.step("Проверка на ошибки полей");
        if (Objects.equals(login, "") || Objects.equals(pass, "")) {
            onView(withText(R.string.empty_login_or_password))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            loginField.check(matches(isDisplayed()));
        } else {
            onView(withText(R.string.error))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            loginField.check(matches(isDisplayed()));
        }
    }
}
