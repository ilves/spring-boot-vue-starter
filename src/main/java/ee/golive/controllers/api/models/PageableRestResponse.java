package ee.golive.controllers.api.models;

import org.springframework.data.domain.Page;

public class PageableRestResponse<T> extends RestResponse<T> {

  private Pagination pagination;

  public PageableRestResponse(Page page) {
    super((T) page.getContent());
    Pagination pagination = new Pagination();
    pagination.setCount(page.getNumberOfElements());
    pagination.setLimit(page.getSize());
    pagination.setTotalCount(page.getTotalElements());
    pagination.setOffset(page.getNumber());
    setPagination(pagination);
  }

  public Pagination getPagination() {
    return pagination;
  }

  public void setPagination(Pagination pagination) {
    this.pagination = pagination;
  }

  public class Pagination {
    private Integer count;
    private Long totalCount;
    private Integer limit;
    private Integer offset;

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }

    public Long getTotalCount() {
      return totalCount;
    }

    public void setTotalCount(long totalCount) {
      this.totalCount = totalCount;
    }

    public int getLimit() {
      return limit;
    }

    public void setLimit(int limit) {
      this.limit = limit;
    }

    public int getOffset() {
      return offset;
    }

    public void setOffset(int offset) {
      this.offset = offset;
    }
  }
}
