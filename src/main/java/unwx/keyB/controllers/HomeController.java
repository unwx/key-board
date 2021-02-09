package unwx.keyB.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import unwx.keyB.services.ArticleService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/**")
@SuppressWarnings("serial")
public class HomeController {

    private final ArticleService articleService;

    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String home(Model model){
        model.addAttribute("isDevMode", true);
        model.addAttribute("frontendData", initFrontendData());
        return "home";
    }

    // TODO : ENABLE CACHING
    private Map<String, Object> initFrontendData(){
        return new HashMap<>(){{
            put("articles" ,articleService.getAll());
        }};
    }

}
