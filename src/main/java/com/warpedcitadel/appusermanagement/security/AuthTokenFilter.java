package com.warpedcitadel.appusermanagement.security;

//@Component
//public class AuthTokenFilter extends OncePerRequestFilter {
//
//    private static final String BEARER_ = "Bearer ";
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//        try {
//            String jwtToken = parseJwt(request);
//            if (jwtToken != null && jwtUtil.validateJwtToken(jwtToken)){
//
//                final String username = jwtUtil.getUserFromToken(jwtToken);
//
//                // add logic here
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String parseJwt(HttpServletRequest request) {
//        String headerAuth = request.getHeader("Authorization");
//        if (headerAuth != null && headerAuth.startsWith(BEARER_)) {
//            return headerAuth.substring(BEARER_.length());
//        }
//        return null;
//    }
//}

