package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import delivery.search.Engine;
import delivery.search.EngineFactory;


/**
 * <p>Web controller used to ask for listing for a query.</p>
 * @author Matthew Burns
 */
@Controller
public class DeliveryController {

    private final Engine engine;

    public DeliveryController() {
        this.engine = EngineFactory.createEngine();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/delivery")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String req = "http://interestingsoftwarestuff.com:8080/delivery?query=loans&token=1212";
        this.engine.showSettings();
        String res = this.engine.search(req);
        ModelAndView modelAndView = new ModelAndView("delivery", "message", res);
        return modelAndView;
    }
}


