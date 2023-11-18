package org.phenomenal.toolkit.entities.net;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Base<T> {
    Integer statusCode;
    String  statusMsg;
    T data;
}
