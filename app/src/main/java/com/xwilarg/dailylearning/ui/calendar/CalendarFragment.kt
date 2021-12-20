package com.xwilarg.dailylearning.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.xwilarg.dailylearning.*
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_calendar, container, false)
        val calendarView = v.findViewById<CalendarView>(R.id.calendarView);
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    container.textView.setTextColor(Color.BLACK)
                } else {
                    container.textView.setTextColor(Color.GRAY)
                }
            }
        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
        return v
    }
}