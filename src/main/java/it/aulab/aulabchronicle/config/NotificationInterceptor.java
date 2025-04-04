package it.aulab.aulabchronicle.config;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import it.aulab.aulabchronicle.repositories.ArticleRepository;
import it.aulab.aulabchronicle.repositories.CareerRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class NotificationInterceptor implements HandlerInterceptor{

    final private CareerRequestRepository careerRequestRepository;

    final private ArticleRepository articleRepository;

    public NotificationInterceptor(CareerRequestRepository careerRequestRepository, ArticleRepository articleRepository) {
        this.careerRequestRepository = careerRequestRepository;
        this.articleRepository = articleRepository;
    }
    

    @SuppressWarnings("null")
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,@Nullable ModelAndView modelAndView) throws Exception {
        if(modelAndView != null && request.isUserInRole("ROLE_ADMIN")){
            int careerCount = careerRequestRepository.findByIsCheckedFalse().size();
            modelAndView.addObject("careerRequests", careerCount);  
        }
        if(modelAndView != null && request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_REVISOR")){
            int articleCount = articleRepository.findByIsAcceptedIsFalse().size();
            modelAndView.addObject("articleCount", articleCount);  
        }
	}
}
