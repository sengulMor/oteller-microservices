package com.oteller.hotelservice.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;

    @NotNull
    private Long hotelId;

    @NotNull
    @NotBlank(message = "Guest Name name is required")
    private String guestName;

    @Min(value = 1, message = "Star Rating can not smaller as 1")
    @Max(value = 5, message = "Star Rating can not bigger as 5")
    private int rating;

    @NotNull
    @NotBlank(message = "Comment is required")
    private String comment;

    private String sentiment;

    private int sentimentScore;

    private String summary;

    private List<String> keyPoints;

    private String status;

}
