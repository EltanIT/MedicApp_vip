package com.example.medicapp_vip.view.activity


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.medicapp_vip.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AppTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashScreenActivity::class.java)

    @Test
    fun appTest() {
        val materialTextView = onView(
            allOf(
                withId(R.id.miss_text), withText("����������"),
                childAtPosition(
                    withParent(withId(R.id.view_pager)),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())

        val materialTextView2 = onView(
            allOf(
                withId(R.id.miss_text), withText("����������"),
                childAtPosition(
                    withParent(withId(R.id.view_pager)),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView2.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.email),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        2
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("g@g.g"), closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(R.id.continue_btn), withText("�����"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        2
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val pinView = onView(
            allOf(
                withId(R.id.code),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        pinView.perform(replaceText("1111"), closeSoftKeyboard())

        val materialTextView3 = onView(
            allOf(
                withId(R.id.miss_fragment), withText("����������"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.login_main_view),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView3.perform(click())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.et_name),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("���"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.et_middlename),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("�����"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.et_lastname),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("����"), closeSoftKeyboard())

        pressBack()

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.et_birthday),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(replaceText("2005-0-402"), closeSoftKeyboard())

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.et_birthday), withText("2005-0-402"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatEditText6.perform(click())

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.et_birthday), withText("2005-0-402"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatEditText7.perform(replaceText("2005-0402"))

        val appCompatEditText8 = onView(
            allOf(
                withId(R.id.et_birthday), withText("2005-0402"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatEditText8.perform(closeSoftKeyboard())

        val appCompatEditText9 = onView(
            allOf(
                withId(R.id.et_birthday), withText("2005-0402"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatEditText9.perform(click())

        val appCompatEditText10 = onView(
            allOf(
                withId(R.id.et_birthday), withText("2005-0402"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatEditText10.perform(replaceText("2005-04-02"))

        val appCompatEditText11 = onView(
            allOf(
                withId(R.id.et_birthday), withText("2005-04-02"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatEditText11.perform(closeSoftKeyboard())

        pressBack()

        val appCompatSpinner = onView(
            allOf(
                withId(R.id.floor_s),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        appCompatSpinner.perform(click())

        val materialTextView4 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    0
                )
            )
            .atPosition(2)
        materialTextView4.perform(click())

        val appCompatSpinner2 = onView(
            allOf(
                withId(R.id.floor_s),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        appCompatSpinner2.perform(click())

        val materialTextView5 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    0
                )
            )
            .atPosition(1)
        materialTextView5.perform(click())

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.continue_btn), withText("�������"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    8
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.add), withText("��������"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton3.perform(click())

        val relativeLayout = onView(
            allOf(
                withId(R.id.shopping_view),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.main_screen_view),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        relativeLayout.perform(click())

        val appCompatImageView = onView(
            allOf(
                withId(R.id.add_patient_button),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.add_remove_patient_cv),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val appCompatImageView2 = onView(
            allOf(
                withId(R.id.remove_patient_button),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.add_remove_patient_cv),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageView2.perform(click())

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.continue_button), withText("������� � ���������� ������"),
                childAtPosition(
                    allOf(
                        withId(R.id.view_bg),
                        childAtPosition(
                            withId(R.id.main_screen_all_view),
                            0
                        )
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())

        val linearLayout = onView(
            allOf(
                withId(R.id.address_ll),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                )
            )
        )
        linearLayout.perform(scrollTo(), click())

        val appCompatEditText12 = onView(
            allOf(
                withId(R.id.address_et),
                childAtPosition(
                    allOf(
                        withId(R.id.address_ll_view),
                        childAtPosition(
                            withId(R.id.address_card_view),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText12.perform(replaceText("������� 5"), closeSoftKeyboard())

        val appCompatEditText13 = onView(
            allOf(
                withId(R.id.longitude_et),
                childAtPosition(
                    allOf(
                        withId(R.id.longitude_ll),
                        childAtPosition(
                            withId(R.id.group1_ll),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText13.perform(replaceText("1"), closeSoftKeyboard())

        val appCompatEditText14 = onView(
            allOf(
                withId(R.id.width_et),
                childAtPosition(
                    allOf(
                        withId(R.id.width_ll),
                        childAtPosition(
                            withId(R.id.group1_ll),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText14.perform(replaceText("2"), closeSoftKeyboard())

        val appCompatEditText15 = onView(
            allOf(
                withId(R.id.height_et),
                childAtPosition(
                    allOf(
                        withId(R.id.height_ll),
                        childAtPosition(
                            withId(R.id.group1_ll),
                            2
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText15.perform(replaceText("3"), closeSoftKeyboard())

        val appCompatEditText16 = onView(
            allOf(
                withId(R.id.flat_et),
                childAtPosition(
                    allOf(
                        withId(R.id.flat_ll),
                        childAtPosition(
                            withId(R.id.group2_ll),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText16.perform(replaceText("4"), closeSoftKeyboard())

        val appCompatEditText17 = onView(
            allOf(
                withId(R.id.entrance_et),
                childAtPosition(
                    allOf(
                        withId(R.id.entrance_ll),
                        childAtPosition(
                            withId(R.id.group2_ll),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText17.perform(replaceText("5"), closeSoftKeyboard())

        val appCompatEditText18 = onView(
            allOf(
                withId(R.id.floor_et),
                childAtPosition(
                    allOf(
                        withId(R.id.floor_ll),
                        childAtPosition(
                            withId(R.id.group2_ll),
                            2
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText18.perform(replaceText("6"), closeSoftKeyboard())

        val appCompatEditText19 = onView(
            allOf(
                withId(R.id.intercom_et),
                childAtPosition(
                    allOf(
                        withId(R.id.intercom_ll_view),
                        childAtPosition(
                            withId(R.id.address_card_view),
                            4
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText19.perform(replaceText("124"), closeSoftKeyboard())

        val switch_ = onView(
            allOf(
                withId(R.id.save_switch),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.address_card_view),
                        5
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        switch_.perform(click())

        pressBack()

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.address_continue_button), withText("�����������"),
                childAtPosition(
                    allOf(
                        withId(R.id.address_card_view),
                        childAtPosition(
                            withId(R.id.background_view),
                            0
                        )
                    ),
                    8
                ),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())

        val linearLayout2 = onView(
            allOf(
                withId(R.id.date_ll),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        linearLayout2.perform(scrollTo(), click())

        val appCompatButton6 = onView(
            allOf(
                withId(R.id.time_10), withText("10:00"),
                childAtPosition(
                    allOf(
                        withId(R.id.date_card_view),
                        childAtPosition(
                            withId(R.id.background_view),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatButton6.perform(click())

        val appCompatButton7 = onView(
            allOf(
                withId(R.id.date_continue_button), withText("����������"),
                childAtPosition(
                    allOf(
                        withId(R.id.date_card_view),
                        childAtPosition(
                            withId(R.id.background_view),
                            0
                        )
                    ),
                    12
                ),
                isDisplayed()
            )
        )
        appCompatButton7.perform(click())

        val appCompatEditText20 = onView(
            allOf(
                withId(R.id.phone_number),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.phone_ll),
                        1
                    ),
                    1
                )
            )
        )
        appCompatEditText20.perform(scrollTo(), replaceText("985220455"), closeSoftKeyboard())

        pressBack()

        val appCompatButton8 = onView(
            allOf(
                withId(R.id.add_patient_button), withText("�������� ��� ��������"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    7
                )
            )
        )
        appCompatButton8.perform(scrollTo(), click())

        val recyclerView = onView(
            allOf(
                withId(R.id.patients_rv),
                childAtPosition(
                    withId(R.id.patient_select_ll),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val appCompatButton9 = onView(
            allOf(
                withId(R.id.profile_continue_button), withText("����������"),
                childAtPosition(
                    allOf(
                        withId(R.id.profile_card_view),
                        childAtPosition(
                            withId(R.id.background_view),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatButton9.perform(click())

        val appCompatEditText21 = onView(
            allOf(
                withId(R.id.phone_number), withText("985220455"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.phone_ll),
                        1
                    ),
                    1
                )
            )
        )
        appCompatEditText21.perform(scrollTo(), click())

        val appCompatEditText22 = onView(
            allOf(
                withId(R.id.phone_number), withText("985220455"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.phone_ll),
                        1
                    ),
                    1
                )
            )
        )
        appCompatEditText22.perform(scrollTo(), replaceText("9852204555"))

        val appCompatEditText23 = onView(
            allOf(
                withId(R.id.phone_number), withText("9852204555"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.phone_ll),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText23.perform(closeSoftKeyboard())

        pressBack()

        val appCompatButton10 = onView(
            allOf(
                withId(R.id.continue_place_order_button), withText("��������"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        11
                    ),
                    1
                )
            )
        )
        appCompatButton10.perform(scrollTo(), click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
