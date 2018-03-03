package org.languagetool.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContextDTO {
    @JsonProperty
    public String text;

    @JsonProperty
    public Integer offset;

    @JsonProperty
    public Integer length;
}
