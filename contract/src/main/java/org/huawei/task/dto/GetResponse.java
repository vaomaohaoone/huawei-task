package org.huawei.task.dto;

import lombok.Value;

import java.io.Serializable;

@Value
public class GetResponse implements Serializable {
    boolean isPresent;
    String value;
}
