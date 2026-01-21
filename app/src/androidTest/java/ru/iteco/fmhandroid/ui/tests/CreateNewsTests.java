package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
import ru.iteco.fmhandroid.ui.utils.CustomViewAssertions;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class CreateNewsTests {
    private static MainPage staticMainPage;
    private MainPage mainPage;
    private NewsPage newsPage;
    private ControlPage controlPage;
    private EditOrCreateNewsPage editOrCreateNewsPage;

    @Rule
    public ActivityScenarioRule<AppActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Rule
    public TestRule rule = new ScreenshotRule();

    @BeforeClass
    public static void registerIdlingResourceAndLogin() {
        // Проверяем на какой странице находимся, если на странице авторизации - авторизуемся
        try (ActivityScenario<AppActivity> scenario = ActivityScenario.launch(AppActivity.class)) {
            IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
            AuthPage authPage = new AuthPage(false);
            staticMainPage = authPage.validLogin();
            IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
        }
    }

    // Настройка первоначального состоянии для всех тестов
    @Before
    public void setup() {
        // Регистрируем IdlingResources для отслеживания команды ожидания
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);

        // Переходим с Main страницы на News нажатием на кнопку "All news"
        mainPage = new MainPage();
        newsPage = mainPage.goToNewsPageFromTextAllNews();

        // Переходим на страницу ControlPage
        controlPage = newsPage.goToControlPage();

        // Переходим на страницу создания новости
        editOrCreateNewsPage = controlPage.goToCreateNews();

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


    // case-create1 В меню создания новости отображается: выпадающий список категорий, поле заголовка, поле даты публикации, поле времени публикации, поле описания, чекбокс Active, кнопки Save и Cancel
    @Test
    @DisplayName("case-create1 В меню создания новости отображается: выпадающий список категорий, поле заголовка, поле даты публикации, поле времени публикации, поле описания, чекбокс Active, кнопки Save и Cancel")
    public void shouldBeVisibleCategoryTitleDateTimeDescriptionActiveSaveCancel() {
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

    // case-create2 В меню создания новости в выпадающем списке 8 категорий
    @Test
    @DisplayName("case-create2 В меню создания новости в выпадающем списке 8 категорий")
    public void shouldBe8Categories() {
        // Открываем выпадающий список категорий нажатием на поле категорий
        editOrCreateNewsPage.clickButton(editOrCreateNewsPage.getCategoryField());

        // Сверяем количество категорий в выпадающем списке с количеством в DataHelper
        editOrCreateNewsPage.checkCountCategories();
    }

    // case-create3 В меню создания новости поле Заголовок принимает кириллицу, латиницу, цифры и спецсимволы
    @Test
    @DisplayName("case-create3 В меню создания новости поле Заголовок принимает кириллицу, латиницу, цифры и спецсимволы")
    public void shouldTitleGettingCyrillicLatinNumberAndSpecChar() {
        // Создаем тестовую строку состоящую из кириллицы, латиница, цифр и спецсимволов
        String testString = DataHelper.getStringRuLatNumSpec();

        // Вводим тестовую строку в поле заголовка
        editOrCreateNewsPage.getTitleField().perform(typeText(testString));

        // Сверяем введенный текст с тестовой строкой
        editOrCreateNewsPage.getTitleField().check(matches(withText(testString)));
    }

    // case-create6 В меню создания новости поле Описание принимает кириллицу, латиницу, цифры и спецсимволы
    @Test
    @DisplayName("case-create6 В меню создания новости поле Описание принимает кириллицу, латиницу, цифры и спецсимволы")
    public void shouldDescriptionGettingCyrillicLatinNumberAndSpecChar() {
        // Создаем тестовую строку состоящую из кириллицы, латиница, цифр и спецсимволов
        String testString = DataHelper.getStringRuLatNumSpec();

        // Вводим тестовую строку в поле описания
        editOrCreateNewsPage.getDescriptionField().perform(typeText(testString));

        // Сверяем введенный текст с тестовой строкой
        editOrCreateNewsPage.getDescriptionField().check(matches(withText(testString)));
    }

    // case-create7 В меню создания новости чекбокс Active кликабелен
    @Test
    @DisplayName("case-create7 В меню создания новости чекбокс Active кликабелен")
    public void shouldCheckboxActiveBeClickable() {
        // Проверяем кликабельность чекбокса
        editOrCreateNewsPage.getCheckboxActive().check(matches(isClickable()));

        // Проверяем включен ли чекбокс
        editOrCreateNewsPage.getCheckboxActive().check(matches(isEnabled()));
    }

    // case-create8 В меню создания новости кнопка Save кликабельна
    @Test
    @DisplayName("case-create8 В меню создания новости кнопка Save кликабельна")
    public void shouldButtonSaveBeClickable() {
        // Проверяем кликабельность кнопки Save
        editOrCreateNewsPage.getSaveEditOrCreateNewButton().check(matches(isClickable()));

        // Проверяем включена ли кнопка
        editOrCreateNewsPage.getSaveEditOrCreateNewButton().check(matches(isEnabled()));
    }

    // case-create9 В меню создания новости кнопка Cancel кликабельна
    @Test
    @DisplayName("case-create9 В меню создания новости кнопка Cancel кликабельна")
    public void shouldButtonCancelBeClickable() {
        // Проверяем кликабельность кнопки
        editOrCreateNewsPage.getCancelEditOrCreateNewButton().check(matches(isClickable()));

        // Проверяем включена ли кнопка
        editOrCreateNewsPage.getCancelEditOrCreateNewButton().check(matches(isEnabled()));
    }

    // case-create10 В меню создания новости кнопка Save создает текущую новость и возвращается к списку новостей
    @Test
    @DisplayName("case-create10 В меню создания новости кнопка Save создает текущую новость и возвращается к списку новостей")
    public void shouldCreateNewAndReturnToControlPage() {
        // Создаем тестовые данные: заголовок и описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Создаем новость с тестовыми данными, случайной категорией, текущими датой и временем
        controlPage = editOrCreateNewsPage.createNew(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);

        // Листаем до созданной новости с совпадением по созданному заголовку и проверяем на видимость
        controlPage.checkAddedTitleNew(title);
    }

    // case-create11 В меню создания новости кнопка Cancel отображает список новостей без создания новости
    @Test
    @DisplayName("case-create11 В меню создания новости кнопка Cancel отображает список новостей без создания новости")
    public void shouldNotCreateNewAndReturnToControlPage() {
        // Создаем тестовые данные: заголовок и описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Запоняем новость с тестовыми данными, случайной категорией, текущими датой и временем и нажимаем Cancel
        controlPage = editOrCreateNewsPage.dontCreateNew(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);

        // Проверяем что на странице присутствует список новостей
        controlPage.getNewsList().check(CustomViewAssertions.isRecycleView());

        //Сеортируем список по дате публикации
        controlPage.getSortButton().perform(click());

        // Проверяем на отсутствие заголовка заполненной новости в списке
        controlPage.checkExistTitleNew(title);
    }

    // case-create14 В меню создания новости невозможно создать новость с невыбранной категорией
    @Test
    @DisplayName("case-create14 В меню создания новости невозможно создать новость с невыбранной категорией")
    public void shouldNotCreateNewWithEmptyCategory() {
        // Создаем тестовые данные: заголовок и описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Запоняем новость с тестовыми данными, случайной категорией, текущими датой и временем, очищаем поле категории и нажимаем кнопку Создать. Проверяем на ошибку
        editOrCreateNewsPage.createNewWithoutField(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getCategoryField());
    }

    // case-create15 В меню создания новости невозможно создать новость с пустым заголовком
    @Test
    @DisplayName("case-create15 В меню создания новости невозможно создать новость с пустым заголовком")
    public void shouldNotCreateNewWithEmptyTitle() {
        // Создаем тестовые данные: заголовок и описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Запоняем новость с тестовыми данными, случайной категорией, текущими датой и временем, очищаем поле заголовка и нажимаем кнопку Создать. Проверяем на ошибку
        editOrCreateNewsPage.createNewWithoutField(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getTitleField());
    }

    // case-create16 В меню создания новости невозможно создать новость с невыбранной датой публикации
    @Test
    @DisplayName("case-create16 В меню создания новости невозможно создать новость с невыбранной датой публикации")
    public void shouldNotCreateNewWithEmptyDate() {
        // Создаем тестовые данные: заголовок и описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Запоняем новость с тестовыми данными, случайной категорией, текущими датой и временем, очищаем поле даты публикации и нажимаем кнопку Создать. Проверяем на ошибку
        editOrCreateNewsPage.createNewWithoutField(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getPublicationDateField());
    }

    // case-create17 В меню создания новости невозможно создать новость с невыбранным временем публикации
    @Test
    @DisplayName("case-create17 В меню создания новости невозможно создать новость с невыбранным временем публикации")
    public void shouldNotCreateNewWithEmptyTime() {
        // Создаем тестовые данные: заголовок и описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Запоняем новость с тестовыми данными, случайной категорией, текущими датой и временем, очищаем поле времени публикации и нажимаем кнопку Создать. Проверяем на ошибку
        editOrCreateNewsPage.createNewWithoutField(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getPublicationTimeField());
    }

    // case-create18 В меню создания новости невозможно создать новость с пустым описанием
    @Test
    @DisplayName("case-create18 В меню создания новости невозможно создать новость с пустым описанием")
    public void shouldNotCreateNewWithEmptyDescription() {
        // Создаем тестовые данные: заголовок и описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Запоняем новость с тестовыми данными, случайной категорией, текущими датой и временем, очищаем поле описания и нажимаем кнопку Создать. Проверяем на ошибку
        editOrCreateNewsPage.createNewWithoutField(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getDescriptionField());
    }

    // case-create21 У созданной новости в Панели управления все поля соответствуют выбранным
    @Test
    @DisplayName("case-create21 У созданной новости в Панели управления все поля соответствуют выбранным")
    public void shouldCreatedNewFieldsMatch() {
        // Создаем тестовые данные: заголовок, описание, случайную дату не ранее текущего дня
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        int[] date = DataHelper.getRandomFutureDate();

        // Создаем новость со случайной категорией и тестовыми данными
        controlPage = editOrCreateNewsPage.createNew(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, date);

        // Проверяем поля у созданной новости
        controlPage.checkCreatedNew(title, desc, date);
    }

    // case-create22 У созданной новости в Панели управления дата создания новости соответствует текущей дате
    @Test
    @DisplayName("case-create22 У созданной новости в Панели управления дата создания новости соответствует текущей дате")
    public void shouldCreatedNewCreateDateMatch() {
        // Создаем тестовые данные: заголовок, описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Создаем новость со случайной категорией и тестовыми данными
        controlPage = editOrCreateNewsPage.createNew(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);

        // Сверяем дату создания новости с текущей датой
        controlPage.checkNewsCreatedDate(title);
    }
}
