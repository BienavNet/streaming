package com.api.streaming.domain.valueObjects;

public record Pageable(int page, int offset) {
    public Pageable {
        if (page < 0)
            throw new IllegalArgumentException("Pagination current page cannot be less than 0.");
        if (offset <=0 || offset > 25)
            throw new IllegalArgumentException("Pagination offset should be between 1 and 25.");
    }

    public Integer getPage() {
        return page;
    }

    public Integer getOffset() {
        return offset;
    }
}
