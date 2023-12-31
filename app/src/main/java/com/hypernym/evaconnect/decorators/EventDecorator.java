package com.hypernym.evaconnect.decorators;

import com.hypernym.evaconnect.models.CalendarMarks;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class EventDecorator implements DayViewDecorator {

    private  int[] colors;
    private final HashSet<CalendarDay> dates;
    private List<CalendarMarks> calendarMarks;
 private CalendarMarks calendarMark;



    public EventDecorator(Collection<CalendarDay> dates, int[] colors, List<CalendarMarks> calendarMarks) {
        //this.color = color;
        this.dates = new HashSet<>(dates);
        this.colors = colors;
        this.calendarMarks=calendarMarks;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {

        view.addSpan((new CustmMultipleDotSpan(5,colors)));
    }
}