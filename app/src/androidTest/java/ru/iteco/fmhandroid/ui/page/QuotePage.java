package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;

public class QuotePage {
    private final ViewInteraction quoteText = onView(withId(R.id.our_mission_title_text_view));
    private final ViewInteraction recyclerView = onView(withId(R.id.our_mission_item_list_recycler_view));
    public QuotePage() {
        quoteText.check(matches(isDisplayed()));
    }

    public ViewInteraction getRecyclerView() {
        return recyclerView;
    }
}
