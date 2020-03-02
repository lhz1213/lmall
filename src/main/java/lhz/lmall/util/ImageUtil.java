package lhz.lmall.util;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import lhz.lmall.entity.Category;
import lhz.lmall.entity.ProductImage;
import net.coobird.thumbnailator.Thumbnails;

public class ImageUtil {

	private ImageUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void save(Category category, MultipartFile image, Integer size, String type) throws IOException {
		if (null != image) {
			File filefolder1 = new File(System.getProperty("user.dir") + "/target/classes/static/img/" + type);
			File filefolder2 = new File(System.getProperty("user.dir") + "/src/main/resources/static/img/" + type);
			filefolder1.mkdirs();
			filefolder2.mkdirs();
			File file1 = new File(filefolder1.getAbsolutePath() + "/" + category.getId() + ".jpg");
			File file2 = new File(filefolder2.getAbsolutePath() + "/" + category.getId() + ".jpg");
			Thumbnails.of(image.getInputStream()).outputFormat("jpg").size(size, size).toFile(file1.getAbsolutePath());
			Thumbnails.of(image.getInputStream()).outputFormat("jpg").size(size, size).toFile(file2.getAbsolutePath());
		}
	}

	public static void save(ProductImage productImage, MultipartFile image, Integer size, String type)
			throws IOException {
		if (null != image) {
			File filefolder1 = new File(System.getProperty("user.dir") + "/target/classes/static/img/" + type);
			File filefolder2 = new File(System.getProperty("user.dir") + "/src/main/resources/static/img/" + type);
			filefolder1.mkdirs();
			filefolder2.mkdirs();
			File file1 = new File(filefolder1.getAbsolutePath() + "/" + productImage.getId() + ".jpg");
			File file2 = new File(filefolder2.getAbsolutePath() + "/" + productImage.getId() + ".jpg");
			Thumbnails.of(image.getInputStream()).outputFormat("jpg").size(size, size).toFile(file1.getAbsolutePath());
			Thumbnails.of(image.getInputStream()).outputFormat("jpg").size(size, size).toFile(file2.getAbsolutePath());
		}
	}

	public static void delete(Integer id, String type) {
		File file1 = new File(
				System.getProperty("user.dir") + "/target/classes/static/img/" + type + "/" + id + ".jpg");
		File file2 = new File(
				System.getProperty("user.dir") + "/src/main/resources/static/img/" + type + "/" + id + ".jpg");

		if (file1.exists()) {
			file1.delete();
		}
		if (file2.exists()) {
			file2.delete();
		}
	}

}
