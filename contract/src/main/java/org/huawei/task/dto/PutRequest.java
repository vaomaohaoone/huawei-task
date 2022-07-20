package org.huawei.task.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class PutRequest implements Serializable {
    Integer key;
    String value;
}
