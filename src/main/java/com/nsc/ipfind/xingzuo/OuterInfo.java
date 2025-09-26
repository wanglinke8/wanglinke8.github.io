package com.nsc.ipfind.xingzuo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OuterInfo {
    private String title;
    private String dec;
    private List<ItemInfo> items;
}
