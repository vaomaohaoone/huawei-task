package org.huawei.task.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class ShutdownRequest implements Serializable {
    String client;
}
