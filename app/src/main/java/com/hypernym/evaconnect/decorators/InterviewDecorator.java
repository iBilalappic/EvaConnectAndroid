package com.hypernym.evaconnect.decorators;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class InterviewDecorator implements DayViewDecorator {

    private final int[] colors;
    private final HashSet<CalendarDay> dates;


    public InterviewDecorator(Collection<CalendarDay> dates, int[] colors) {
        //this.color = color;
        this.dates = new HashSet<>(dates);
        this.colors = colors;

    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {

      //  view.addSpan((new CustmMultipleDotSpan(5,colors)));
        view.addSpan(new DotSpan(6, colors[0]));

    }


}