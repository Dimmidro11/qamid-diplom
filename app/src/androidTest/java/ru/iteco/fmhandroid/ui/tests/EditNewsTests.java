package ru.iteco.fmhandroid.ui.tests;


import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ru.iteco.fmhandroid.ui.data.DataHelper.randomizeCategory;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import io.qameta.allure.android.rules.ScreenshotRule;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.EspressoIdlingResources;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AuthPage;
import ru.iteco.fmhandroid.ui.page.ControlPage;
import ru.iteco.fmhandroid.ui.page.EditOrCreateNewsPage;
import ru.iteco.fmhandroid.ui.page.MainPage;
import ru.iteco.fmhandroid.ui.page.NewsPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class EditNewsTests {
    private static MainPage staticMainPage;
    private MainPage mainPage;
    private NewsPage newsPage;
    private ControlPage controlPage;
    private EditOrCreateNewsPage editOrCreateNewsPage;

    @Rule
    public ActivityScenarioRule<AppActivity> activityScenarioRule = new ActivityScenarioRule<>(AppActivity.class);

    @Rule
    public TestRule rule = new ScreenshotRule();

    // Проверяем на какой странице находимся, если на странице авторизации - авторизуемся
    @BeforeClass
    public static void registerIdlingResourceAndLogin() {
        try (ActivityScenario<AppActivity> scenario = ActivityScenario.launch(AppActivity.class)) {
            IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
            AuthPage authPage = new AuthPage(false);
            staticMainPage = authPage.validLogin();
            IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
        }
    }

    // Настройка первоначального состояния для тестов
    @Before
    public void setup() {
        // Регистрируем IdlingResources для отслеживания команды ожидания
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);

        // Переходим со страницы Main на страницу News
        mainPage = new MainPage();
        newsPage = mainPage.goToNewsPageFromTextAllNews();

        // Переходим на страницу ControlPage нажатием на кнопку Edit News
        controlPage = newsPage.goToControlPage();

        // Отключаем IdlingResources
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
    }

    // Выходим из системы после всех тестов
    @AfterClass
    public static void unregisterIdlingResourceAndLogout() {
        try (ActivityScenario<AppActivity> scenario = ActivityScenario.launch(AppActivity.class)) {
            IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
            staticMainPage.logOut();
            IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
        }
    }


    // case-edit1 В меню редактирования новости отображаются: выпадающий список категорий, поле заголовка, поле даты публикации, поле времени публикации, поле описания, чекбокс Active, кнопки Save и Cancel
    @Test
    @DisplayName("case-edit1 В меню редактирования новости отображаются: выпадающий список категорий, поле заголовка, поле даты публикации, поле времени публикации, поле описания, чекбокс Active, кнопки Save и Cancel")
    public void shouldBeVisibleCategoryTitleDateTimeDescriptionActiveSaveCancel() {
        // Переходим на страницу редактирования первой новости в списке
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Проверяем поле категории на видимость
        editOrCreateNewsPage.getCategoryField().check(matches(isDisplayed()));

        // Проверяем поле заголовка на видимость
        editOrCreateNewsPage.getTitleField().check(matches(isDisplayed()));

        // Проверяем поле даты публикации на видимость
        editOrCreateNewsPage.getPublicationDateField().check(matches(isDisplayed()));

        // Проверяем поле времени публикации на видимость
        editOrCreateNewsPage.getPublicationTimeField().check(matches(isDisplayed()));

        // Проверяем поле описания на видимость
        editOrCreateNewsPage.getDescriptionField().check(matches(isDisplayed()));

        // Проверяем чекбокс Active на видимость
        editOrCreateNewsPage.getCheckboxActive().check(matches(isDisplayed()));

        // Провеярем кнопку Save на видиость
        editOrCreateNewsPage.getSaveEditOrCreateNewButton().check(matches(isDisplayed()));

        // Проверяем кнопку Cancel на видимость
        editOrCreateNewsPage.getCancelEditOrCreateNewButton().check(matches(isDisplayed()));
    }

    // case-edit2 В меню редактирования новости поля заполнены соответствующими выбранной новости данными
    @Test
    @DisplayName("case-edit2 В меню редактирования новости поля заполнены соответствующими выбранной новости данными")
    public void shouldFilledFieldsMatch() {
        // Сохраняем заголовок, дату публикации и описание новости из списка в переменную
        String[] extractedData = controlPage.extractData();

        // Переходим на страницу редактирования выбранной новости
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Сравниваем поля на странице редактирования с сохраненными данными
        editOrCreateNewsPage.checkEditingNewsFields(extractedData);
    }

    // case-edit3 В меню редактирования новости в выпадающем списке содержится 8 категорий
    @Test
    @DisplayName("case-edit3 В меню редактирования новости в выпадающем списке содержится 8 категорий")
    public void shouldBe8Categories() {
        // Переходим к редактированию первой новости
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Очищаем и открываем выпадающий список категорий нажатием на поле категорий, сверяем количество
        editOrCreateNewsPage.checkCountCategories();
    }

    // case-edit4 В меню редактирования новости поле Заголовок принимает кириллицу, латиницу, цифры и спецсимволы
    @Test
    @DisplayName("case-edit4 В меню редактирования новости поле Заголовок принимает кириллицу, латиницу, цифры и спецсимволы")
    public void shouldTitleGettingCyrillicLatinNumberAndSpecChar() {
        // Создаем тестовую строку состоящую из кириллицы, латиница, цифр и спецсимволов
        String testString = DataHelper.getStringRuLatNumSpec();

        // Переходим к редактированию первой новости
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Вводим тестовую строку в поле заголовка
        editOrCreateNewsPage.getTitleField().perform(clearText(), typeText(testString));

        // Сверяем введенный текст с тестовой строкой
        editOrCreateNewsPage.getTitleField().check(matches(withText(testString)));
    }

    // case-edit7 В меню редактирования новости поле Описание принимает кириллицу, латиницу, цифры и спецсимволы
    @Test
    @DisplayName("case-edit7 В меню редактирования новости поле Описание принимает кириллицу, латиницу, цифры и спецсимволы")
    public void shouldDescriptionGettingCyrillicLatinNumberAndSpecChar() {
        // Создаем тестовую строку состоящую из кириллицы, латиница, цифр и спецсимволов
        String testString = DataHelper.getStringRuLatNumSpec();

        // Переходим к редактированию первой новости
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Вводим тестовую строку в поле описания
        editOrCreateNewsPage.getDescriptionField().perform(clearText(), typeText(testString));
        editOrCreateNewsPage.getDescriptionField().check(matches(withText(testString)));
    }

    // case-edit8 В меню редактирования новости чекбокс Active кликабелен
    @Test
    @DisplayName("case-edit8 В меню редактирования новости чекбокс Active кликабелен")
    public void shouldCheckboxActiveBeClickable() {
        // Переходим к редактированию первой новости
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Проверяем кликабельность чекбокса
        editOrCreateNewsPage.getCheckboxActive().check(matches(isClickable()));

        // Проверяем включен ли чекбокс
        editOrCreateNewsPage.getCheckboxActive().check(matches(isEnabled()));
    }

    // case-edit9 В меню редактирования новости кнопка Save кликабельна
    @Test
    @DisplayName("case-edit9 В меню редактирования новости кнопка Save кликабельна")
    public void shouldButtonSaveBeClickable() {
        // Переходим к редактированию первой новости
        editOrCreateNewsPage = controlPage.goToEditNews();


        // Проверяем кликабельность кнопки Save
        editOrCreateNewsPage.getSaveEditOrCreateNewButton().check(matches(isClickable()));

        // Проверяем включена ли кнопка
        editOrCreateNewsPage.getSaveEditOrCreateNewButton().check(matches(isEnabled()));
    }

    // case-edit10 В меню редактирования новости кнопка Cancel кликабельна
    @Test
    @DisplayName("case-edit10 В меню редактирования новости кнопка Cancel кликабельна")
    public void shouldButtonCancelBeClickable() {
        // Переходим к редактированию первой новости
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Проверяем кликабельность кнопки
        editOrCreateNewsPage.getCancelEditOrCreateNewButton().check(matches(isClickable()));

        // Проверяем включена ли кнопка
        editOrCreateNewsPage.getCancelEditOrCreateNewButton().check(matches(isEnabled()));
    }

    // case-edit11 В меню редактирования новости кнопка Save изменяет текущую новость и возвращается к списку новостей
    @Test
    @DisplayName("case-edit11 В меню редактирования новости кнопка Save изменяет текущую новость и возвращается к списку новостей")
    public void shouldEditNewAndReturnToControlPage() {
        // Создаем тестовые данные: заголовок и описание
        String newTitle = DataHelper.getRandomString();
        String newDesc = DataHelper.getRandomString();

        // Извлекаем из первой новости заголовок и описание
        String extractTitle = controlPage.extractTitle();
        String extractDesc = controlPage.extractDescription();

        // Переходим к редактированию данный записи
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Заполняем поля тестовыми данными и кликаем на Save
        controlPage = editOrCreateNewsPage.createNew(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), newTitle, newDesc);

        // Проверяем на создание новости с тестовыми данными
        controlPage.checkAddedTitleNew(newTitle);

        // Проверяем на отсутствие измененной новости
        controlPage.checkExistTitleNew(extractTitle);
    }

    // case-edit12 В меню редактирования новости кнопка Cancel отображает список новостей без изменения новости
    @Test
    @DisplayName("case-edit12 В меню редактирования новости кнопка Cancel отображает список новостей без изменения новости")
    public void shouldNotEditNewAndReturnToControlPage() {
        // Создаем тестовые данные: заголовок и описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Извлекаем из первой новости заголовок
        String extractTitle = controlPage.extractTitle();

        // Переходим к редактированию данный записи
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Заполняем поля тестовыми данными и кликаем на Cancel
        controlPage = editOrCreateNewsPage.dontCreateNew(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);

        // Проверяем на отсутствие измененной новости
        controlPage.checkExistTitleNew(title);

        // Проверяем на присутствие в списке изначальной новости
        controlPage.checkAddedTitleNew(extractTitle);
    }

    // case-edit16 В меню редактирования новости невозможно сохранить новость с невыбранной категорией
    @Test
    @DisplayName("case-edit16 В меню редактирования новости невозможно сохранить новость с невыбранной категорией")
    public void shouldNotEditNewWithEmptyCategory() {
        // Создаем тестовые данные - заголовок и описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Переходим на страницу редактирования первой новости из списка
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Заполняем поля случайной категорией, тестовыми данными, текущей датой и временем, очищаем поле категории, кликаем Save и проверяем на ошибку поле категории
        editOrCreateNewsPage.createNewWithoutField(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getCategoryField());
    }

    // case-edit17 В меню редактирования новости невозможно сохранить новость с пустым заголовком
    @Test
    @DisplayName("case-edit17 В меню редактирования новости невозможно сохранить новость с пустым заголовком")
    public void shouldNotEditNewWithEmptyTitle() {
        // Создаем тестовые данные - заголовок и описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Переходим на страницу редактирования первой новости из списка
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Заполняем поля случайной категорией, тестовыми данными, текущей датой и временем, очищаем поле заголовка, кликаем Save и проверяем на ошибку поле категории
        editOrCreateNewsPage.createNewWithoutField(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getTitleField());
    }

    // case-edit18 В меню редактирования новости невозможно сохранить новость с пустым описанием
    @Test
    @DisplayName("case-edit18 В меню редактирования новости невозможно сохранить новость с пустым описанием")
    public void shouldNotEditNewWithEmptyDate() {
        // Создаем тестовые данные - заголовок и описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Переходим на страницу редактирования первой новости из списка
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Заполняем поля случайной категорией, тестовыми данными, текущей датой и временем, очищаем поле описания, кликаем Save и проверяем на ошибку поле категории
        editOrCreateNewsPage.createNewWithoutField(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getPublicationDateField());
    }

    // case-edit19 У измененной новости в Панели управления все поля соответствуют выбранным
    @Test
    @DisplayName("case-edit19 У измененной новости в Панели управления все поля соответствуют выбранным")
    public void shouldEditedNewFieldsMatch() {
        // Создаем тестовые данные - заголовок, описание и дату публикации не ранее текущего дня
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        int[] date = DataHelper.getRandomFutureDate();

        // Переходим к редактированию первой новости из списка
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Сохраняем новость со случайной категорией, тестовыми данными
        controlPage = editOrCreateNewsPage.createNew(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, date);

        // Проверяем выбраные поля на соответствие тестовым данным
        controlPage.checkCreatedNew(title, desc, date);
    }

    // case-edit20 У измененной новости в Панели управления дата создания новости соответствует текущей дате
    @Test
    @DisplayName("case-edit20 У измененной новости в Панели управления дата создания новости соответствует текущей дате")
    public void shouldEditedNewCreateDateMatch() {
        // Создаем тестовые данные - заголовок, описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Переходим к редактированию первой новости из списка
        editOrCreateNewsPage = controlPage.goToEditNews();

        // Сохраняем новость со случайной категорией, тестовыми данными
        controlPage = editOrCreateNewsPage.createNew(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);

        // У сохраненной новости сверяем дату создания с текущей датой
        controlPage.checkNewsCreatedDate(title);
    }
}
