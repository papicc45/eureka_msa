//package com.example.api_gateway.filter;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ResponseStatusException;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//public class JwtTokenGlobalFilter implements GlobalFilter, Ordered {
//
//    @Value("${json-web-token.secret-key}")
//    private String secretKey;
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getPath().toString();
//        //category-service api들은 전부 토큰값 필요 없음
//        if(path.startsWith("/category/")){
//            return chain.filter(exchange);
//        }
//
//        //토큰값이 필요하지 않은 user-service api
//        if(path.equals("/user/signup") || path.equals("/user/signin") || path.equals("/api/login")) {
//            return chain.filter(exchange);
//        }
//
//        //토큰값이 필요하지 않은 board-service api
//        if(path.equals("/board/list") || path.startsWith("/board/detail/") || path.equals("/board/search")) {
//            return chain.filter(exchange);
//        }
//
//        //토큰값이 필요하지 않은 comment-service api
//        if(path.equals("/comment/comments")) {
//            return chain.filter(exchange);
//        }
//
//        //테스트용 api
//        if(path.equals("/comment/jwtTest2")) {
//            return chain.filter(exchange);
//        }
//
//        String jwtToken = exchange.getRequest().getHeaders().getFirst("Authorization");
//
//        //jwt 토큰의 디코딩 값이 필요한 api들( /comment/jwtTest 는 테스트용 )
//        if(path.equals("/comment/jwtTest") || path.equals("/board/write") || path.equals("/comment/write") || path.equals("/comment/reply")) {
//            try {
//                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken.replace("Bearer ", "")).getBody();
//
//                exchange.getAttributes().put("decodedToken", claims.getSubject());
//                exchange = exchange.mutate().request(exchange.getRequest().mutate().header("decodedToken", claims.toString()).build()).build();
//
//                return chain.filter(exchange);
//            } catch (Exception e) {
//                throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token");
//            }
//        }
//
//        //위 api 제외 나머지 api들
//        if(jwtToken == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Jwt Token is missing");
//        }
//
//        try {
//            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken.replace("Bearer ", ""));
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token");
//        }
//
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//}