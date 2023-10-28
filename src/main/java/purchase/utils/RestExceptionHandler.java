package purchase.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptionMethod(Exception ex) {

        ExceptionMessage exceptionMessageObj = new ExceptionMessage();

        if(ex instanceof MethodArgumentNotValidException) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors();
            for(FieldError fieldError: fieldErrors){
                sb.append(fieldError.getDefaultMessage());
                sb.append(";");
            }
            exceptionMessageObj.setMessage(sb.toString());
        }else{
            exceptionMessageObj.setMessage(ex.getLocalizedMessage());
        }

        return new ResponseEntity<>(exceptionMessageObj, HttpStatus.BAD_REQUEST);
    }
}