package com.ex.books.view.validators;

import com.ex.books.utils.Msg;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Date;


@FacesValidator(value = "dateRangeValidator")
public class DateRangeValidator implements Validator {
    @Override
    public void validate(FacesContext context,
                         UIComponent uiComponent, Object o) throws ValidatorException {
        UIInput startDateInput = (UIInput) uiComponent.getAttributes().get("startDateAttr");
        Date startDate = (Date) startDateInput.getValue();
        Date endDate = (Date) o;

        if (endDate.before(startDate)) {
            throw new ValidatorException(Msg.create(
                    FacesMessage.SEVERITY_ERROR,
                    "err.searchFrom.dateRange",
                    "err.searchFrom.dateRange.detail.sequence"));
        }
    }
}