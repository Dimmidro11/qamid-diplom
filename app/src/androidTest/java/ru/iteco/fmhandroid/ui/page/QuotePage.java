package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;

public class QuotePage {
    // Набор элементов
    private final ViewInteraction quoteText = onView(withId(R.id.our_mission_title_text_view));
    private final ViewInteraction recyclerView = onView(withId(R.id.our_mission_item_list_recycler_view));

    // Конструктор с проверкой нахождения на странице Quote по видимости элемента с текстом Love is all
    public QuotePage() {
        Allure.step("Проверка страницы Quote в конструкторе класса");
        quoteText.check(matches(isDisplayed()));
    }

    // Геттеры
    public ViewInteraction getRecyclerView() {
        return recyclerView;
    }
}
