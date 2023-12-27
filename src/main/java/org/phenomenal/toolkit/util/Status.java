package org.phenomenal.toolkit.util;

import org.phenomenal.toolkit.entities.net.CleanBase;

public class Status {
    private static final int SUCCESS = 200;
    private static final int USER_NOT_FOUND = 400;
    private static final int WRONG_PSD = 401;
    public static void set(CleanBase resp, int code){
        resp.setStatusCode(code);
        switch (code){
            case SUCCESS ->{
                resp.setStatusMsg("Success");
                break;
            }
            case USER_NOT_FOUND -> {
                resp.setStatusMsg("User not found");
                break;
            }
            case WRONG_PSD -> {
                resp.setStatusMsg("Wrong password");
                break;
            }
            default -> {
                resp.setStatusMsg("Unknown wrong");
            }
        }
    }
}
