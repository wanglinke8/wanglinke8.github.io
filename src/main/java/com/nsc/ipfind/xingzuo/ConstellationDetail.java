package com.nsc.ipfind.xingzuo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConstellationDetail {
    private TodayInfo today;
    private TomorrowInfo tomorrow;
    private WeeksInfo weeks;
    private MonthInfo month;
    private YearsInfo years;
}
