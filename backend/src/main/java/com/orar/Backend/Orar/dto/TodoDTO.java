package com.orar.Backend.Orar.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoDTO {
    private String username;
    private String title;
    private String description;
    private String deadline;
    private Boolean done;
}
