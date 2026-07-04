package org.example.haileyproject.common.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.swing.*;

@Component
public class SessionAuthInterceptor implements HandlerInterceptor {

    //HandlerInterceptor (인터셉터)는 스프링(Spring)에서 컨트롤러(Controller)로 요청이 들어가기 전과 후에,
    // 그 요청을 가로채서(Intercept) 필요한 공통 작업을 처리해 주는 '문지기'
    public static final String SESSION_KEY = "LOGIN_USER";

    /*
    preHandle() (컨트롤러 가기 전) ➔ 요청이 컨트롤러에 도착하기 전에 실행됩니다.
    postHandle() (컨트롤러 실행 후, 화면 만들기 전) -> 컨트롤러가 자기 할 일을 다 끝낸 직후에 실행됩니다. (컨트롤러에서 에러가 나면 실행 안 됨)
    afterCompletion() (모든 처리가 끝나고 화면까지 다 그려진 후) -> 뷰(화면) 렌더링까지 완전히 끝난 후 실행됩니다. (에러가 나도 실행됨, 보통 로그 기록이나 리소스 정리용)
    */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        // 세션이 없거나 세션 상 "LOGIN_USER" 가 없는 경우 401 에러 발생
        if (session == null || session.getAttribute(SESSION_KEY) == null) {
            // 인증되지 않은 사용자
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Unauthorized: 인증에 실패했습니다. 세션이 없거나 만료되었습니다.");
            return false;
        }

        return true;
    }
}
