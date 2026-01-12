package ru.iteco.fmhandroid.ui.data;
import static androidx.test.espresso.Espresso.onView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.view.View;
import android.widget.TextView;
import java.util.concurrent.atomic.AtomicReference;
public class MaterialTextExtractor {

    /**
     * Извлечь текст из MaterialTextView
     */
    public static String extractText(Matcher<View> viewMatcher) {
        AtomicReference<String> extractedText = new AtomicReference<>();

        onView(viewMatcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                // Проверяем, что это TextView или его подкласс (включая MaterialTextView)
                return new TypeSafeMatcher<View>() {
                    @Override
                    public void describeTo(Description description) {
                        description.appendText("is TextView or subclass");
                    }

                    @Override
                    protected boolean matchesSafely(View item) {
                        return item instanceof TextView;
                    }
                };
            }

            @Override
            public String getDescription() {
                return "Extract text from MaterialTextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView textView = (TextView) view;
                extractedText.set(textView.getText().toString());
            }
        });

        return extractedText.get();
    }
}

