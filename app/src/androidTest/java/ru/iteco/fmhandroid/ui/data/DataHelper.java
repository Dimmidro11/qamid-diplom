package ru.iteco.fmhandroid.ui.data;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

import com.github.javafaker.Faker;

import org.hamcrest.Matcher;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private DataHelper() {}

    private static final String correctLogin = "login2";
    private static final String correctPassword = "password2";
    private static final int countCategory = 8;


    public static String getRandomString()  {
        Faker faker = new Faker(new Locale("en"));
        return faker.name().firstName();
    }

    public static String getStringRuLatNumSpec() {
        String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        Faker ruFaker = new Faker(new Locale("ru"));
        Faker faker = new Faker();
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        String ruWord = ruFaker.lorem().word();
        result.append(ruWord.substring(0, Math.min(3, ruWord.length())));

        String latWord = faker.lorem().word();
        result.append(latWord.substring(0, Math.min(3, latWord.length())));

        result.append(faker.number().digits(3));

        for (int i = 0; i < 2; i++) {
            result.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));
        }

        return result.toString();
    }

    public static String getCorrectPassword() {
        return correctPassword;
    }

    public static String getCorrectLogin() {
        return correctLogin;
    }

    public static int getCountCategory() {
        return countCategory;
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static int[] getRandomFutureDate() {
        Random random = new Random();

        int randomDays = random.nextInt(365);

        LocalDate futureDate = LocalDate.now().plusDays(randomDays);

        return new int[]{
                futureDate.getYear(),
                futureDate.getMonthValue(),
                futureDate.getDayOfMonth()
        };
    }

    public static String formatDateFromArray(int[] date) {
        return String.format(Locale.getDefault(), "%02d.%02d.%d",
                date[2],
                date[1],
                date[0]
        );
    }

    public static ViewAssertion createTextAssertion(String expectedText) {
        return new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                if (noViewFoundException != null) {
                    throw new AssertionError(
                            "Не удалось найти элемент для проверки текста: " +
                                    noViewFoundException.getMessage()
                    );
                }

                if (view == null) {
                    throw new AssertionError("Элемент не найден (null)");
                }

                if (!(view instanceof TextView)) {
                    throw new AssertionError(
                            "Найденный элемент не является TextView. " +
                                    "Тип: " + view.getClass().getSimpleName()
                    );
                }

                TextView textView = (TextView) view;
                String actualText = textView.getText() != null ?
                        textView.getText().toString() : "null";

                if (!expectedText.equals(actualText)) {
                    String viewId = "unknown";
                    try {
                        if (view.getId() != View.NO_ID) {
                            viewId = view.getResources().getResourceName(view.getId());
                        }
                    } catch (Exception e) {
                        viewId = "id:" + view.getId();
                    }

                    throw new AssertionError(
                            "ПРОВАЛ: Текст не совпадает\n" +
                                    "┌─────────────────────────────────────┐\n" +
                                    "│ Элемент: " + viewId + "\n" +
                                    "│ Ожидалось: \"" + expectedText + "\"\n" +
                                    "│ Фактически: \"" + actualText + "\"\n" +
                                    "└─────────────────────────────────────┘"
                    );
                }
            }
        };
    }

    public static String randomizeCategory(String[] categories) {
        List<String> categoriesList = Arrays.asList(categories);
        Random random = new Random();
        int randomIndex = random.nextInt(categoriesList.size());
        return categoriesList.get(randomIndex);
    }

    public static boolean isElementVisible(Matcher<View> matcher) {
        try {
            onView(matcher).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
