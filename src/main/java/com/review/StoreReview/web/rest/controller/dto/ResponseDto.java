package com.review.StoreReview.web.rest.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDto<T> {
    int status;
    T data;
}
