package ru.iteco.fmhandroid.ui.tests;

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
public class DeleteNewsTests {
    private static MainPage staticMainPage;
    private MainPage mainPage;
    private NewsPage newsPage;
    private ControlPage controlPage;
    private EditOrCreateNewsPage editOrCreateNewsPage;

    @Rule
    public ActivityScenarioRule<AppActivity> activityScenarioRule = new ActivityScenarioRule<>(AppActivity.class);

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


    // case-delete1 На странице панели управления в списке новостей кнопка удаления после подтверждения удаляет выбранную новость из списка
    @Test
    @DisplayName("case-delete1 На странице панели управления в списке новостей кнопка удаления после подтверждения удаляет выбранную новость из списка")
    public void shouldDeleteNew() {
        // Создаем тестовые даныне - заголовок и описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Переходим к странице создания новости
        editOrCreateNewsPage = controlPage.goToCreateNews();

        // Создаем новость с случайной категорией и тестовыми данными. Дата и время текущие
        controlPage = editOrCreateNewsPage.createNew(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);

        // Проверяем созданную новость
        controlPage.checkAddedTitleNew(title);

        // Кликаем удалить, подтверждаем удаление и проверяем на отсутствие ранее созданной и удаленной новости
        controlPage.deleteNew(true, title);
    }

    // case-delete2 На странице панели управления в списке новостей кнопка удаления после отмены не удаляет выбранную новость из списка
    @Test
    @DisplayName("case-delete2 На странице панели управления в списке новостей кнопка удаления после отмены не удаляет выбранную новость из списка")
    public void shouldNotDeleteNewCancelDelete() {
        // Создаем тестовые данные - заголовок и описание
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();

        // Переходим к странице создания новости
        editOrCreateNewsPage = controlPage.goToCreateNews();

        // Создаем новость с случайной категорией и тестовыми данными. Дата и время текущие
        controlPage = editOrCreateNewsPage.createNew(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);

        // Проверяем созданную новость
        controlPage.checkAddedTitleNew(title);

        // Кликаем удалить, отменяем удаление и проверяем на присутсвие в списке ранее созданной новости
        controlPage.deleteNew(false, title);
    }
}
