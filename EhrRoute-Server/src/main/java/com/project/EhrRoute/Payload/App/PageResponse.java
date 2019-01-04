package com.project.EhrRoute.Payload.App;
import java.util.List;


public class PageResponse<T>
{
    private List<T> resources;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;


    public PageResponse() { }
    public PageResponse(List<T> resources, int page, int size, long totalElements, int totalPages, boolean last) {
        this.resources = resources;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }


    public int getSize() {
        return size;
    }
    public int getPage() {
        return page;
    }
    public boolean isLast() {
        return last;
    }
    public int getTotalPages() {
        return totalPages;
    }
    public List<T> getResources() {
        return resources;
    }
    public long getTotalElements() {
        return totalElements;
    }

    public void setSize(int size) {
        this.size = size;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public void setLast(boolean last) {
        this.last = last;
    }
    public void setResources(List<T> content) {
        this.resources = resources;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
