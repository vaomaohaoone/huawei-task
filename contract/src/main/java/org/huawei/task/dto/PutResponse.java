package org.huawei.task.dto;

import lombok.Value;

import java.io.Serializable;

@Value
public class PutResponse implements Serializable {
    Boolean saved;
    Integer key;
}
