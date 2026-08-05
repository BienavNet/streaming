package com.api.streaming.domain.valueObjects;

import java.util.List;
import java.util.function.Function;

public record Page<T>(List<T> content, Long totalElements, int totalPages) {
    public List<T> getContent() { return content; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }

    public <R> Page<R> map(Function<T, R> mapper) throws Exception {
        try {
            List<R> mappedContent = this.content
                                .stream()
                                .map(mapper)
                                .toList();
        
            return new Page<>(mappedContent, this.totalElements(), this.totalPages());
        } catch (Exception e) {
            throw new Exception("Error when mapping element list from Page.");
        }
    }
}
