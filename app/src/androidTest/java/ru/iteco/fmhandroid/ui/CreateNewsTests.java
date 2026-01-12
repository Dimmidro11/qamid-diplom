package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

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
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.CustomViewAssertions;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AuthPage;
import ru.iteco.fmhandroid.ui.page.ControlPage;
import ru.iteco.fmhandroid.ui.page.EditOrCreateNewsPage;
import ru.iteco.fmhandroid.ui.page.MainPage;
import ru.iteco.fmhandroid.ui.page.NewsPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class CreateNewsTests {
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
        editOrCreateNewsPage = controlPage.goToCreateNews();
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


    // case-create1 В меню создания новости отображается: выпадающий список категорий, поле заголовка, поле даты публикации, поле времени публикации, поле описания, чекбокс Active, кнопки Save и Cancel
    @Test
    public void shouldBeVisibleCategoryTitleDateTimeDescriptionActiveSaveCancel() {
        editOrCreateNewsPage.getCategoryField().check(matches(isDisplayed()));
        editOrCreateNewsPage.getTitleField().check(matches(isDisplayed()));
        editOrCreateNewsPage.getPublicationDateField().check(matches(isDisplayed()));
        editOrCreateNewsPage.getPublicationTimeField().check(matches(isDisplayed()));
        editOrCreateNewsPage.getDescriptionField().check(matches(isDisplayed()));
        editOrCreateNewsPage.getCheckboxActive().check(matches(isDisplayed()));
        editOrCreateNewsPage.getSaveEditOrCreateNewButton().check(matches(isDisplayed()));
        editOrCreateNewsPage.getCancelEditOrCreateNewButton().check(matches(isDisplayed()));
    }

    // case-create2 В меню создания новости в выпадающем списке 8 категорий
    @Test
    public void shouldBe8Categories() {
        editOrCreateNewsPage.clickButton(editOrCreateNewsPage.getCategoryField());
        assertThat(editOrCreateNewsPage.countCategories(), is(DataHelper.getCountCategory()));
    }

    // case-create3 В меню создания новости поле Заголовок принимает кириллицу, латиницу, цифры и спецсимволы
    @Test
    public void shouldTitleGettingCyrillicLatinNumberAndSpecChar() {
        String testString = DataHelper.getStringRuLatNumSpec();
        editOrCreateNewsPage.getTitleField().perform(typeText(testString));
        editOrCreateNewsPage.getTitleField().check(matches(withText(testString)));
    }

    // case-create6 В меню создания новости поле Описание принимает кириллицу, латиницу, цифры и спецсимволы
    @Test
    public void shouldDescriptionGettingCyrillicLatinNumberAndSpecChar() {
        String testString = DataHelper.getStringRuLatNumSpec();
        editOrCreateNewsPage.getDescriptionField().perform(typeText(testString));
        editOrCreateNewsPage.getDescriptionField().check(matches(withText(testString)));
    }

    // case-create7 В меню создания новости чекбокс Active кликабелен
    @Test
    public void shouldCheckboxActiveBeClickable() {
        editOrCreateNewsPage.getCheckboxActive().check(matches(isClickable()));
        editOrCreateNewsPage.getCheckboxActive().check(matches(isEnabled()));
    }

    // case-create8 В меню создания новости кнопка Save кликабельна
    @Test
    public void shouldButtonSaveBeClickable() {
        editOrCreateNewsPage.getSaveEditOrCreateNewButton().check(matches(isClickable()));
        editOrCreateNewsPage.getSaveEditOrCreateNewButton().check(matches(isEnabled()));
    }

    // case-create9 В меню создания новости кнопка Cancel кликабельна
    @Test
    public void shouldButtonCancelBeClickable() {
        editOrCreateNewsPage.getCancelEditOrCreateNewButton().check(matches(isClickable()));
        editOrCreateNewsPage.getCancelEditOrCreateNewButton().check(matches(isEnabled()));
    }

    // case-create10 В меню создания новости кнопка Save создает текущую новость и возвращается к списку новостей
    @Test
    public void shouldCreateNewAndReturnToControlPage() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        controlPage = editOrCreateNewsPage.createNew(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);
        controlPage.checkAddedTitleNew(title);
    }

    // case-create11 В меню создания новости кнопка Cancel отображает список новостей без создания новости
    @Test
    public void shouldNotCreateNewAndReturnToControlPage() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        controlPage = editOrCreateNewsPage.dontCreateNew(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);
        controlPage.getNewsList().check(CustomViewAssertions.isRecycleView());
        controlPage.getSortButton().perform(click());
        onView(allOf(withId(R.id.news_item_title_text_view), withText(title))).check(doesNotExist());
    }

    // case-create14 В меню создания новости невозможно создать новость с невыбранной категорией
    @Test
    public void shouldNotCreateNewWithEmptyCategory() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        editOrCreateNewsPage.createNewWithoutField(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getCategoryField());
    }

    // case-create15 В меню создания новости невозможно создать новость с пустым заголовком
    @Test
    public void shouldNotCreateNewWithEmptyTitle() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        editOrCreateNewsPage.createNewWithoutField(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getTitleField());
    }

    // case-create16 В меню создания новости невозможно создать новость с невыбранной датой публикации
    @Test
    public void shouldNotCreateNewWithEmptyDate() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        editOrCreateNewsPage.createNewWithoutField(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getPublicationDateField());
    }

    // case-create17 В меню создания новости невозможно создать новость с невыбранным временем публикации
    @Test
    public void shouldNotCreateNewWithEmptyTime() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        editOrCreateNewsPage.createNewWithoutField(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getPublicationTimeField());
    }

    // case-create18 В меню создания новости невозможно создать новость с пустым описанием
    @Test
    public void shouldNotCreateNewWithEmptyDescription() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        editOrCreateNewsPage.createNewWithoutField(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getDescriptionField());
    }

    // case-create21 У созданной новости в Панели управления все поля соответствуют выбранным
    @Test
    public void shouldCreatedNewFieldsMatch() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        int[] date = DataHelper.getRandomFutureDate();
        controlPage = editOrCreateNewsPage.createNew(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, date);
        controlPage.checkCreatedNew(title, desc, date);
    }

    // case-create22 У созданной новости в Панели управления дата создания новости соответствует текущей дате
    @Test
    public void shouldCreatedNewCreateDateMatch() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        controlPage = editOrCreateNewsPage.createNew(DataHelper.randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);
        controlPage.checkNewsCreatedDate(title);
    }
}
