package com.openelements.hiero.base.data;

import java.util.List;

/**
 * This interface defines a page of data for an endpoint of service that supports pagination.
 *
 * @param <T> the type of the data in the page
 */
public interface Page<T> {

  /**
   * Returns the index of the page.
   *
   * @return the index of the page
   */
  int getPageIndex();

  /**
   * Returns the size of the page. The size is equal to the number of elements in the page.
   *
   * @return the size of the page
   * @see #getData()
   */
  int getSize();

  /**
   * Returns the elements in the page.
   *
   * @return the elements in the page
   */
  List<T> getData();

  /**
   * Returns true if there is a next page.
   *
   * @return true if there is a next page
   * @see #next()
   */
  boolean hasNext();

  /**
   * Returns the next page.
   *
   * @return the next page
   * @see #hasNext()
   */
  Page<T> next();

  /**
   * Returns the first page.
   *
   * @return the first page
   */
  Page<T> first();

  /**
   * Returns true if this page is the first page.
   *
   * @return true if this page is the first page
   */
  boolean isFirst();
}
