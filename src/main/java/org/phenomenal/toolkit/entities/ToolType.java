package org.phenomenal.toolkit.entities;

import com.baomidou.mybatisplus.annotation.EnumValue;


public enum ToolType {
    CIDR_CAL(0),B_TREE(1),QR_CODE(2),FUNC_GRAPH(3),FLOATING_POINT(4);
    @EnumValue
    private int code;
    ToolType(int code) {
        this.code = code;
    }
    public static ToolType valueOf(int i){
        ToolType[] values = values();
        if (i >= values.length) return null;
        return values[i];
    }
}
