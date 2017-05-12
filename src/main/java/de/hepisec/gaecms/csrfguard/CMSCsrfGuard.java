package de.hepisec.gaecms.csrfguard;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.owasp.csrfguard.CsrfGuard;
import org.owasp.csrfguard.CsrfGuardException;
import org.owasp.csrfguard.util.RandomGenerator;

/**
 * This is a replacement for the original CsrfGuardFilter. Manually call
 * validateRequest() when required.
 *
 * Other resources won't be protected.
 *
 * @author Hendrik Pilz
 */
public class CMSCsrfGuard {

    public static void validateRequest(HttpServletRequest request) throws CsrfGuardException {
        validateRequest(request, null);
    }
    
    public static void validateRequest(HttpServletRequest request, String tokenFromRequest) throws CsrfGuardException {
        //maybe the short circuit to disable is set
        if (!CsrfGuard.getInstance().isEnabled()) {
            return;
        }

        HttpSession session = request.getSession(false);

        //if there is no session and we arent validating when no session exists
        if (session == null && !CsrfGuard.getInstance().isValidateWhenNoSessionExists()) {
            // If there is no session, no harm can be done
            return;
        }

        CsrfGuard csrfGuard = CsrfGuard.getInstance();
        csrfGuard.getLogger().log(String.format("CsrfGuard analyzing request %s", request.getRequestURI()));

        performValidation(request, tokenFromRequest);

        /**
         * update tokens *
         */
        csrfGuard.updateTokens(request);
    }

    private static void performValidation(HttpServletRequest request, String tokenFromRequest) throws CsrfGuardException {
        CsrfGuard csrfGuard = CsrfGuard.getInstance();
        HttpSession session = request.getSession(true);
        String tokenFromSession = (String) session.getAttribute(csrfGuard.getSessionKey());

        if (tokenFromSession == null) {
            throw new CsrfGuardException("CsrfGuard expects the token to exist in session at this point");
        }

        /**
         * sending request to protected resource - verify token *
         */
        if (csrfGuard.isAjaxEnabled() && isAjaxRequest(request)) {
            verifyAjaxToken(request, tokenFromRequest);
        } else if (csrfGuard.isTokenPerPageEnabled()) {
            verifyPageToken(request, tokenFromRequest);
        } else {
            verifySessionToken(request, tokenFromRequest);
        }

        /**
         * rotate session and page tokens *
         */
        if (!isAjaxRequest(request) && csrfGuard.isRotateEnabled()) {
            rotateTokens(request);
        }
    }

    private static boolean isAjaxRequest(HttpServletRequest request) {
        return request.getHeader("X-Requested-With") != null;
    }

    private static void verifyAjaxToken(HttpServletRequest request, String tokenFromRequest) throws CsrfGuardException {
        CsrfGuard csrfGuard = CsrfGuard.getInstance();
        HttpSession session = request.getSession(true);
        String tokenFromSession = (String) session.getAttribute(csrfGuard.getSessionKey());

        if (null == tokenFromRequest) {
            tokenFromRequest = request.getHeader(csrfGuard.getTokenName());
        }        
        
        if (tokenFromRequest == null) {
            /**
             * FAIL: token is missing from the request *
             */
            throw new CsrfGuardException("required token is missing from the request");
        } else {
            //if there are two headers, then the result is comma separated
            if (!tokenFromSession.equals(tokenFromRequest)) {
                if (tokenFromRequest.contains(",")) {
                    tokenFromRequest = tokenFromRequest.substring(0, tokenFromRequest.indexOf(',')).trim();
                }
                if (!tokenFromSession.equals(tokenFromRequest)) {
                    /**
                     * FAIL: the request token does not match the session token
                     * *
                     */
                    throw new CsrfGuardException("request token does not match session token");
                }
            }
        }
    }

    private static void verifyPageToken(HttpServletRequest request, String tokenFromRequest) throws CsrfGuardException {
        CsrfGuard csrfGuard = CsrfGuard.getInstance();
        HttpSession session = request.getSession(true);
        @SuppressWarnings("unchecked")
        Map<String, String> pageTokens = (Map<String, String>) session.getAttribute(CsrfGuard.PAGE_TOKENS_KEY);

        String tokenFromPages = (pageTokens != null ? pageTokens.get(request.getRequestURI()) : null);
        String tokenFromSession = (String) session.getAttribute(csrfGuard.getSessionKey());

        if (null == tokenFromRequest) {
            tokenFromRequest = request.getParameter(csrfGuard.getTokenName());
        }
        
        if (tokenFromRequest == null) {
            /**
             * FAIL: token is missing from the request *
             */
            throw new CsrfGuardException("required token is missing from the request");
        } else if (tokenFromPages != null) {
            if (!tokenFromPages.equals(tokenFromRequest)) {
                /**
                 * FAIL: request does not match page token *
                 */
                throw new CsrfGuardException("request token does not match page token");
            }
        } else if (!tokenFromSession.equals(tokenFromRequest)) {
            /**
             * FAIL: the request token does not match the session token *
             */
            throw new CsrfGuardException("request token does not match session token");
        }
    }

    private static void verifySessionToken(HttpServletRequest request, String tokenFromRequest) throws CsrfGuardException {
        CsrfGuard csrfGuard = CsrfGuard.getInstance();
        HttpSession session = request.getSession(true);
        String tokenFromSession = (String) session.getAttribute(csrfGuard.getSessionKey());
        
        if (null == tokenFromRequest) {
            tokenFromRequest = request.getParameter(csrfGuard.getTokenName());
        }
        
        if (tokenFromRequest == null) {
            /**
             * FAIL: token is missing from the request *
             */
            throw new CsrfGuardException("required token is missing from the request");
        } else if (!tokenFromSession.equals(tokenFromRequest)) {
            /**
             * FAIL: the request token does not match the session token *
             */
            throw new CsrfGuardException("request token does not match session token");
        }
    }
    
    private static void rotateTokens(HttpServletRequest request) {
        CsrfGuard csrfGuard = CsrfGuard.getInstance();
        HttpSession session = request.getSession(true);

        /**
         * rotate master token *
         */
        String tokenFromSession = null;

        try {
            tokenFromSession = RandomGenerator.generateRandomId(csrfGuard.getPrng(), csrfGuard.getTokenLength());
        } catch (Exception e) {
            throw new RuntimeException(String.format("unable to generate the random token - %s", e.getLocalizedMessage()), e);
        }

        session.setAttribute(csrfGuard.getSessionKey(), tokenFromSession);

        /**
         * rotate page token *
         */
        if (csrfGuard.isTokenPerPageEnabled()) {
            @SuppressWarnings("unchecked")
            Map<String, String> pageTokens = (Map<String, String>) session.getAttribute(CsrfGuard.PAGE_TOKENS_KEY);

            try {
                pageTokens.put(request.getRequestURI(), RandomGenerator.generateRandomId(csrfGuard.getPrng(), csrfGuard.getTokenLength()));
            } catch (Exception e) {
                throw new RuntimeException(String.format("unable to generate the random token - %s", e.getLocalizedMessage()), e);
            }
        }
    }
}
