package com.koala.utils.common.mybatis;


import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

/**
 * 分页请求对象
 */
public class PageRequest implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -7767909042764475002L;

    private int pageNumber = 1;

    private int pageSize = 0;

    public PageRequest() {

    }

    public PageRequest(int pageNo, int pageSize) {
        this.pageNumber = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }


    public boolean hasPrevious() {
        return pageNumber > 1;
    }

    public PageRequest next() {
        return new PageRequest(pageNumber + 1, getPageSize());
    }

    public PageRequest previousOrFirst() {
        return hasPrevious() ? new PageRequest(pageNumber - 1, getPageSize()) : this;
    }

    public PageRequest first() {
        return new PageRequest(1, getPageSize());
    }

    public RowBounds getRowBounds() {
        return new RowBounds((Math.max(1, pageNumber) - 1) * pageSize, pageSize);
    }

    @Override
    public int hashCode() {
        return pageNumber;
    }
    
}