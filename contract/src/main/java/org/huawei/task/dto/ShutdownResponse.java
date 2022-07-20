package org.huawei.task.dto;

import lombok.Value;

import java.io.Serializable;

@Value
public class ShutdownResponse implements Serializable {
    String client;
}
