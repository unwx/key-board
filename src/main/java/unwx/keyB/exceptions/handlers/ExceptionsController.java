package unwx.keyB.exceptions.handlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import unwx.keyB.exceptions.BadRequestException;
import unwx.keyB.exceptions.NotFoundException;

@Controller
public class ExceptionsController {

    @ExceptionHandler(value = NotFoundException.class)
    public String notFoundHandler(){
        return "redirect:/"; // TODO : error page.
    }

    @ExceptionHandler(value = BadRequestException.class)
    public String badRequestHandler(){
        return "redirect:/"; // TODO : error page.
    }

}
