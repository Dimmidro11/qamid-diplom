package ru.iteco.fmhandroid.ui;

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
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.iteco.fmhandroid.EspressoIdlingResources;
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

    @BeforeClass
    public static void registerIdlingResourceAndLogin() {
        try (ActivityScenario<AppActivity> scenario = ActivityScenario.launch(AppActivity.class)) {
            IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
            AuthPage authPage = new AuthPage(true);
            staticMainPage = authPage.validLogin();
            IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
        }
    }

    @Before
    public void setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
        mainPage = new MainPage();
        newsPage = mainPage.goToNewsPageFromTextAllNews();
        controlPage = newsPage.goToControlPage();
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
    }

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
    public void shouldDeleteNew() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        editOrCreateNewsPage = controlPage.goToCreateNews();
        controlPage = editOrCreateNewsPage.createNew(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);
        controlPage.checkAddedTitleNew(title);
        controlPage.deleteNew(true, title);
    }

    // case-delete2 На странице панели управления в списке новостей кнопка удаления после отмены не удаляет выбранную новость из списка
    @Test
    public void shouldNotDeleteNewCancelDelete() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        editOrCreateNewsPage = controlPage.goToCreateNews();
        controlPage = editOrCreateNewsPage.createNew(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);
        controlPage.checkAddedTitleNew(title);
        controlPage.deleteNew(false, title);
    }
}
