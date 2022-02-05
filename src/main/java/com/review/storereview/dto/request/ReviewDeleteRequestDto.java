package com.review.storereview.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewDeleteRequestDto {
    private List<String> imgFileNames;

    public ReviewDeleteRequestDto(List<String> fileNames) {
        this.imgFileNames = imgFileNames;
    }
}
