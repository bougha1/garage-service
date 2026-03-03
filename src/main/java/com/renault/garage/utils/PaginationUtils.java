package com.renault.garage.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {

    /**
     * Crée un Pageable Spring Data à partir des paramètres page, size et tri.
     *
     * @param page numéro de page (0-based)
     * @param size taille de la page
     * @param sortBy propriété pour le tri (optionnel)
     * @param direction direction du tri ("asc" ou "desc")
     * @return Pageable
     */
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