package com.hxw.wscrm.sys.model;

import lombok.Data;

import java.util.List;

@Data
public class ShowMenu {
    private String path;
    private String name;
    private String label;
    private String icon;
    private String url;
    private List<ShowMenu> children;

}
