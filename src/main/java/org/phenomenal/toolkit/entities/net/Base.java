package org.phenomenal.toolkit.entities.net;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Base<T> extends CleanBase{
    Integer statusCode;
    String  statusMsg;
    T data;
}
