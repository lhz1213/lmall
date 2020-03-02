package lhz.lmall.vo;

import java.util.List;

import lombok.Data;

@Data
public class MyPage<T> {

	// 显示几个分页链接
	private Integer navigatePages;
	// 显示哪几个分页链接
	private Integer[] pageNumbers;

	private Integer currentPage;

	private Integer totalPages;

	// 数据集合
	private List<T> list;

	private boolean First;
	private boolean Last;
	private boolean hasNext;
	private boolean hasPrevious;
}
