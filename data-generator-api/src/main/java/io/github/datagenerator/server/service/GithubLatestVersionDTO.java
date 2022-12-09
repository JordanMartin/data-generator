package io.github.datagenerator.server.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubLatestVersionDTO {
    private String name;
    private String created_at;

    public String getVersion() {
        if (name == null) {
            return null;
        }
        return name.replace("v", "");
    }
}