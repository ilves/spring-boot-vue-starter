package ee.golive.controllers.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class MainController {

    @Value( "${indexLocation}" )
    private String indexLocation;

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView(indexLocation);
    }
}
