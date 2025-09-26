package com.nsc.ipfind.xingzuo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConstellationInfo {
    private String avatar;
    private String name;
    private String symbol;
    private String enName;
    private String date;
    private List<OuterInfo> outer;
}
