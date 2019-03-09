package br.com.alura.forum.controller.dto.output;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorsOutputDto {

    private List<String> globalErrorMessages = new ArrayList<>();
    private List<FieldErrorOutputDto> fieldErrors = new ArrayList<>();


    public void addError(String message) {
        globalErrorMessages.add(message);
    }

    public void addFieldError(String field, String message){
        fieldErrors.add(new FieldErrorOutputDto(field, message));
    }


    public List<String> getGlobalErrorMessages() {
        return globalErrorMessages;
    }

    public List<FieldErrorOutputDto> getFieldErrors() {
        return fieldErrors;
    }


    public int getNumberOfErrors() {
        return globalErrorMessages.size() + fieldErrors.size();
    }
}
