package kirey.com.icap.model;

import java.io.Serializable;

/**
 * Created by kitanoskan on 23/05/2017.
 */

public class ValidationErrorDto implements Serializable {

    private String pageId;
    private String errorCode;
    private String fieldName;

    private String additionalDataForField;

    public ValidationErrorDto(){
    }

    public ValidationErrorDto(String errorCode){
        pageId=null;
        this.errorCode = errorCode;
    }

    public ValidationErrorDto(String pageId, String errorCode){
        this.pageId = pageId;
        this.errorCode = errorCode;
    }

    public ValidationErrorDto(String pageId, String errorCode, String fieldName){
        this.pageId = pageId;
        this.errorCode = errorCode;
        this.fieldName = fieldName;
    }

    public ValidationErrorDto(String pageId, String errorCode, String fieldName, String additionalData){
        this.pageId = pageId;
        this.errorCode = errorCode;
        this.fieldName = fieldName;
        this.additionalDataForField = additionalData;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getAdditionalDataForField() {
        return additionalDataForField;
    }


    public void setAdditionalDataForField(String additionalDataForField) {
        this.additionalDataForField = additionalDataForField;
    }

}
