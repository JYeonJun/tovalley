package kr.ac.kumoh.illdang100.tovalley.security.jwt.filter;

import kr.ac.kumoh.illdang100.tovalley.handler.ex.UnauthorizedAccessException;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshToken;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;
import kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil;
import kr.ac.kumoh.illdang100.tovalley.util.HttpServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kr.ac.kumoh.illdang100.tovalley.util.CookieUtil.*;
import static kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.findRefreshTokenOrElseThrowEx;
import static kr.ac.kumoh.illdang100.tovalley.util.TokenUtil.saveRefreshToken;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtProcess jwtProcess;

    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess,
                                  RefreshTokenRedisRepository refreshTokenRedisRepository) {
        super(authenticationManager);
        this.jwtProcess = jwtProcess;
        this.refreshTokenRedisRepository = refreshTokenRedisRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String requestUri = request.getRequestURI();
        if (requestUri.contains("//")) {
            logger.warn("Double slash detected in request URI: " + requestUri);
        }

        if (!isRequestValid(request)) {
            chain.doFilter(request, response);
            return;
        }

        if (!isCookieVerify(request, JwtVO.ACCESS_TOKEN)) {
            sendLoginRequiredResponse(response);
            return;
        }

        String accessToken = findAccessTokenFromCookie(request);
        log.debug("accessToken={}", accessToken);

        try {
            setAuthenticationFromAccessToken(accessToken);
            chain.doFilter(request, response);
        } catch (Exception e) {
            reIssueToken(request, response);
        }
    }

    private boolean isRequestValid(HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        return isApiAuthRequest(requestUrl) || isAdminRequest(requestUrl);
    }

    private boolean isApiAuthRequest(String requestUrl) {
        return requestUrl.contains("/api/auth/");
    }

    private boolean isAdminRequest(String requestUrl) {
        return requestUrl.contains("/th/admin/");
    }

    private String findAccessTokenFromCookie(HttpServletRequest request) {
        return findCookieValue(request, JwtVO.ACCESS_TOKEN).replace(JwtVO.TOKEN_PREFIX, "");
    }

    private void setAuthenticationFromAccessToken(String accessToken) {
        PrincipalDetails loginMember = jwtProcess.verify(accessToken);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginMember, null, loginMember.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private void reIssueToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!isCookieVerify(request, JwtVO.REFRESH_TOKEN)) {
            return;
        }
        String refreshTokenId = findCookieValue(request, JwtVO.REFRESH_TOKEN);
        try {
            RefreshToken findRefreshToken = findRefreshTokenOrElseThrowEx(refreshTokenRedisRepository, refreshTokenId);
            validateToken(request, response, findRefreshToken);
        } catch (Exception e) {
            handleTokenVerificationFailure(response);
        }
    }

    private void validateToken(HttpServletRequest request, HttpServletResponse response, RefreshToken findRefreshToken)
            throws IOException {
        String jwtToken = findRefreshToken.getRefreshToken().replace(JwtVO.TOKEN_PREFIX, "");
        String clientIpAddress = HttpServletUtil.getClientIpAddress(request);
        try {
            jwtProcess.isSatisfiedToken(jwtToken);
            validateIpAddress(findRefreshToken, clientIpAddress);
        } catch (Exception e) {
            handleTokenVerificationFailure(response);
        }
        reIssueNewToken(response, findRefreshToken, clientIpAddress);
    }

    private void validateIpAddress(RefreshToken findRefreshToken, String clientIpAddress) {
        if (!findRefreshToken.getIp().equals(clientIpAddress)) {
            log.error("다른 IP에서의 접근이 감지되었습니다. 보안을 위해 접속이 종료됩니다.");
            throw new UnauthorizedAccessException("다른 IP에서의 접근이 감지되었습니다. 보안을 위해 접속이 종료됩니다.");
        }
    }

    private void reIssueNewToken(HttpServletResponse response, RefreshToken findRefreshToken, String clientIpAddress) {
        String memberId = findRefreshToken.getMemberId();
        String memberRole = findRefreshToken.getRole();
        String newAccessToken = jwtProcess.createNewAccessToken(Long.valueOf(memberId), memberRole);
        refreshTokenRedisRepository.delete(findRefreshToken);
        String newRefreshTokenId = saveRefreshToken(jwtProcess, refreshTokenRedisRepository,
                memberId, memberRole, clientIpAddress);
        addCookie(response, JwtVO.ACCESS_TOKEN, newAccessToken);
        addCookie(response, JwtVO.REFRESH_TOKEN, newRefreshTokenId);
    }

    private void sendLoginRequiredResponse(HttpServletResponse response) {
        CustomResponseUtil.fail(response, "로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
    }
}