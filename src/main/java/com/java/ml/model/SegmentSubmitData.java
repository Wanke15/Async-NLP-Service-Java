package com.java.ml.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class SegmentSubmitData {
    @NotEmpty(message = "requestId不能为空")
    @NotBlank(message = "requestId不能为空哟")
    private String requestId;

    @NotEmpty(message = "text不能为空")
    @NotBlank(message = "text不能为空哟")
    private String text;

    public String getRequestId()
    {
        return this.requestId;
    }

    public String getText()
    {
        return this.text;
    }
}
