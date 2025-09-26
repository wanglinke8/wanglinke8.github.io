package com.nsc.ipfind.xingzuo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearsInfo {
    private List<String> info;
    private List<DetailList> list;
}
