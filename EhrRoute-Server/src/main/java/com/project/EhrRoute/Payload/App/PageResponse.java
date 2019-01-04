package com.project.EhrRoute.Payload.App;
import java.util.List;


public class PageResponse<T>
{
    private List<T> resources;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;


    public PageResponse() { }
    public PageResponse(List<T> resources, int pageNumber, int pageSize, long totalElements, int totalPages, boolean isFirst, boolean isLast) {
        this.resources = resources;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }


    public boolean isLast() {
        return isLast;
    }
    public boolean isFirst() {
        return isFirst;
    }
    public int getPageSize() {
        return pageSize;
    }
    public int getPageNumber() {
        return pageNumber;
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

    public void setLast(boolean last) {
        isLast = last;
    }
    public void setFirst(boolean first) {
        isFirst = first;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
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
