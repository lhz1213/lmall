package lhz.lmall.util;

import org.springframework.data.domain.Page;

import lhz.lmall.vo.MyPage;

/**
 * @author lhz
 *
 * 
 */

public class PageUtil {

	private PageUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	private static Integer[] calPageNum(Integer np, Integer cp, Integer tp, Integer[] pageNumbers) {
		pageNumbers = new Integer[np];
		int start = cp - np / 2;
		int end = cp + np / 2;
		if (end > tp) {
			end = tp;
			for (int i = np - 1; i >= 0; i--) {
				pageNumbers[i] = end--;
			}
		} else {
			start = start < 1 ? 1 : start;
			for (int i = 0; i < np; i++) {
				pageNumbers[i] = start++;
			}
		}
		return pageNumbers;
	}

	public static <T> MyPage<T> converter(Page<T> JPAPage) {
		MyPage<T> mp = new MyPage<>();
		mp.setCurrentPage(JPAPage.getNumber());
		mp.setTotalPages(JPAPage.getTotalPages());
		mp.setList(JPAPage.getContent());
		mp.setFirst(JPAPage.isFirst());
		mp.setLast(JPAPage.isLast());
		mp.setHasNext(JPAPage.hasNext());
		mp.setHasPrevious(JPAPage.hasPrevious());
		mp.setNavigatePages(JPAPage.getTotalPages() > 5 ? 5 : JPAPage.getTotalPages());

		mp.setPageNumbers(
				calPageNum(mp.getNavigatePages(), mp.getCurrentPage(), mp.getTotalPages(), mp.getPageNumbers()));
		return mp;
	}
}
