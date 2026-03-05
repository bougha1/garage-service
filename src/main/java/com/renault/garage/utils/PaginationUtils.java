package com.renault.garage.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {

    public static Pageable createPageable(int page, int size, String sortBy, String direction) {
        if (sortBy == null || sortBy.isEmpty()) {
            return PageRequest.of(page, size);
        }
        Sort sort = direction != null && direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        return PageRequest.of(page, size, sort);
    }
}