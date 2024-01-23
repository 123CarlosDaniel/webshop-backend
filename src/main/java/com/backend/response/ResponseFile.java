package com.backend.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseFile {
    private String url;
    private String name;
    private String type;
    private long size;
}
