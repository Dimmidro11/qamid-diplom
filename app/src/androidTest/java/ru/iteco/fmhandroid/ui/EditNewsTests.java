package ru.iteco.fmhandroid.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
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
import ru.iteco.fmhandroid.R;
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


    // case-edit1 В меню редактирования новости отображаются: выпадающий список категорий, поле заголовка, поле даты публикации, поле времени публикации, поле описания, чекбокс Active, кнопки Save и Cancel
    @Test
    public void shouldBeVisibleCategoryTitleDateTimeDescriptionActiveSaveCancel() {
        editOrCreateNewsPage = controlPage.goToEditNews();
        editOrCreateNewsPage.getCategoryField().check(matches(isDisplayed()));
        editOrCreateNewsPage.getTitleField().check(matches(isDisplayed()));
        editOrCreateNewsPage.getPublicationDateField().check(matches(isDisplayed()));
        editOrCreateNewsPage.getPublicationTimeField().check(matches(isDisplayed()));
        editOrCreateNewsPage.getDescriptionField().check(matches(isDisplayed()));
        editOrCreateNewsPage.getCheckboxActive().check(matches(isDisplayed()));
        editOrCreateNewsPage.getSaveEditOrCreateNewButton().check(matches(isDisplayed()));
        editOrCreateNewsPage.getCancelEditOrCreateNewButton().check(matches(isDisplayed()));
    }

    // case-edit2 В меню редактирования новости поля заполнены соответствующими выбранной новости данными
    @Test
    public void shouldFilledFieldsMatch() {
        String[] extractedData = controlPage.extractData();
        editOrCreateNewsPage = controlPage.goToEditNews();
        editOrCreateNewsPage.checkEditingNewsFields(extractedData);
    }

    // case-edit3 В меню редактирования новости в выпадающем списке содержится 8 категорий
    @Test
    public void shouldBe8Categories() {
        editOrCreateNewsPage = controlPage.goToEditNews();
        editOrCreateNewsPage.checkCountCategories();
    }

    // case-edit4 В меню редактирования новости поле Заголовок принимает кириллицу, латиницу, цифры и спецсимволы
    @Test
    public void shouldTitleGettingCyrillicLatinNumberAndSpecChar() {
        String testString = DataHelper.getStringRuLatNumSpec();
        editOrCreateNewsPage = controlPage.goToEditNews();
        editOrCreateNewsPage.getTitleField().perform(clearText(), typeText(testString));
        editOrCreateNewsPage.getTitleField().check(matches(withText(testString)));
    }

    // case-edit7 В меню редактирования новости поле Описание принимает кириллицу, латиницу, цифры и спецсимволы
    @Test
    public void shouldDescriptionGettingCyrillicLatinNumberAndSpecChar() {
        String testString = DataHelper.getStringRuLatNumSpec();
        editOrCreateNewsPage = controlPage.goToEditNews();
        editOrCreateNewsPage.getDescriptionField().perform(clearText(), typeText(testString));
        editOrCreateNewsPage.getDescriptionField().check(matches(withText(testString)));
    }

    // case-edit8 В меню редактирования новости чекбокс Active кликабелен
    @Test
    public void shouldCheckboxActiveBeClickable() {
        editOrCreateNewsPage = controlPage.goToEditNews();
        editOrCreateNewsPage.getCheckboxActive().check(matches(isClickable()));
        editOrCreateNewsPage.getCheckboxActive().check(matches(isEnabled()));
    }

    // case-edit9 В меню редактирования новости кнопка Save кликабельна
    @Test
    public void shouldButtonSaveBeClickable() {
        editOrCreateNewsPage = controlPage.goToEditNews();
        editOrCreateNewsPage.getSaveEditOrCreateNewButton().check(matches(isClickable()));
        editOrCreateNewsPage.getSaveEditOrCreateNewButton().check(matches(isEnabled()));
    }

    // case-edit10 В меню редактирования новости кнопка Cancel кликабельна
    @Test
    public void shouldButtonCancelBeClickable() {
        editOrCreateNewsPage = controlPage.goToEditNews();
        editOrCreateNewsPage.getCancelEditOrCreateNewButton().check(matches(isClickable()));
        editOrCreateNewsPage.getCancelEditOrCreateNewButton().check(matches(isEnabled()));
    }

    // case-edit11 В меню редактирования новости кнопка Save изменяет текущую новость и возвращается к списку новостей
    @Test
    public void shouldEditNewAndReturnToControlPage() {
        String newTitle = DataHelper.getRandomString();
        String newDesc = DataHelper.getRandomString();
        String extractTitle = controlPage.extractTitle();
        String extractDesc = controlPage.extractDescription();
        editOrCreateNewsPage = controlPage.goToEditNews();
        controlPage = editOrCreateNewsPage.createNew(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), newTitle, newDesc);
        controlPage.checkAddedTitleNew(newTitle);
        controlPage.getTitleText(extractTitle).check(doesNotExist());
    }

    // case-edit12 В меню редактирования новости кнопка Cancel отображает список новостей без изменения новости
    @Test
    public void shouldNotEditNewAndReturnToControlPage() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        String extractTitle = controlPage.extractTitle();
        editOrCreateNewsPage = controlPage.goToEditNews();
        controlPage = editOrCreateNewsPage.dontCreateNew(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);
        onView(allOf(withId(R.id.news_item_title_text_view), withText(title))).check(doesNotExist());
        controlPage.checkAddedTitleNew(extractTitle);
    }

    // case-edit16 В меню редактирования новости невозможно сохранить новость с невыбранной категорией
    @Test
    public void shouldNotEditNewWithEmptyCategory() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        editOrCreateNewsPage = controlPage.goToEditNews();
        editOrCreateNewsPage.createNewWithoutField(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getCategoryField());
    }

    // case-edit17 В меню редактирования новости невозможно сохранить новость с пустым заголовком
    @Test
    public void shouldNotEditNewWithEmptyTitle() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        editOrCreateNewsPage = controlPage.goToEditNews();
        editOrCreateNewsPage.createNewWithoutField(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getTitleField());
    }

    // case-edit18 В меню редактирования новости невозможно сохранить новость с пустым описанием
    @Test
    public void shouldNotEditNewWithEmptyDate() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        editOrCreateNewsPage = controlPage.goToEditNews();
        editOrCreateNewsPage.createNewWithoutField(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, editOrCreateNewsPage.getPublicationDateField());
    }

    // case-edit19 У измененной новости в Панели управления все поля соответствуют выбранным
    @Test
    public void shouldEditedNewFieldsMatch() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        int[] date = DataHelper.getRandomFutureDate();
        editOrCreateNewsPage = controlPage.goToEditNews();
        controlPage = editOrCreateNewsPage.createNew(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc, date);
        controlPage.checkCreatedNew(title, desc, date);
    }

    // case-edit20 У измененной новости в Панели управления дата создания новости соответствует текущей дате
    @Test
    public void shouldEditedNewCreateDateMatch() {
        String title = DataHelper.getRandomString();
        String desc = DataHelper.getRandomString();
        editOrCreateNewsPage = controlPage.goToEditNews();
        controlPage = editOrCreateNewsPage.createNew(randomizeCategory(editOrCreateNewsPage.getCategoriesArray()), title, desc);
        controlPage.checkNewsCreatedDate(title);
    }
}
