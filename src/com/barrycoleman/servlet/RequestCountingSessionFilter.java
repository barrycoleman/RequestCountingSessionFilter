package com.barrycoleman.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: barry
 * Date: 4/24/12
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class RequestCountingSessionFilter implements Filter {
    static final int MAX_REQUESTS_PER_SESSION_DEFAULT = 0;
    static final int MAX_SIMULTANEOUS_REQUESTS_DEFAULT = 10;
    static final String REQUESTCOUNTING_REQUESTCOUNT = "_requestcounting_requestcount";
    static final String REQUESTCOUNTING_SIMULTANEOUSCOUNT = "_requestcounting_simultaneouscount";

    private FilterConfig mFilterConfig;

    boolean mEnabled=false;
    int mMaxRequestsPerSession=MAX_REQUESTS_PER_SESSION_DEFAULT;
    int mMaxSimultaneousRequestsPerSession=MAX_SIMULTANEOUS_REQUESTS_DEFAULT;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        mFilterConfig=filterConfig;

        String _maxRequests = mFilterConfig.getInitParameter("maxRequestsPerSession");
        String _maxSimultaneous = mFilterConfig.getInitParameter("maxSimultaneousRequests");
        String _enabled = mFilterConfig.getInitParameter("enabled");

        try {
            mEnabled = Boolean.parseBoolean(_enabled);
        } catch (NumberFormatException nfe) {
            // do nothing - leave at default value
        }
        if (_maxRequests != null) {
            try {
                mMaxRequestsPerSession = Integer.parseInt(_maxRequests);
                if (mMaxRequestsPerSession < 0) {
                    mMaxRequestsPerSession = MAX_REQUESTS_PER_SESSION_DEFAULT;
                }
            } catch (NumberFormatException nfe) {
                // do nothing - leave at default value
            }
        }

        if (_maxSimultaneous != null) {
            try {
                mMaxSimultaneousRequestsPerSession = Integer.parseInt(_maxSimultaneous);
                if (mMaxSimultaneousRequestsPerSession < 1) {
                    mMaxSimultaneousRequestsPerSession = MAX_SIMULTANEOUS_REQUESTS_DEFAULT;
                }
            } catch (NumberFormatException nfe) {
                // do nothing - leave at default value
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      if (mEnabled) {
          doRestrictingFilter(servletRequest,servletResponse,filterChain);
      } else {
          filterChain.doFilter(servletRequest, servletResponse);
      }
    }

    void doRestrictingFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
        HttpSession httpSession;
        boolean maxreached = false;
        int page_requestcount=0;
        int page_sessioncount=0;

        if ((httpSession=httpServletRequest.getSession()) != null) {
            synchronized (httpSession) {
                Integer sessionRequestCount = (Integer)httpSession.getAttribute(REQUESTCOUNTING_REQUESTCOUNT);
                Integer sessionSimultaneousRequestCount = (Integer)httpSession.getAttribute(REQUESTCOUNTING_SIMULTANEOUSCOUNT);

                if (sessionRequestCount == null) {
                    httpSession.setAttribute(REQUESTCOUNTING_REQUESTCOUNT,new Integer(1));
                    page_requestcount=1;
                } else {
                    int reqcount = sessionRequestCount.intValue();
                    if ((reqcount + 1) > mMaxRequestsPerSession && mMaxRequestsPerSession != 0) {
                        maxreached=true;
                    } else {
                        httpSession.setAttribute(REQUESTCOUNTING_REQUESTCOUNT, new Integer(reqcount+1));
                        page_requestcount=reqcount+1;
                    }
                }

                if (sessionSimultaneousRequestCount == null) {
                    httpSession.setAttribute(REQUESTCOUNTING_SIMULTANEOUSCOUNT, new Integer(1));
                    page_sessioncount=1;
                } else {
                    int sessioncount = sessionSimultaneousRequestCount.intValue();
                    if ((sessioncount + 1) > mMaxSimultaneousRequestsPerSession) {
                        maxreached=true;
                    } else {
                        httpSession.setAttribute(REQUESTCOUNTING_SIMULTANEOUSCOUNT, new Integer(sessioncount + 1));
                        page_sessioncount=sessioncount+1;
                    }
                }
            }

            if (!maxreached) {
                httpServletRequest.setAttribute("requestcounting_page", page_requestcount);
                httpServletRequest.setAttribute("requestcounting_session", page_sessioncount);
                filterChain.doFilter(servletRequest,servletResponse);
            } else {
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            synchronized (httpSession) {
                Integer endSessionSimultaneousRequestCount = (Integer)httpSession.getAttribute(REQUESTCOUNTING_SIMULTANEOUSCOUNT);

                if (endSessionSimultaneousRequestCount.intValue()>0) {
                    httpSession.setAttribute(REQUESTCOUNTING_SIMULTANEOUSCOUNT,new Integer(endSessionSimultaneousRequestCount.intValue()-1));
                }
            }
        }
    }

    @Override
    public void destroy() {
        mFilterConfig=null;
    }

    public boolean enable() {
        mEnabled = true;
        return mEnabled;
    }

    public boolean disable() {
        mEnabled = false;
        return true;
    }

    public boolean isEnabled() {
        return mEnabled;
    }
}
