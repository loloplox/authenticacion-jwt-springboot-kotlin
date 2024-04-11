package com.rodrigo.authenticationjwt.filters

import com.rodrigo.authenticationjwt.model.AuthenticationUser
import com.rodrigo.authenticationjwt.service.AuthenticationService
import com.rodrigo.authenticationjwt.utils.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.filter.OncePerRequestFilter

@CrossOrigin
@Component
@Order(1)
class AuthenticationFilter(
    private val authenticationService: AuthenticationService
): OncePerRequestFilter() {

    private val PRIVATE_ROUTES = listOf("/api/")
    private val EXCEPTIONAL_ROUTES = listOf(
        "/api/auth/*"
    )


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        try {
            val uri = request.requestURI
            val method = request.method

            println("$method $uri ${request.queryString}")

            if (method == "OPTIONS") {
                filterChain.doFilter(request, response)
                return
            }

            if (isPrivateRoute(uri)) {
                val token = request.getHeader("Authorization")
                if (token.isEmpty()) {
                    response.sendError(401, "Token not found")
                    return
                }

                val authenticationUser = AuthenticationUser(token = token)
                authenticationService.verify(authenticationUser)
            }

            filterChain.doFilter(request, response)
        } catch (ex: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.setHeader("Content-Type", "application/json")

            response.writer.write("{\"error\": \"${ex.message}\"}")
        }
    }

    private fun isPrivateRoute(uri: String): Boolean {
        return PRIVATE_ROUTES.any { uri.startsWith(it) && !isExceptionalRoute(uri) }
    }

    private fun isExceptionalRoute(uri: String): Boolean {
        return EXCEPTIONAL_ROUTES.any { exceptionalRoute ->
            val rutaSinAsterisco = exceptionalRoute.substring(0, exceptionalRoute.length - 2)

            if (exceptionalRoute.endsWith("/*")) {
                return uri.startsWith(rutaSinAsterisco)
            } else {
                return uri == exceptionalRoute
            }
        }
    }
}