package kirey.com.icap.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kitanoskan on 23/05/2017.
 */

public class RestResponseDto implements Serializable {

    private String message;
    private String errorCode;
    private int statusCode;
    private Object data;
    private Object validationObject;
    private List<ValidationErrorDto> errors = new ArrayList<ValidationErrorDto>();


    public RestResponseDto(){}


    public RestResponseDto(Object data, int statusCode){
        this.data = data;
        this.statusCode = statusCode;
    }

    public RestResponseDto(Object data, int status, String msg){
        this.data = data;
        this.statusCode = status;
        this.message = msg;
    }

    public RestResponseDto(List<ValidationErrorDto> errors){
        this.errors = errors;
    }

    public RestResponseDto(List<ValidationErrorDto> errors, Object object){
        this.errors = errors;
        this.validationObject = object;
    }

    public RestResponseDto(List<ValidationErrorDto> errors, Object object, Object data){
        this.errors = errors;
        this.validationObject = object;
        this.data = data;
    }

    public RestResponseDto(int status, String msg){
        this.message = msg;
        this.statusCode = status;
    }

    public RestResponseDto(int status, String errorCode, String msg){
        this.statusCode = status;
        this.message = msg;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ValidationErrorDto> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationErrorDto> errors) {
        this.errors = errors;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getValidationObject() {
        return validationObject;
    }

    public void setValidationObject(Object validationObject) {
        this.validationObject = validationObject;
    }

}
