package com.fpu.exe.cleaninghub.config;

import com.fpu.exe.cleaninghub.services.interfc.JWTService;
import com.fpu.exe.cleaninghub.services.interfc.UserService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5173")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null) {
                    // Log only specific headers and avoid the entire accessor to prevent lazy loading issues
                    log.info("STOMP Command: {}", accessor.getCommand());
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    log.info("Authorization Header Present: {}", authHeader != null);

                    if (StompCommand.CONNECT.equals(accessor.getCommand()) && authHeader != null) {
                        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
                        try {
                            String username = jwtService.extractUsername(token);
                            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                            SecurityContextHolder.getContext().setAuthentication(authToken);
                            accessor.setUser(authToken);
                            log.info("User authenticated for WebSocket: {}", username);
                        } catch (Exception e) {
                            log.error("WebSocket authentication failed", e);
                            return null; // Reject connection if authentication fails
                        }
                    }
                }
                return message;
            }
        });
    }
}
