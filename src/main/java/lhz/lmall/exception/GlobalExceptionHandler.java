package lhz.lmall.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MyException.class)
	public String handlerMyException(MyException me) {

		return me.getMessage();
	}

}
